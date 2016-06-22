/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PadronElectoral;
import Modelo.MapaCodigoElectoral;
import Control.Lock;
import Controlador.Estadisticas;
import Controlador.Iterador;
import Controlador.Procesador;
import Vista.Impresor;
/**
 *
 * @author Gersan
 */
public class Main {
    public static void usage(){
        System.out.println("Modo de uso:\n "+
                "Con java: "+
                "$ java padronelectoral.Main Distelec.txt PADRON_COMPLETO.txt resultado.txt"+
                "\n Con Make: "+
                "$ make run Distelec.txt PADRON_COMPLETO.txt resultado.txt"
                );
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if(args.length != 3){
            Main.usage();
            System.exit(1);
        }
        
        MapaCodigoElectoral est = MapaCodigoElectoral.getInstance();
        est.cargar_codigo_electoral(args[0]);
        Lock lockEst = new Lock();
        
        Iterador iter = new Iterador(args[1]);
        Estadisticas estadistica = new Estadisticas(lockEst);
        Procesador procesa = new Procesador(iter, estadistica, 10);
        procesa.start();
        procesa.join();
        Impresor imprime = new Impresor();
        imprime.imprimir_estadisticas(estadistica,
                args[2]);
        
        
    }
    
}
