package fes.aragon.modelo;

public class TokenModelo {
    private String token;
    private String lexema;
    private int linea;
    private int columna;

    public TokenModelo(String token, String lexema, int linea, int columna){
        this.token = token;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public String getToken(){return token; }
    public String getLexema(){return lexema; }
    public int getLinea(){return linea; }
    public int getColumna(){return columna; }
}