package josecarlos.analizadorweb.backend.html.tokens;

/**
 *
 * @author emahch
 */
public enum TagHTML {
    principal("main"),
    encabezado("header"),
    navegacion("nav"),
    apartado("aside"),
    listaordenada("ul"),
    listadesordenada("ol"),
    itemlista("li"),
    anclaje("a"),
    contenedor("div"),
    seccion("section"),
    articulo("article"),
    parrafo("p"),
    span("span"),
    formulario("form"),
    label("label"),
    boton("button"),
    piepagina("footer");
    
    private final String value;
    public static final String TOKEN_TYPE = "Etiqueta";
    
    private TagHTML(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
