/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;
import Modelo.InformaciónEstadistica;
import Controlador.Estadisticas;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Gersan
 */
public class Impresor {
    FileWriter writer;
    Estadisticas estadistica;

    public Impresor() {
    }
    
    
    private void write_with_throws(String filepath, String text) throws IOException{
        File file = new File(filepath);
        if(!file.exists()){
            file.createNewFile();
        }
        writer = new FileWriter(file); 
        // Writes the content to the file
        writer.write(text); 
        writer.flush();

    }  
    
    public void write_file(String filepath, String text){
        try {
            write_with_throws(filepath, text);
        } catch (IOException ex) {
            Logger.getLogger(Impresor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void imprimir_estadisticas(Estadisticas esta,
            String path){
        this.estadistica = esta;
        write_file(path, cantones());
        
    }
    
    
    public String provincias(){       
        HashMap<String, InformaciónEstadistica> provincias;
        provincias = estadistica.getProvincias();
        String dev="";
        
        Set set = provincias.keySet();
        Iterator iter = set.iterator();
        String clave;
        InformaciónEstadistica info;
        while(iter.hasNext()){
            clave = (String)iter.next();
            dev += "##############################\n"+clave+":\n";
            
            info = (InformaciónEstadistica) provincias.get(clave);
            dev += "Femenino: "+info.femenino+"\nMasculino: "+
                    info.masculino+"\nTotal: "+info.total+"\n";
        }
        return dev;
    }
    
    public String cantones(){
        String dev="";
        HashMap<String, InformaciónEstadistica> cantones;
        HashMap<String, String> provincias= new HashMap<>();
        cantones = estadistica.getCantones();
        
        Set set = cantones.keySet();
        Iterator iter = set.iterator();
        
        String[] infoProv;
        String clave;
        InformaciónEstadistica info;
        while(iter.hasNext()){
            clave = (String)iter.next();

            infoProv = clave.split(":");
            if(!provincias.containsKey(infoProv[0])){
                provincias.put(infoProv[0], "");
            }
            
            dev = "----- "+infoProv[1]+": -----\n";
            
            info = (InformaciónEstadistica) cantones.get(clave);
            dev += "\tFemenino: "+info.femenino+"\n\tMasculino: "+
                    info.masculino+"\n\tTotal: "+info.total+"\n";
            
            provincias.replace(infoProv[0], 
                provincias.get(infoProv[0]) + dev
                               );
        }
        
        
        set = provincias.keySet();
        iter = set.iterator();
        dev = provincias()+"\n\n\n";
        while(iter.hasNext()){ 
            clave = (String)iter.next();
            dev += "##############################\n"+clave+":\n";
            dev += provincias.get(clave);
        }
        return dev;    
    
    }
}
