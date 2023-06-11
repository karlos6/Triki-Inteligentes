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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author carlo
 */
public class JugadorB extends Agent{
    // Numero random 
    Random random = new Random();
    
    // Modo de juego
    String modeJuego = "";  
    
    // Tablero de juego
    String[][] tablero = new String[][]{   
        {"-","-","-"},
        {"-","-","-"},
        {"-","-","-"},                
    };
    
    //Resivir y enviar mensaje
    ACLMessage mensaje;
    
    // Ganaste la partida
    String ganaste = "Jugador B: \nGanaste la partida";
    
    // Perdiste la partida
    String perdiste = "Jugador B: \nPerdiste la partida";
    
    // Tablero string para mostrar en pantalla
    String tableroString = "";
    
    // Expresion Regular
    String expresionRegular = "^(?:[0-2]),(?:[0-2])$";
    
    // Compilar la expresión regular en un patrón
    Pattern patron = Pattern.compile(expresionRegular);

    protected void setup() {
        modeJuego = JOptionPane.showInputDialog("Configuracion Jugador B \n\nModo de juego: \n 1: manual. \n 2: aleatorio.");
                
            if(!modeJuego.equals("1") && !modeJuego.equals("2")){
               seleccionarModeJuego();
            }   
        
        addBehaviour(new RecibirXBehaviour());
        //addBehaviour(new AgregarOBehaviour());
    }
    
    // Comportamiento jugador B
    private class RecibirXBehaviour extends CyclicBehaviour {

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
                            enviarMensajeJugadorA(); 
                            doDelete(); 
                        }else{
                            if(modeJuego.equals("1")){
                                modoJuegoManual();
                                block();
                            }else{
                                modoJuegoAleatorio();
                                block();
                            }                                                       
                        }                       
                    } else {
                        // Detener el agente si el tablero está lleno
                        JOptionPane.showMessageDialog(null, hayGanador);
                        enviarMensajeJugadorA(); 
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
            tablero[fila][columna] = "O";
            enviarMensajeJugadorA();           
        }                
    }
    
    //Modo de juego Manual
    private void modoJuegoManual(){
        stringTablero();
        String cordenadas = JOptionPane.showInputDialog("Jugador B \n\nIngrese la cordenada: \n Ejempo: (1,2), donde 1 es la fila y 2 la columna.\n\n" + tableroString);
        Matcher matcher = patron.matcher(cordenadas);       
        // Verificar si la cadena coincide con la expresión regular
        if (matcher.matches()) {
            String[] CordenadaXY = cordenadas.split(",");
            String casilla = tablero[Integer.parseInt(CordenadaXY[0])][Integer.parseInt(CordenadaXY[1])];
            if(casilla.equals("X") || casilla.equals("O")){
                seleccionarCordenadaValida();
            }else{
                tablero[Integer.parseInt(CordenadaXY[0])][Integer.parseInt(CordenadaXY[1])] = "O";
                enviarMensajeJugadorA(); 
            }
        }else{
            seleccionarCordenadaValida();
        }            
    }
    
    // Validar un coordenada valida.
    private void seleccionarCordenadaValida(){
         String cordenadas = JOptionPane.showInputDialog("Jugador B \n\nIngrese la cordenada valida: "
                +"\n* No puede seleccionar una casilla con una letra."
                +"\n* No puede seleccionar un rango de fila o columna mayor a 2 y menor a 0. "
                +"\n* No puede contener letras ni caracteres especiales."
                + "\n\nNota de ejemplo: (1,2) donde 1 es la fila y 2 la columna.\n\n" + tableroString+"\n\n");
        Matcher matcher = patron.matcher(cordenadas);
        // Verificar si la cadena coincide con la expresión regular
        if (matcher.matches()) {
            String[] CordenadaXY = cordenadas.split(",");
            tablero[Integer.parseInt(CordenadaXY[0])][Integer.parseInt(CordenadaXY[1])] = "O";
            enviarMensajeJugadorA();            
        }else{
            seleccionarCordenadaValida();
        }
    }
    
    // Enviar mensaje al jugador B.
    private void enviarMensajeJugadorA(){
        try {
            mensaje = new ACLMessage(ACLMessage.INFORM);
            mensaje.addReceiver(new AID("jugadorA", AID.ISLOCALNAME));
            mensaje.setContentObject(tablero);        
            send(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(JugadorA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Tablero string para mostrar en pantalla.
    private void stringTablero(){
        tableroString = "    0  1  2 \n";
        for (int i = 0; i < tablero.length; i++) {
            tableroString = tableroString + i + "  "; 
            for (int j = 0; j < tablero[i].length; j++) {
                tableroString = tableroString+ "  " + tablero[i][j] + " ";
            }        
            tableroString = tableroString + "\n";                        
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
                    return perdiste;
                }
                if (tablero[i][0].equals("O")) {
                    return ganaste;
                }
            }
        }
        
        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            if (tablero[0][j].equals(tablero[1][j]) && tablero[1][j].equals(tablero[2][j])) {
                if (tablero[0][j].equals("X")) {
                    return perdiste;
                }
                if (tablero[0][j].equals("O")) {
                    return ganaste;
                }
            }
        }
        
        // Verificar diagonales
        if (tablero[0][0].equals(tablero[1][1]) && tablero[1][1].equals(tablero[2][2])) {
            if (tablero[0][0].equals("X")) {
                return perdiste;
            }
            if (tablero[0][0].equals("O")) {
                return ganaste;
            }
        }
        
        if (tablero[0][2].equals(tablero[1][1]) && tablero[1][1].equals(tablero[2][0])) {
            if (tablero[0][2].equals("X")) {
                return perdiste;
            }
            if (tablero[0][2].equals("O")) {
                return ganaste;
            }
        }        
        return "";       
    }    
    
    // Imprimir el tablero
    private void imprimirTablero(){
        System.out.println("Jugada del jugador A");
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                System.out.print(tablero[i][j] + " ");
            }        
            System.out.println();                        
        }                         
        System.out.println();                   
    }
    
    // Metodo que se ejecuta solo una vez. 
    private class AgregarOBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
           
                // Agregar O al tablero
                tablero[1][1] = "O";
                
                // Enviar el tablero al AgenteA
                ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
                enviarMensajeJugadorA(); 
            
        }
    }
    
    // Seleccionar modo de juego. 
    private void seleccionarModeJuego(){
        modeJuego = JOptionPane.showInputDialog("Configuracion Jugador B \n\nSeleccione un modo de juego valido: \n 1: manual. \n 2: aleatorio.");
        if(!modeJuego.equals("1") && !modeJuego.equals("2")){
            seleccionarModeJuego();
        }
    }   
}
