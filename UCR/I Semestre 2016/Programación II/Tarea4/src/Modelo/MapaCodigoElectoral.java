/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.HashMap;
import Controlador.Iterador;
/**
 *
 * @author Gersan
 */
public class MapaCodigoElectoral {
    private static MapaCodigoElectoral mapa = new MapaCodigoElectoral();
    private HashMap<String, MapaCodElec> mapasElectoral;
    
    private class MapaCodElec {
        public String provincia;
        public String canton; 
    } 
   
    private MapaCodigoElectoral(){
        mapasElectoral = new HashMap<>();
    }
    
    public static MapaCodigoElectoral getInstance(){
        return mapa;
    }
    
   private void agregar_codigo(String[] datos){
        MapaCodElec mapa = new MapaCodElec();
        mapa.provincia = datos[1];
        mapa.canton = datos[2];
        mapasElectoral.put(datos[0], mapa);
    }
    
    public void cargar_codigo_electoral(String path){
        Iterador iter = new Iterador(path);
        String linea = (String)iter.next();
        while(iter.hasNext() && linea != null){
            // cod electoral, provincia, canton
            //101001,SAN JOSE,CENTRAL,HOSPITAL
            String[] datos = linea.split(",");
            agregar_codigo(datos);
            linea = (String)iter.next();
        }
    }
    
    public String get_provincia(String codElectoral){
        MapaCodElec mapa =  mapasElectoral.get(codElectoral);
        return mapa.provincia;
    }
    
    public String get_canton(String codElectoral){
        MapaCodElec mapa =  mapasElectoral.get(codElectoral);
        return mapa.provincia+":"+mapa.canton;
    } 
}
