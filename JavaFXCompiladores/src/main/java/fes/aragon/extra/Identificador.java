package fes.aragon.extra;

import java.util.Scanner;

public class Identificador {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa la cadena a evaluar: ");
        String cadena = scanner.nextLine();

        int estado = 1;

        //While no fin-cadena do y leer siguiente simbolo
        for (int i = 0; i < cadena.length(); i++){
            char simbolo = cadena.charAt(i);

            switch (estado) {
                case 1:
                    if (Character.isLetter(simbolo)){
                        estado = 3;
                    }
                    else if (Character.isDigit(simbolo)){
                        estado = 2;
                    }
                    else{
                        estado = -1;
                    }
                    break;
                case 2:
                    estado = -1;
                    break;

                case 3:
                    if(Character.isLetter(simbolo)){
                        estado = 3;
                    } else if(Character.isDigit(simbolo)){
                        estado = 3;
                    } else {
                        estado = -1;
                    }
                    break;
                //pozo
                case -1:
                    break;
            }
        }

        //if Estado != 3 then rutina error
        if (estado == 3) {
            System.out.println("El identificador es valido");
        } else {
            System.out.println("Error: El identificador es invalido");
        }
        scanner.close();
    }
}