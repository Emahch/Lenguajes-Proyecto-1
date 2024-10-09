package josecarlos.analizadorweb.backend.html;

/**
 *
 * @author emahch
 */
public enum TagLineHTML {
    titulo1("h1"),
    titulo2("h2"),
    titulo3("h3"),
    titulo4("h4"),
    titulo5("h5"),
    titulo6("h6"),
    entrada("input"),
    area("textarea");
    
    private final String traduction;
    
    private TagLineHTML(String traduccion){
        this.traduction = traduccion;
    }

    public String getTraduction() {
        return traduction;
    }
    
}
