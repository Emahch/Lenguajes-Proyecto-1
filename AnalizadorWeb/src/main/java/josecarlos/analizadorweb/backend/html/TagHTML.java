package josecarlos.analizadorweb.backend.html;

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
    
    private final String traduction;
    
    private TagHTML(String value){
        this.traduction = value;
    }

    public String getTraduction() {
        return traduction;
    }
}
