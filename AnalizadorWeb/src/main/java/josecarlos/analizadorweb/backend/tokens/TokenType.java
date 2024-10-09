package josecarlos.analizadorweb.backend.tokens;

/**
 *
 * @author emahch
 */
public enum TokenType {
    STATE("Estado"),
    JUMP_LINE("Salto linea"),
    WHITE_SPACE("Espacio en blanco"),
    TAG("Etiqueta"),
    TAG_LINE("Etiqueta de una sola linea"),
    RESERVED_HTML("Palabra reservada HTML"),
    DATA_STRING_HTML("Texto atributo"),
    DATA_TEXT_HTML("Texto HTML"),
    Etiqueta_de_Apertura("Etiqueta de apertura"),
    Etiqueta_de_Cierre("Etiqueta de cierre"),
    Cierre_Etiqueta("Fin de etiqueta"),
    COMMENT("Comentario"),
    SELECTOR_U("Selector universal"),
    SELECTOR_ID("Selector de Id"),
    SELECTOR_TAG("Selector etiqueta"),
    SELECTOR_IDENTIFIER("Identificador de selector"),
    SELECTOR_CLASS("Selector de clase"),
    COMBINATOR("Combinadores"),
    RULE("Regla css"),
    OTHERS_CSS("Otros css"),
    STRING_CSS("Cadena de caracteres css"),
    COLOR("Color"),
    IDENTIFIER("Identificador"),
    INTEGER("Numero entero"),
    DECIMAL("Numero decimal"),
    STRING_JS("Cadena de caracteres Js"),
    DATO_BOOLEANO("Dato booleano"),
    OPERATOR_AR("Operador Aritmetico"),
    OPERATOR_RE("Operador Relacional"),
    OPERATOR_LO("Operador Lógico"),
    OPERATOR_IN("Operador Incremental"),
    RESERVED_JS("Palabra reservada Js"),
    SIMBOLO("Simbolo");
    
    
    private final String tokenName;
    
    private TokenType(String tokenName){
        this.tokenName = tokenName;
    }

    public String getTokenName() {
        return tokenName;
    }
    
}
