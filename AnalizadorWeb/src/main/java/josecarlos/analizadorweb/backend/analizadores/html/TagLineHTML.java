package josecarlos.analizadorweb.backend.analizadores.html;

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
    
    private final String traduccion;
    public static final String TOKEN_TYPE = "Etiqueta de una linea";
    
    private TagLineHTML(String traduccion){
        this.traduccion = traduccion;
    }

    public String getTraduccion() {
        return traduccion;
    }
    
}
