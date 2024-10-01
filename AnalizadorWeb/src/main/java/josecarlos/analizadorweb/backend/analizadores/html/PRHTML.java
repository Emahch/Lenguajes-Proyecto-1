package josecarlos.analizadorweb.backend.analizadores.html;

import java.util.Optional;

/**
 *
 * @author emahch
 */
public enum PRHTML {
    CLASS("class"),
    equal("="),
    HREF("href"),
    ONCLICK("onClick"),
    ID("id"),
    STYLE("style"),
    TYPE("type"),
    PLACEHOLDER("placeholder"),
    REQUIRED("required"),
    NAME("name");
    
    private final String reservedWord;
    public static final String TOKEN_TYPE = "Palabra Reservada";
    
    private PRHTML(String reservedWord){
        this.reservedWord = reservedWord;
    }

    public String getReservedWord() {
        return reservedWord;
    }
    
    public Optional<String> getValue(String text){
            PRHTML[] valores = PRHTML.values();
            for (PRHTML valor : valores) {
                if (text != null && valor.getReservedWord().equals(text)) {
                    return Optional.of(valor.getReservedWord());
                }
            }
            return Optional.empty();
    }
}
