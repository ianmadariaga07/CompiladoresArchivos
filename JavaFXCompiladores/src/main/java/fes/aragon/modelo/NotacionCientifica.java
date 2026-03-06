package fes.aragon.modelo;

public class NotacionCientifica {
    private String cadena;

    public NotacionCientifica(String cadena) {
        this.cadena = cadena;
    }

    public void validar() throws Exception{
        int estado = 0;
        int contador = 0;
        int entrada = 0;
        int[][] tabla = {
                {1, 7, 7, 7, 0}, //q0
                {1, 2, 4, 7, 0}, //q1
                {3, 7, 7, 7, 0}, //q2
                {3, 7, 4, 7,-1}, //q3 aceptacion
                {6, 7, 7, 5, 0}, //q4
                {6, 7, 7, 7, 0}, //q5
                {6, 7, 7, 7,-1}, //q6 aceptacion
                {7, 7, 7, 7, 0}  //q7 muerto
        };

        do{
            if(contador == cadena.length()) {
                entrada = 4; //fin de cadena
            }else{
                char simbolo = cadena.charAt(contador);

                if (Character.isDigit(simbolo)) {
                    entrada = 0;
                } else if (simbolo == '.') {
                    entrada = 1;
                } else if (simbolo == 'e' || simbolo == 'E') {
                    entrada = 2;
                } else if (simbolo == '+' || simbolo == '-') {
                    entrada = 3;
                } else {
                    throw new Exception("Error: Símbolo ajeno al alfabeto: " + simbolo);
                }
            }

            estado = tabla[estado][entrada];

            if(estado == 7 && entrada != 4){
                throw new Exception("Error: La cadena rompio la estructura y cayo en el estado pozo");
            }

            if(estado != -1 && entrada == 4){
                throw new Exception("Error: La cadena quedo incompleta");
            }
            contador++;

        } while (estado != -1);
    }
}
