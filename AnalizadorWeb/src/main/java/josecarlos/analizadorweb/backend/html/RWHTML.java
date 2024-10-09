package josecarlos.analizadorweb.backend.html;

import java.util.Optional;

/**
 *
 * @author emahch
 */
public enum RWHTML {
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
    
    private RWHTML(String reservedWord){
        this.reservedWord = reservedWord;
    }

    public String getReservedWord() {
        return reservedWord;
    }
    
    public static Optional<RWHTML> getValue(String text){
            RWHTML[] valores = RWHTML.values();
            for (RWHTML valor : valores) {
                if (text != null && valor.getReservedWord().equals(text)) {
                    return Optional.of(valor);
                }
            }
            return Optional.empty();
    }
}
