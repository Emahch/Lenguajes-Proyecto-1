package josecarlos.analizadorweb.backend.html.tokens;

/**
 *
 * @author emahch
 */
public enum TokenType {
    STATE("Estado"),
    IGNORE(""),
    TAG("Etiqueta"),
    TAG_LINE("Etiqueta de una sola linea"),
    RESERVED_HTML("Palabra reservada HTML"),
    DATA_STRING("Texto atributo"),
    DATA_TEXT("Texto HTML"),
    Etiqueta_de_Apertura("<"),
    Etiqueta_de_Cierre("<"),
    Cierre_Etiqueta("/");
    
    
    private final String tokenName;
    
    private TokenType(String tokenName){
        this.tokenName = tokenName;
    }

    public String getTokenName() {
        return tokenName;
    }
    
}
