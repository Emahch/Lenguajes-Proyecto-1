package josecarlos.analizadorweb.backend.analizadores;

import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.html.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class HtmlAnalizer extends Analizer{
    
    private boolean inTag;
    
    public HtmlAnalizer(String inputText) {
        super(inputText);
        this.inTag = false;
    }

    @Override
    public Optional<Token> analize(int currentIndex) {
        this.currentIndex = currentIndex;
        char currentChar = charActual();
        
        if (currentChar == '<') {
            Token token = new Token(String.valueOf(currentChar),TokenType.Etiqueta_de_Apertura,"<");
            token.setTraduction("<");
            return Optional.of(token);
        } else if (currentChar == '>') {
            Token token = new Token(String.valueOf(currentChar),TokenType.Etiqueta_de_Apertura,"<");
            token.setTraduction("<");
            
        } else if (currentChar == '/') {
            
        } else if (currentChar == '=') {
            
        } else if (currentChar == '"') {
            
        } else if (Character.isLetter(currentChar)) {
            
        }
    }
    
    private Optional<Token> isOpenTag(){
        
    }
}
