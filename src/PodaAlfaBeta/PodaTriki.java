/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PodaAlfaBeta;

/**
 *
 * @author Asus
 */
public class PodaTriki {
    
    
    public static int[] minimax(String[][] tablero, int profundidad, boolean esMaximizador) {
        if (haGanado(tablero, "X")) {
            return new int[]{10 - profundidad, -1, -1};
        } else if (haGanado(tablero, "O")) {
            return new int[]{profundidad - 10, -1, -1};
        } else if (hayEmpate(tablero)) {
            return new int[]{0, -1, -1};
        }

        if (esMaximizador) {
            int[] mejorMovimiento = {-1, -1, Integer.MIN_VALUE};
            for (int fila = 0; fila < tablero.length; fila++) {
                for (int columna = 0; columna <  tablero[fila].length; columna++) {
                    if (tablero[fila][columna].equals("-")) {
                        tablero[fila][columna] = "X";
                        int[] resultado = minimax(tablero, profundidad + 1, false);
                        tablero[fila][columna] = "-";
                        if (resultado[2] > mejorMovimiento[2]) {
                            mejorMovimiento[0] = fila;
                            mejorMovimiento[1] = columna;
                            mejorMovimiento[2] = resultado[2];
                        }
                    }
                }
            }
            return mejorMovimiento;
        } else {
            int[] mejorMovimiento = {-1, -1, Integer.MAX_VALUE};
            for (int fila = 0; fila < tablero.length; fila++) {
                for (int columna = 0; columna < tablero[fila].length; columna++) {
                    if (tablero[fila][columna].equals("-")) {
                        tablero[fila][columna] = "O";
                        int[] resultado = minimax(tablero, profundidad + 1, true);
                        tablero[fila][columna] = "-";
                        if (resultado[2] < mejorMovimiento[2]) {
                            mejorMovimiento[0] = fila;
                            mejorMovimiento[1] = columna;
                            mejorMovimiento[2] = resultado[2];
                        }
                    }
                }
            }
            return mejorMovimiento;
        }
    }

    public static boolean haGanado(String[][] tablero, String jugador) {
        for (int fila = 0; fila < tablero.length; fila++) {
            if (tablero[fila][0].equals(jugador) && tablero[fila][1].equals(jugador) && tablero[fila][2].equals(jugador)) {
                return true;
            }
        }

        for (int columna = 0; columna < tablero.length; columna++) {
            if (tablero[0][columna].equals(jugador) && tablero[1][columna].equals(jugador) && tablero[2][columna].equals(jugador)) {
                return true;
            }
        }

        if (tablero[0][0].equals(jugador) && tablero[1][1].equals(jugador) && tablero[2][2].equals(jugador)) {
            return true;
        }

        if (tablero[0][2].equals(jugador) && tablero[1][1].equals(jugador) && tablero[2][0].equals(jugador)) {
            return true;
        }

        return false;
    }

    public static boolean hayEmpate(String[][] tablero) {
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int columna = 0; columna < tablero[fila].length; columna++) {
                if (tablero[fila][columna].equals("-")) {
                    return false;
                }
            }
        }
        return true;
    }
}
