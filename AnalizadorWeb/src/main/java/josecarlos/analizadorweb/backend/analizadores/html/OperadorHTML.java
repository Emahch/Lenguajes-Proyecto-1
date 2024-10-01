package josecarlos.analizadorweb.backend.analizadores.html;

/**
 *
 * @author emahch
 */
public enum OperadorHTML {
    Etiqueta_de_Apertura("<"),
    Etiqueta_de_Cierre("<"),
    Cierre_Etiqueta("/");
    
    private final String operator;

    private OperadorHTML(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
    
    
}
