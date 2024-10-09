package josecarlos.analizadorweb.backend.analizadores;

import java.util.ArrayList;
import java.util.List;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;
import josecarlos.analizadorweb.backend.html.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class CssAnalizer extends Analizer{

    public CssAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText,currentIndex, tokenList);
    }

    @Override
    public List<Token> analize() {
        char currentChar = charActual();
        
        if (currentChar == '*') {
            return addSelectorUniversal();
        }
        return null;
    }
    
    private List<Token> addSelectorUniversal(){
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        if (currentChar == '*') {
            Token tokenSelector = new Token("*", "*", TokenType.SELECTOR_U, "*", Language.css);
            tokens.add(tokenSelector);
            return tokens;
        }
        return null;
    }
    
    private List<Token> verifySelectorTag(){
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        if (currentChar == '*') {
            Token tokenSelector = new Token("*", "*", TokenType.SELECTOR_U, "*", Language.css);
            tokens.add(tokenSelector);
            return tokens;
        }
        return null;
    }
}
