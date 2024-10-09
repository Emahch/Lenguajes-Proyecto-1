package josecarlos.analizadorweb.backend.analizadores.utilities;

import java.util.ArrayList;
import java.util.List;
import josecarlos.analizadorweb.backend.tokens.TokenType;
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

    private List<Token> tokensHtml;
    private List<Token> tokensJs;
    private List<Token> tokensCss;
    
    private StringBuilder codeHtml;
    private StringBuilder codeJs;
    private StringBuilder codeCss;

    public TokenList() {
        this.tokens = new ArrayList<>();
        this.tokensHtml = new ArrayList<>();
        this.tokensJs = new ArrayList<>();
        this.tokensCss = new ArrayList<>();
        this.tokensError = new ArrayList<>();
        this.removedLines = new ArrayList<>();
        this.line = 1;
        this.column = 0;
    }

    public void addToken(Token token) {
        if (token.getType().equals(TokenType.JUMP_LINE) || token.getType().equals(TokenType.WHITE_SPACE)) {
            if (token.getType().equals(TokenType.JUMP_LINE)) {
                nextLine();
                this.column = 0;
            }
        } else {
            nextColumn();
            token.SetLocation(line, column);
            tokens.add(token);
        }
        
        if (token.getType().equals(TokenType.COMMENT)) {
            this.removedLines.add(line);
        }
        addToCorrectLanguage(token);
    }
    
    private void addToCorrectLanguage(Token token){
        switch (token.getLanguage()) {
            case html -> tokensHtml.add(token);
            case css -> tokensCss.add(token);
            case js -> tokensJs.add(token);
            default -> {}
        }
    }

    public void addErrorToken(TokenError token) {
        tokensError.add(token);
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
