/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Control.Lock;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Gersan
 */
public class Iterador {
    BufferedReader br;
    boolean finaliced;
    Lock lock;

    public Iterador(String path) {
        finaliced = false;
        lock = new Lock();
        try {
            read_file(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Iterador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void read_file(String path) throws FileNotFoundException {
        FileInputStream fstream = new FileInputStream(path);
        br = new BufferedReader(new InputStreamReader(fstream));
    }

    
    public boolean hasNext() {
        return !finaliced;
    }

    private String get_new_line() {
        String strLine = null;
        try {
            
           
            lock.lock();
            if(!finaliced)
            strLine = br.readLine();
            lock.unlock();
            
            
        } catch (IOException ex) {
            Logger.getLogger(Iterador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Iterador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strLine;
    }

    
    public Object next() {
        
        String strLine=null;
        if(!finaliced){
            strLine = get_new_line();
            if (strLine == null) {
                finaliced = true;
                closeBuffer();
            }
        }
        return strLine;
    }

    
    private void closeBuffer(){
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Iterador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
