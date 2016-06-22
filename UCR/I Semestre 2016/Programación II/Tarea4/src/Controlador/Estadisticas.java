/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Modelo.InformaciónEstadistica;
import Control.Lock;
import Modelo.MapaCodigoElectoral;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Gersan
 */
public class Estadisticas {
    private HashMap<String, InformaciónEstadistica> cantones;
    private HashMap<String, InformaciónEstadistica> provincias;
    private MapaCodigoElectoral mapa;
    private Lock lock;


    public Estadisticas(Lock lock) {
        cantones = new HashMap<>();
        provincias = new HashMap<>();
        mapa = MapaCodigoElectoral.getInstance();
        this.lock = lock;
    }
    
    public Estadisticas() {
        cantones = new HashMap<>();
        provincias = new HashMap<>();
        mapa = MapaCodigoElectoral.getInstance();
        lock = null; // new Lock();

    }
    
    public void agregarEstadistica(String codElectoral, int genero){
        String nomCanton = mapa.get_canton(codElectoral);
        String nomProvincia = mapa.get_provincia(codElectoral);
        if(!cantones.containsKey(nomCanton)){
            cantones.put(nomCanton, new InformaciónEstadistica());
        }
        if(!provincias.containsKey(nomProvincia)){
            provincias.put(nomProvincia, new InformaciónEstadistica());
        }
        
        InformaciónEstadistica icantones = cantones.get(nomCanton);
        InformaciónEstadistica iprovincias = provincias.get(nomProvincia);
        

        icantones.agregar1(genero);
        iprovincias.agregar1(genero);
        
        cantones.replace(nomCanton, icantones);
        provincias.replace(nomProvincia, iprovincias);
    }
    
    public void procesarInformacion(String linea){
        String[] dato = linea.split(",");
        // cedula   codElec genero
        // 100653102,112022,2,20200625,00000,TERESA                        ,DURAN                     ,MORA
        int genero = Integer.parseInt(dato[2]);
        agregarEstadistica(dato[1], genero);
    
    }
    
    private void migrateHashMap(HashMap<String, InformaciónEstadistica> original,
        HashMap<String, InformaciónEstadistica> copiar){
        Set set = copiar.keySet();
        
        Iterator iter = set.iterator();
        String clave;
        InformaciónEstadistica tmpEstadistica;
        InformaciónEstadistica tmporiginaEstadistica;
        while(iter.hasNext()){
            clave = (String)iter.next();
            if(original.containsKey(clave)){
                tmporiginaEstadistica = original.get(clave);
                tmpEstadistica = copiar.get(clave);
                
                tmporiginaEstadistica.agregar(tmpEstadistica.masculino,
                tmpEstadistica.femenino, tmpEstadistica.total);
                
                original.replace(clave, tmporiginaEstadistica); 
            }else{
                original.put(clave, copiar.get(clave));
            }
        }
        
    }
   
    private void _migraEstadisticas(Estadisticas estas) throws InterruptedException{
        lock.lock();
        migrateHashMap(cantones, estas.getCantones());
        migrateHashMap(provincias, estas.getProvincias());
        lock.unlock();    
    }
    public void migraEstadisticas(Estadisticas estas){
        try { 
            _migraEstadisticas(estas);
        } catch (InterruptedException ex) {
            Logger.getLogger(Estadisticas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, InformaciónEstadistica> getCantones() {
        return cantones;
    }

    public HashMap<String, InformaciónEstadistica> getProvincias() {
        return provincias;
    }
    
    public void limpiar(){
        cantones = new HashMap<>();
        provincias = new HashMap<>();
    }
}
