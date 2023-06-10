/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author carlo
 */
public class JugadorA extends Agent{
    // Numero random
    String modeJuego = "";
    
    // Modo de juego
    Random random = new Random();
    
    // Tablero de juego
    String[][] tablero = new String[][]{
        {"-","-","-"},
        {"-","-","-"},
        {"-","-","-"},            
        
    };
    
    //Resivir y enviar mensaje
    ACLMessage mensaje;

    @Override
    protected void setup() {       
        addBehaviour(new AgregarXBehaviour());
        addBehaviour(new RecibirOBehaviour());
    }
    
    private class AgregarXBehaviour extends OneShotBehaviour {
        @Override
        public void action() {           
            // Agregar X al tablero
            modeJuego = JOptionPane.showInputDialog("Configuracion Jugador A \n\nModo de juego: \n 1: manual. \n 2: aleatorio.");                
            if(!modeJuego.equals("1") && !modeJuego.equals("2")){
                seleccionarModeJuego();
            }   
            modoJuegoAleatorio();           
        }
    }
    
    
    
    private class RecibirOBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            // Esperar un mensaje del AgenteA
            mensaje = receive();
            String hayGanador = "";
            if (mensaje != null) {
                try {
                    // Actualizar el tablero con la respuesta del AgenteA
                    tablero = (String[][]) mensaje.getContentObject();
                    hayGanador = evaluarGanador();
                    imprimirTablero();                    
                    
                    if (hayGanador.isEmpty()) {
                        // Agregar O al tablero y enviarlo nuevamente al AgenteA
                        if(tableroLleno()){
                            JOptionPane.showMessageDialog(null, "Empate");                        
                            doDelete();  
                            
                        }else{
                            modoJuegoAleatorio();                           
                        }                       
                    } else {
                        // Detener el agente si el tablero está lleno
                        JOptionPane.showMessageDialog(null, hayGanador);    
                         block();
                        doDelete();                       
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                block();
            }
        }
    }
    
    //Modo de juego Aleatorio
    private void modoJuegoAleatorio(){     
        int fila = random.nextInt(3);
        int columna = random.nextInt(3);
        
        if(tablero[fila][columna].equals("O") ){
            modoJuegoAleatorio();           
        }else if(tablero[fila][columna].equals("X")){
            modoJuegoAleatorio();
        }else{
            try {
                tablero[fila][columna] = "X";
                mensaje = new ACLMessage(ACLMessage.INFORM);
                mensaje.addReceiver(new AID("jugadorB", AID.ISLOCALNAME));
                mensaje.setContentObject(tablero); 
                send(mensaje);
            } catch (IOException ex) {
                Logger.getLogger(JugadorB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }                
    }
    
    // Evalua si el tablero se encuentra lleno
    private boolean tableroLleno(){
        boolean tableroLleno = true;
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {                            
                if (tablero[i][j].equals("-")) {                                
                    tableroLleno = false;                                
                    return tableroLleno;                          
                }            
            }                                                       
        }
        return tableroLleno;
    }
    
    //Evalua si existe un ganador
    private String evaluarGanador(){
        
        // Verificar filas
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0].equals(tablero[i][1]) && tablero[i][1].equals(tablero[i][2])) {
                if (tablero[i][0].equals("X")) {
                    return "Perdiste la partida";
                }
                if (tablero[i][0].equals("O")) {
                    return "Ganaste la partida";
                }
            }
        }
        
        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            if (tablero[0][j].equals(tablero[1][j]) && tablero[1][j].equals(tablero[2][j])) {
                if (tablero[0][j].equals("X")) {
                    return "Perdiste la partida";
                }
                if (tablero[0][j].equals("O")) {
                    return "Ganaste la partida";
                }
            }
        }
        
        // Verificar diagonales
        if (tablero[0][0].equals(tablero[1][1]) && tablero[1][1].equals(tablero[2][2])) {
            if (tablero[0][0].equals("X")) {
                return "Perdiste la partida";
            }
            if (tablero[0][0].equals("O")) {
                return "Ganaste la partida";
            }
        }
        
        if (tablero[0][2].equals(tablero[1][1]) && tablero[1][1].equals(tablero[2][0])) {
            if (tablero[0][2].equals("X")) {
                return "Perdiste la partida";
            }
            if (tablero[0][2].equals("O")) {
                return "Ganaste la partida";
            }
        }        
        return "";       
    }
    
    private void modoJuegoManual(){
            
    }
    
    private void imprimirTablero(){
        System.out.println("Jugada del jugador B");
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                System.out.print(tablero[i][j] + " ");
            }        
            System.out.println();                        
        }                         
        System.out.println();                   
    }  
    
    private void seleccionarModeJuego(){
        modeJuego = JOptionPane.showInputDialog("Configuracion Jugador A \n\nSeleccione un modo de juego valido: \n 1: manual. \n 2: aleatorio.");
        if(!modeJuego.equals("1") && !modeJuego.equals("2")){
            seleccionarModeJuego();
        }
    }
    
    
}