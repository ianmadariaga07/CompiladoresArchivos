package fes.aragon.extra;

public class ExpresionRegularApoyo {
    private String cadena;
    private int contador;
    private boolean aceptado;

    public ExpresionRegularApoyo(String cadena) {
        this.cadena = cadena;
        this.contador = 0;
        this.aceptado = false;
    }

    //metodo principal. Manda a llamar al estado A
    public void validar() throws Exception {
        estadoA();
        if (!aceptado) {
            throw new Exception("Error: La cadena no termino en un estado valido");
        }
    }

    private void estadoA() throws Exception {
        if (contador == cadena.length()) return;
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoB();
        else if (simbolo == '1') estadoC();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    private void estadoB() throws Exception {
        if (contador == cadena.length()) return;
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoD();
        else if (simbolo == '1') estadoE();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    private void estadoC() throws Exception {
        if (contador == cadena.length()) return;
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoF();
        else if (simbolo == '1') estadoG();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    private void estadoD() throws Exception {
        if (contador == cadena.length()) return;
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoD();
        else if (simbolo == '1') estadoH();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    //estado final
    private void estadoE() throws Exception {
        if (contador == cadena.length()) {
            aceptado = true;
            return;
        }
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoF();
        else if (simbolo == '1') estadoI();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    private void estadoF() throws Exception {
        if (contador == cadena.length()) return;
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoI();
        else if (simbolo == '1') estadoJ();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    private void estadoG() throws Exception {
        if (contador == cadena.length()) return;
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoF();
        else if (simbolo == '1') estadoG();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    private void estadoH() throws Exception {
        if (contador == cadena.length()) return;
        char simbolo = cadena.charAt(contador++);

        if (simbolo == '0') estadoF();
        else if (simbolo == '1') estadoI();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }

    //estado error
    private void estadoI() throws Exception {
        if (contador == cadena.length()) return;
        contador++;
        estadoI();
    }

    //estado final
    private void estadoJ() throws Exception {
        if (contador == cadena.length()) {
            aceptado = true;
            return;
        }

        char simbolo = cadena.charAt(contador++);
        if (simbolo == '0') estadoI();
        else if (simbolo == '1') estadoI();
        else throw new Exception("Símbolo inválido: " + simbolo);
    }
}

