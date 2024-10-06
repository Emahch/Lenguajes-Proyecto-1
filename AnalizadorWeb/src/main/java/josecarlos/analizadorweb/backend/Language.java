package josecarlos.analizadorweb.backend;

/**
 *
 * @author emahch
 */
public enum Language {
    html("HTML"),
    js("Javascript"),
    css("CSS"),
    NO_ESPECIFIED("no_especificado");
    
    private final String languageType;
    public static final String TOKEN_TYPE = "Estado";
    
    private Language(String name){
        this.languageType = name;
    }

    public String getLanguageType() {
        return languageType;
    }
}
