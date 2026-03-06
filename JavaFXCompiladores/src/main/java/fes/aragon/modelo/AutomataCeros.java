package fes.aragon.modelo;

public class AutomataCeros {
    private String cadena;

    public AutomataCeros(String cadena) {
        this.cadena = cadena;
    }

    public void validarV1() throws Exception {
        int estado = 0;
        int contador = 0;

        while (contador < cadena.length()) {
            char simbolo = cadena.charAt(contador);

            if (simbolo != '0' && simbolo != '1') {
                throw new Exception("Error: El alfabeto solo permite 0 y 1. Simbolo invalido:  " + simbolo);
            }

            switch (estado) {
                case 0:
                    if (simbolo == '0') estado = 1;
                    else throw new Exception("Error: La cadena debe empezar con 00");
                    break;
                case 1:
                    if (simbolo == '0') estado = 2;
                    else throw new Exception("Error: La cadena debe empezar con 00");
                    break;

                case 2: //aceptar
                    if (simbolo == '0') estado = 2;
                    else if (simbolo == '1') estado = 3;
                    break;
                case 3:
                    if (simbolo == '0') estado = 4;
                    else if (simbolo == '1') estado = 3;
                    break;

                case 4:
                    if (simbolo == '0') estado = 2;
                    else if (simbolo == '1') estado = 3;
                    break;
            }
            contador++;
        }
        if (estado != 2) {
            throw new Exception("Error: La cadena no termina con 00");
        }
    }

    public void  validarV3() throws Exception {
        int estado = 0;
        int contador = 0;
        int entrada = 0;
        int[][] tabla = {
                {1, 5, 0}, //q0
                {2, 5, 0}, //q1
                {2, 3,-1}, //q2 aceptacion
                {4, 3, 0}, //q3
                {2, 3, 0}, //q4
                {5, 5, 0}  //qm muerto
        };

        do {
            if(contador == cadena.length()){
                entrada = 2; //fin de cadena
            } else {
                char simbolo = cadena.charAt(contador);
                if (simbolo == '0') {
                    entrada = 0;
                } else if (simbolo == '1') {
                    entrada = 1;
                } else {
                    throw new Exception("Error: El alfabeto solo permite 0 y 1. Simbolo invalido:  " + simbolo);
                }
            } contador++;
            estado = tabla[estado][entrada];

            if (estado == 5) {
                throw new Exception("Error, la cadena no empieza con 00");
            }

            if (entrada == 2 && estado == 0) {
                throw new Exception("Error: La cadena no termina con '00'.");
            }
        } while (estado != tabla[2][2]);
    }
}
