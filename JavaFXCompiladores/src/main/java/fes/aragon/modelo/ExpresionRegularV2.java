package fes.aragon.modelo;

public class ExpresionRegularV2 {
    private int indice = 0;
    private String cadena = "";
    private final int error = -1;
    private final int aceptado = 1;

    public ExpresionRegularV2(String cadena){
        this.cadena = cadena;
    }

    //metodo main, se llama al estado A
    public void validar() throws Exception {
        int valor = estado_A();

        if (valor != aceptado){
            throw new Exception("Error: Cadena Invalida");
        }
    }

    private char siguienteCaracter(){
        char caracter = ' ';
        if (indice < cadena.length()){
            caracter = cadena.charAt(indice);
            indice++;
        }
        return caracter;
    }

    private int estado_A(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_B();
            case '1': return estado_C();
            default: return error;
        }
    }

    private int estado_B(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_D();
            case '1': return estado_E();
            default: return error;
        }
    }

    private int estado_C(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_F();
            case '1': return estado_G();
            default: return error;
        }
    }

    private int estado_D(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_D();
            case '1': return estado_H();
            default: return error;
        }
    }

    //estado final
    private int estado_E(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_F();
            case '1': return estado_I();
            case ' ': return aceptado;
            default: return error;
        }
    }

    private int estado_F(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_I();
            case '1': return estado_J();
            default: return error;
        }
    }

    private int estado_G(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_F();
            case '1': return estado_G();
            default: return error;
        }
    }

    private int estado_H(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_F();
            case '1': return estado_I();
            default: return error;
        }
    }

    //estado error
    private int estado_I(){
        return error;
    }

    //estado final
    private int estado_J(){
        char c = siguienteCaracter();
        switch (c){
            case '0': return estado_I();
            case '1': return estado_I();
            case ' ': return aceptado;
            default: return error;
        }
    }
}