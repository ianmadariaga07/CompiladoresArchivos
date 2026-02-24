package fes.aragon.modelo;

import java.util.Scanner;

public class IdentificadorV3 {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa la cadena a evaluar: ");
        String cadena = scanner.nextLine();

        int estado = 0;
        int entrada = 0;
        int fincadena = cadena.length();
        int i = 0;
        int[][] tabla = {
                {2,1,0},
                {1,1,0},
                {2,2,1}
        };

        do {
            char simbolo = cadena.charAt(i);
            if(Character.isLetter(simbolo)){
                entrada = 0;
            } else if(Character.isDigit(simbolo)){
                estado = 1;
            } else if(i == fincadena){
                entrada = 2;
            } else {
                throw new Exception("Identificador vacio");
            } i++;

            estado = tabla[estado][entrada];
            if(estado == tabla[0][2] || estado == tabla[1][2]){
                throw new Exception("Identificador vacio");
            }
        }while (estado != tabla[2][2]);

        if(estado == tabla[2][2]){
            System.out.println("Identificador valido");
        }

        scanner.close();
    }
}