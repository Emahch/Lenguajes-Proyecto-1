package josecarlos.analizadorweb.backend.analizadores;

import java.util.List;
import josecarlos.analizadorweb.backend.Language;
import static josecarlos.analizadorweb.backend.Language.css;
import static josecarlos.analizadorweb.backend.Language.html;
import static josecarlos.analizadorweb.backend.Language.js;
import josecarlos.analizadorweb.backend.html.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class Lector {

    private Index currentIndex;
    private String inputText;
    private TokenList tokens;

    private StateAnalizer stateAnalizer;
    private HtmlAnalizer htmlAnalizer;
    private JsAnalizer jsAnalizer;
    private CssAnalizer cssAnalizer;

    private Analizer currentAnalizer;
    private Language currentLanguage;

    public Lector(String inputText) {
        this.inputText = inputText;
        this.tokens = new TokenList();
        this.stateAnalizer = new StateAnalizer(inputText, currentIndex, tokens);
        this.htmlAnalizer = new HtmlAnalizer(inputText, currentIndex, tokens);
        this.jsAnalizer = new JsAnalizer(inputText, currentIndex, tokens);
        this.cssAnalizer = new CssAnalizer(inputText, currentIndex, tokens);
        this.currentLanguage = Language.NO_SPECIFIED;
    }
    
    protected char charActual() {
        if (currentIndex.get() >= inputText.length()) {
            return '\0';  // Fin de la cadena
        }
        return inputText.charAt(currentIndex.get());
    }

    /**
     * Metodo encargado de analizar todo el texto y regresar una lista con todos los tokens
     *
     * @return lista de lineas
     */
    public TokenList generarTokens() {
        while (currentIndex.get() < inputText.length()) {
            char currentChar = charActual();

            if (currentChar == '>') {
                stateAnalizer.analize();
                currentLanguage = stateAnalizer.getCurrentLanguage();
                changeActiveAnalizer();
            }
            currentChar = charActual();

            if (!currentLanguage.equals(Language.NO_SPECIFIED)) {
                if (Character.isWhitespace(currentChar)) {
                    Token token = new Token(" ", " ", TokenType.WHITE_SPACE, " ", currentLanguage);
                    tokens.addToken(token);
                    currentIndex.next();
                } else if (currentChar == '\n') {
                    optimizeJumpLines();
                } else {
                    currentAnalizer.analize();
                }
            } else {
                System.out.println("No language specified");
            }
        }
        return this.tokens;
    }

    private void optimizeJumpLines() {
        char currentChar = charActual();

        while (currentChar == '\n') {
            currentIndex.next();
        }
        Token token = new Token("\n", "\n", TokenType.JUMP_LINE, "\\n", currentLanguage);
        tokens.addToken(token);
    }
    
    private void changeActiveAnalizer() {
        switch (currentLanguage) {
            case css ->
                currentAnalizer = cssAnalizer;
            case html ->
                currentAnalizer = htmlAnalizer;
            case js ->
                currentAnalizer = jsAnalizer;
            default ->
                currentAnalizer = null;
        }
    }
}
