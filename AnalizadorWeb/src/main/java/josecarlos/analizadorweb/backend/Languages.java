package josecarlos.analizadorweb.backend;

/**
 *
 * @author emahch
 */
public enum Languages {
    html("HTML"),
    js("Javascript"),
    css("CSS");
    
    private final String name;
    public static final String TOKEN_TYPE = "Estado";
    
    private Languages(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
