package fes.aragon.modelo;

import java.util.HashMap;

public class AutomataConfigurable {
    private boolean configurado = false;
    private int[][] matriz;
    private boolean[] estado;
    private HashMap<String, Integer> alfabeto;
    private int filas;
    private int columnas;

    public AutomataConfigurable(){}

    public void cargarConfiguracion(String texto){
        String[] lineas = texto.split("\n");
        String[] dimensiones = lineas[0].split(",");

        filas = Integer.parseInt(dimensiones[0].trim());
        columnas = Integer.parseInt(dimensiones[1].trim());

        matriz = new int[filas][columnas];
        estado = new boolean[filas];
        alfabeto = new HashMap<>();

        String[] palabras = lineas[1].split(",");
        for(int i = 0; i < palabras.length; i++){
            palabras[i] = palabras[i].trim();
            alfabeto.put(palabras[i], i);
        }

        for(int i = 2; i < lineas.length; i++){
            int filaMatriz = i - 2;
            String[] valoresRenglon = lineas[i].split(",");

            for(int j = 0; j < columnas; j++){
                matriz[filaMatriz][j] = Integer.parseInt(valoresRenglon[j].trim());
            }

            if(Integer.parseInt(valoresRenglon[columnas].trim()) == 1){
                estado[filaMatriz] = true;
            } else{
                estado[filaMatriz] = false;
            }
        }
        configurado = true;
    }

    public void validar(String cadena) throws Exception {
        int estadoActual = 0;

        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));
            Integer columna = alfabeto.get(simbolo);

            if (columna == null){
                throw new Exception("Simbolo '" + simbolo + "' no pertenece al alfabeto");
            }
            estadoActual = matriz[estadoActual][columna];
        }

        if (!estado[estadoActual]){
            throw new Exception("Cadena invalida: El automata no termino en un estado de aceptacion");
        }
    }

    public boolean isConfigurado(){
        return configurado;
    }
}
