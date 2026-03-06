package fes.aragon.modelo;

public class IdentificadorVariables {
    private String cadena;

    public IdentificadorVariables(String cadena) {
        this.cadena = cadena;
    }

    public void validarV1() throws Exception{
        int estado = 0;
        int contador = 0;

        while (contador < cadena.length()) {
            char simbolo = cadena.charAt(contador);
            estado = switch (estado) {
                case 0 -> (Character.isLetter(simbolo)) ? 2 : (Character.isDigit(simbolo)) ? 1 : -1;
                case 1 -> -1;
                case 2 -> (Character.isLetter(simbolo) || Character.isDigit(simbolo)) ? 2 : -1;
                case -1 -> throw new Exception("Estado no valido");
                default -> estado;
            };
            contador++;
        }
        if (estado != 2) {
            throw new Exception("Error: La cadena no terminó en un estado de aceptación");
        }
    }

    public void validarV3() throws Exception {
        int estado = 0;
        int contador = 0;
        int entrada = 0;
        //boolean valido = true;
        int[][] tabla = {
                {2,1,0},
                {1,1,0},
                {2,2,-1}
        };

        do {
            if(contador == cadena.length()){
                entrada = 2;
            } else {
                char simbolo = cadena.charAt(contador);
                if (Character.isDigit(simbolo)) {
                    entrada = 1;
                } else if (Character.isLetter(simbolo)) {
                    entrada = 0;
                } else {
                    throw new Exception("La letra no puede estar vacia");
                }
            } contador++;
            estado = tabla[estado][entrada];

            if (estado == tabla[0][2] || tabla[1][2] == estado) {
                throw new Exception("Error, identificador invalido");
            }
        } while (estado != tabla[2][2]);
    }
}
