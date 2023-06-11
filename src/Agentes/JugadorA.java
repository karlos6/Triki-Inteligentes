/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import PodaAlfaBeta.PodaTriki;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author carlo
 */
public class JugadorA extends Agent{
    // Numero random
    String modeJuego = "";    
   
    // Tablero de juego
    String[][] tablero = new String[][]{
        {"-","-","-"},
        {"-","-","-"},
        {"-","-","-"},            
        
    };
    
    //Resivir y enviar mensaje
    ACLMessage mensaje;
    
    // Expresion Regular
    String expresionRegular = "^(?:[0-2]),(?:[0-2])$";
    
    // Compilar la expresión regular en un patrón
    Pattern patron = Pattern.compile(expresionRegular);
    
    // Tablero string para mostrar en pantalla
    String tableroString = "";
    
    // Ganaste la partida
    String ganaste = "Jugador A: \nGanaste la partida";
    
    // Perdiste la partida
    String perdiste = "Jugador A: \nPerdiste la partida";

    @Override
    protected void setup() {       
        addBehaviour(new AgregarXBehaviour());
        addBehaviour(new RecibirOBehaviour());
    }
    
    // Comportamiento que se ejecuta solo una vez
    private class AgregarXBehaviour extends OneShotBehaviour {
        @Override
        public void action() {           
            // Agregar X al tablero
            modeJuego = JOptionPane.showInputDialog("Configuracion Jugador A \n\nModo de juego: \n 1: manual. \n 2: aleatorio.");          
            if(!modeJuego.equals("1") && !modeJuego.equals("2")){
                seleccionarModeJuego();
            }   
            if(modeJuego.equals("1")){
                modoJuegoManual();
            }else{
                modoJuegoAleatorio();
            }           
        }
    }
    
    
    // Comportamiento Jugador A 
    private class RecibirOBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            // Esperar un mensaje del AgenteA
            mensaje = receive();
            String hayGanador = "";
            if (mensaje != null) {
                try {
                    System.out.println(mensaje.getContent());
                   
                    hayGanador = evaluarGanador();
                    imprimirTablero();                    
                    
                    if (hayGanador.isEmpty()) {
                        // Agregar O al tablero y enviarlo nuevamente al AgenteA
                        if(tableroLleno()){
                            JOptionPane.showMessageDialog(null, "Empate");                        
                            doDelete();  
                            
                        }else{
                            if(modeJuego.equals("1")){
                                modoJuegoManual();
                            }else{
                                modoJuegoAleatorio();
                            }
                                                       
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
    
    // Modo de juego Manual
    private void modoJuegoManual(){
        stringTablero();
        String cordenadas = JOptionPane.showInputDialog("Jugador A \n\nIngrese la cordenada: \n Ejempo: (1,2), donde 1 es la fila y 2 la columna.\n\n" + tableroString);
        Matcher matcher = patron.matcher(cordenadas);       
        // Verificar si la cadena coincide con la expresión regular
        if (matcher.matches()) {
            String[] CordenadaXY = cordenadas.split(",");
            String casilla = tablero[Integer.parseInt(CordenadaXY[0])][Integer.parseInt(CordenadaXY[1])];
            if(casilla.equals("X") || casilla.equals("O")){
                seleccionarCordenadaValida();
            }else{
                tablero[Integer.parseInt(CordenadaXY[0])][Integer.parseInt(CordenadaXY[1])] = "X";
                enviarMensajeJugadorB();
            }                       
        }else{
            seleccionarCordenadaValida();
        }            
    }   
    
    // Enviar mensaje al jugador B
    private void enviarMensajeJugadorB(){
        mensaje = new ACLMessage(ACLMessage.INFORM);
        AID r = new AID("receptor@192.168.0.104:1099/JADE", AID.ISGUID);
        r.addAddresses("http://192.168.0.104:7778/acc");
        mensaje.addReceiver(r);
        // Establecer el arreglo de bytes como contenido del mensaje
        mensaje.setContent(tablero.toString());
        send(mensaje);
    }
    
    
    
    // Seleccionar una cordenada valida
    private void seleccionarCordenadaValida(){
        String cordenadas = JOptionPane.showInputDialog("Jugador A \n\nIngrese la cordenada valida: "
                +"\n* No puede seleccionar una casilla con una letra."
                +"\n* No puede seleccionar un rango de fila o columna mayor a 2 y menor a 0. "
                +"\n* No puede contener letras ni caracteres especiales."
                + "\n\nNota de ejemplo: (1,2) donde 1 es la fila y 2 la columna.\n\n" + tableroString+"\n\n");
        Matcher matcher = patron.matcher(cordenadas);
        // Verificar si la cadena coincide con la expresión regular
        if (matcher.matches()) {
            String[] CordenadaXY = cordenadas.split(",");
            String casilla = tablero[Integer.parseInt(CordenadaXY[0])][Integer.parseInt(CordenadaXY[1])];
            if(casilla.equals("X") || casilla.equals("O")){
                seleccionarCordenadaValida();
            }else{
                tablero[Integer.parseInt(CordenadaXY[0])][Integer.parseInt(CordenadaXY[1])] = "X";
                enviarMensajeJugadorB();   
            }                     
        }else{
            seleccionarCordenadaValida();
        }
    }
    
    // Metodo para sacar el string del tablero para mostrar en pantalla.
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
    
    //Modo de juego Aleatorio
    private void modoJuegoAleatorio(){   
        
       int[] mejorMovimiento = PodaTriki.minimax(tablero, 0, true);
        int fila = mejorMovimiento[0];
        int columna = mejorMovimiento[1]; 
        
        System.out.println("Jugador A, fila : "+fila+ " columna : "+columna);
        if(tablero[fila][columna].equals("O") ){
            modoJuegoAleatorio();           
        }else if(tablero[fila][columna].equals("X")){
            modoJuegoAleatorio();
        }else{            
            tablero[fila][columna] = "X";
            enviarMensajeJugadorB();            
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
                    return ganaste;
                }
                if (tablero[i][0].equals("O")) {
                    return perdiste;
                }
            }
        }
        
        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            if (tablero[0][j].equals(tablero[1][j]) && tablero[1][j].equals(tablero[2][j])) {
                if (tablero[0][j].equals("X")) {
                    return ganaste;
                }
                if (tablero[0][j].equals("O")) {
                    return perdiste;
                }
            }
        }
        
        // Verificar diagonales
        if (tablero[0][0].equals(tablero[1][1]) && tablero[1][1].equals(tablero[2][2])) {
            if (tablero[0][0].equals("X")) {
                return ganaste;
            }
            if (tablero[0][0].equals("O")) {
                return perdiste;
            }
        }
        
        if (tablero[0][2].equals(tablero[1][1]) && tablero[1][1].equals(tablero[2][0])) {
            if (tablero[0][2].equals("X")) {
                return ganaste;
            }
            if (tablero[0][2].equals("O")) {
                return perdiste;
            }
        }        
        return "";       
    }
    
    //Imprimir el tablero    
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
    
    // Seleccionar un modo de juego valido
    private void seleccionarModeJuego(){
        modeJuego = JOptionPane.showInputDialog("Configuracion Jugador A \n\nSeleccione un modo de juego valido: \n 1: manual. \n 2: aleatorio.");
        if(!modeJuego.equals("1") && !modeJuego.equals("2")){
            seleccionarModeJuego();
        }
    }   
    
    
}
