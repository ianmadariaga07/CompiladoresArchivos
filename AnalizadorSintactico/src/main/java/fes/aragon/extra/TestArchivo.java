package fes.aragon.extra;

import fes.aragon.modelo.LexicoMike;
import fes.aragon.token.TokensMike;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class TestArchivo {
    public static void main(String[] args) {
        try{
            Reader rd=new BufferedReader(new FileReader("src/main/resources/fes.aragon/textos/FuenteMike#.txt"));
            LexicoMike lexico=new LexicoMike(rd);
            TokensMike resultado;
            do {
                resultado=lexico.yylex();
                if(resultado!=null){
                    System.out.print("("+resultado+")");
                    System.out.println(lexico.lexema+ "-> Lexema");
                }
                if(TokensMike.ERROR==resultado){
                    System.out.println("Línea "+lexico.getYyline() +
                            " Columna "+lexico.getYycolumn());
                }

            }while(TokensMike.ERROR!=resultado);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
