package josecarlos.analizadorweb.backend.js;

import java.util.Optional;

/**
 *
 * @author emahch
 */
public enum ReservedJs {
    FN("function"),
    CST("const"),
    LET("let"),
    DOC("document"),
    EVT("event"),
    AL("alert"),
    FOR("for"),
    WHL("while"),
    IF("if"),
    ELS("else"),
    RET("return"),
    CL("console.log"),
    NUL("null");

    private final String traduction;

    private ReservedJs(String traduction) {
        this.traduction = traduction;
    }

    public String getTraduction() {
        return traduction;
    }
    
    public static Optional<ReservedJs> getValue(String text) {
        ReservedJs[] values = ReservedJs.values();
        for (ReservedJs value : values) {
            if (text != null && value.getTraduction().equals(text)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

}
