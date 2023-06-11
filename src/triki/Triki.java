/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triki;

import jade.Boot;

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

        String[] opciones2 = { "-gui", "-agents", "jugadorB:Agentes.JugadorB;jugadorA:Agentes.JugadorA"};
        Boot.main(opciones2); 
    }   
}
