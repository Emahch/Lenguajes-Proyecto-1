package josecarlos.analizadorweb.backend;

/**
 *
 * @author emahch
 */
public enum Language {
    html("HTML"),
    js("Javascript"),
    css("CSS"),
    STATE(""),
    NO_SPECIFIED("");
    
    private final String languageType;
    
    private Language(String name){
        this.languageType = name;
    }

    public String getLanguageType() {
        return languageType;
    }
}
