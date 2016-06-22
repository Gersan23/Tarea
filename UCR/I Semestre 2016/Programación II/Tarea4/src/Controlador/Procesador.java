/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Controlador.Estadisticas;
import Controlador.Iterador;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Gersan
 */
public class Procesador implements Runnable {
    private Iterador iter;
    private Estadisticas estadisticaTemporal;
    private Estadisticas estadistica;
    private int iteraciones;
    private List<Thread> threads;

    public Procesador(Iterador iter, Estadisticas estadistica, int iteraciones) {
        this.iter = iter;
        this.estadistica = estadistica;
        this.iteraciones = iteraciones;
        this.estadisticaTemporal = new Estadisticas();
        threads = new ArrayList<>();
    }

    @Override
    public void run() {
    
        String linea=null;
        int lineas_procesadas = 0;
        if(iter.hasNext())  linea= (String)iter.next();
        
        while(iter.hasNext() && linea!= null){
            for(int x=0; x<iteraciones && iter.hasNext(); x++){
                estadisticaTemporal.procesarInformacion(linea);
                linea = (String)iter.next();
                lineas_procesadas++;
            }
            //System.out.println("Líneas procesadas: "+lineas_procesadas);
            
            estadistica.migraEstadisticas(estadisticaTemporal);
            estadisticaTemporal.limpiar();
            
        }
    }
    
   public void start (){
       Thread t;
       for(int x=0; x<10; x++){
           t = new Thread (new Procesador(
           iter, estadistica, iteraciones),
                   
                   "Estadisticas "+x);
           threads.add(t);
           t.start();
       }
   }
    
   public void join(){
   
       for(int x=0; x<threads.size(); x++){
           try {
               threads.get(x).join();
           } catch (InterruptedException ex) {
               Logger.getLogger(Procesador.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
   }
    
    
}
