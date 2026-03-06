package fes.aragon.modelo;

public class AutomataSubcadenaAA {
    private String cadena;

    public AutomataSubcadenaAA(String cadena) {
        this.cadena = cadena;
    }

    public void validar() throws Exception {
        int estado = 0;
        int contador = 0;
        int entrada = 0;
        int[][] tabla = {
                {1, 0, 0},//q0
                {2, 0, 0},//q1
                {2, 2,-1} //q2 aceptacion
        };

        do {
            if(contador == cadena.length()){
                entrada = 2; //fin de cadena
            } else {
                char simbolo = cadena.charAt(contador);
                if (simbolo == 'a') {
                    entrada = 0;
                } else if (simbolo == 'b') {
                    entrada = 1;
                } else {
                    throw new Exception("Error: El alfabeto solo permite a y b. Simbolo invalido:  " + simbolo);
                } contador++;
            }
            estado = tabla[estado][entrada];

            if (entrada == 2 && (estado == 0 || estado == 1)) {
                throw new Exception("Error: La cadena no encuentra 'aa'");
            }
        } while (estado != tabla[2][2]);
    }
}
