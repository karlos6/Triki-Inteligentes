/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triki;

import jade.Boot;
import java.util.Random;

/**
 *
 * @author carlo
 */
public class Triki {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here        
        String[] opciones2 = { "-gui", "-agents", "jugadorA:Agentes.JugadorA;jugadorB:Agentes.JugadorB"};
        Boot.main(opciones2);        
    }
    
   
    
}