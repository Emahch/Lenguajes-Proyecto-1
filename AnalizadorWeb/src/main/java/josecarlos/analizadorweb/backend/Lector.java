package josecarlos.analizadorweb.backend;

import java.util.ArrayList;
import java.util.List;
import josecarlos.analizadorweb.backend.analizadores.*;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;
import josecarlos.analizadorweb.backend.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class Lector {

    private Index currentIndex;
    private String inputText;
    private TokenList tokenList;

    private StateAnalizer stateAnalizer;
    private HtmlAnalizer htmlAnalizer;
    private JsAnalizer jsAnalizer;
    private CssAnalizer cssAnalizer;

    private Analizer currentAnalizer;
    private Language currentLanguage;

    public Lector(String inputText) {
        this.inputText = inputText;
        this.currentIndex = new Index();
        this.tokenList = new TokenList();
        this.stateAnalizer = new StateAnalizer(inputText, currentIndex, tokenList);
        this.htmlAnalizer = new HtmlAnalizer(inputText, currentIndex, tokenList);
        this.jsAnalizer = new JsAnalizer(inputText, currentIndex, tokenList);
        this.cssAnalizer = new CssAnalizer(inputText, currentIndex, tokenList);
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
            List<Token> tokens;
            char currentChar = charActual();

            if (currentChar == '>') {
                tokens = stateAnalizer.analize();
                if (tokens != null) {
                    for (Token token : tokens) {
                        tokenList.addToken(token);
                    }
                    tokens.clear();
                    currentLanguage = stateAnalizer.getCurrentLanguage();
                    changeActiveAnalizer();
                }
            }
            currentChar = charActual();
            if (currentChar == '/') {
                verifyComment();
            }
            currentChar = charActual();
            if (!currentLanguage.equals(Language.NO_SPECIFIED)) {
                if (currentChar == '\n') {
                    optimizeJumpLines();  // Optimizar multiples saltos de linea
                } else if (Character.isWhitespace(currentChar)) {
                    Token token = new Token(" ", " ", TokenType.WHITE_SPACE, " ", currentLanguage);
                    tokenList.addToken(token);
                    currentIndex.next();
                } else {
                    tokens = currentAnalizer.analize();
                    if (tokens == null || tokens.isEmpty()) {
                        currentChar = charActual();
                        tokenList.addErrorToken(currentChar, currentLanguage);
                    } else {
                        for (Token token : tokens) {
                            tokenList.addToken(token);
                        }
                    }
                }
            } else {
                currentIndex.next();
            }
        }
        return this.tokenList;
    }

    private void optimizeJumpLines() {
        currentIndex.next();
        char currentChar = charActual();
        boolean doubleJump = false;

        while (currentChar == '\n' || Character.isWhitespace(currentChar)) {
            currentIndex.next();
            currentChar = charActual();
            if (currentChar == '\n') {
                doubleJump = true;
            }
        }
        if (doubleJump) {
            Token tokenDouble = new Token("\n", "\n", TokenType.JUMP_LINE, "\\n", currentLanguage);
            tokenList.addToken(tokenDouble);
        }
        Token token = new Token("\n", "\n", TokenType.JUMP_LINE, "\\n", currentLanguage);
        tokenList.addToken(token);
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

    private boolean verifyComment() {
        currentIndex.setBookmark();
        char currentChar = charActual();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentChar);
        currentIndex.next();
        currentChar = charActual();

        if (currentChar == '/') {
            currentIndex.next();
            stringBuilder.append(currentChar);
            currentChar = charActual();

            while (currentChar != '\n' && currentChar != '\0') {
                stringBuilder.append(currentChar);
                currentIndex.next();
                currentChar = charActual();
            }
            Token token = new Token(stringBuilder.toString(), "", TokenType.COMMENT, "//[a-zA-Z]|[0-9]|[.]", currentLanguage);
            tokenList.addToken(token);
            return true;
        }
        currentIndex.back();
        return false;

    }
    
    public String getHtml(){
        return this.tokenList.generateHtml();
    }
    
    public String getTokenReport(){
        return this.tokenList.generateReport();
    }
    
    public String getErrorReport(){
        return this.tokenList.generateErrorReport();
    }
    
    public String getOptimizationReport(){
        return this.tokenList.generateOptimizationReport();
    }
}
