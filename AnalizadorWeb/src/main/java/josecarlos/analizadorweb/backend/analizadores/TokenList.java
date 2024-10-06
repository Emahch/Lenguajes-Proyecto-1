package josecarlos.analizadorweb.backend.analizadores;

import java.util.ArrayList;
import java.util.List;
import josecarlos.analizadorweb.backend.html.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;
import josecarlos.analizadorweb.backend.tokens.TokenError;

/**
 *
 * @author emahch
 */
public class TokenList {

    private int line;
    private int column;

    private List<Token> tokens;
    private List<TokenError> tokensError;
    private List<Integer> removedLines;

    private StringBuilder codeHtml;
    private StringBuilder codeJs;
    private StringBuilder codeCss;

    public TokenList() {
        this.tokens = new ArrayList<>();
        this.tokensError = new ArrayList<>();
        this.removedLines = new ArrayList<>();
    }

    public void addToken(Token token) {
        if (token.isIgnored()) {

        }
    }
    
    public void addErrorToken(TokenError token){
        
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<TokenError> getTokensError() {
        return tokensError;
    }

    public List<Integer> getRemovedLines() {
        return removedLines;
    }

    public void nextLine() {
        line++;
    }

    public void nextColumn() {
        column++;
    }
}
