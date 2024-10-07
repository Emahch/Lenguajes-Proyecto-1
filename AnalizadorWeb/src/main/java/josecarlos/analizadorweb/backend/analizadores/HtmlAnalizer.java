package josecarlos.analizadorweb.backend.analizadores;

import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.html.tokens.TagHTML;
import josecarlos.analizadorweb.backend.html.tokens.TagLineHTML;
import josecarlos.analizadorweb.backend.html.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class HtmlAnalizer extends Analizer{
    
    private boolean inTag;
    
    public HtmlAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText,currentIndex, tokenList);
        this.inTag = false;
    }

    @Override
    public void analize() {
        char currentChar = charActual();
        
        if (currentChar == '<') {
            
        } else if (currentChar == '>') {
            
        } else if (currentChar == '/') {
            
        } else if (currentChar == '=') {
            
        } else if (currentChar == '"') {
            
        } else if (Character.isLetter(currentChar)) {
            
        }
    }
    
    private void verifyTag(){
        if (inTag) {
            return;
        }
        currentIndex.setBookmark();
        char currentChar = charActual();
        String identifierTag = getText();
        
        try {
            TagHTML tag = TagHTML.valueOf(identifierTag);
        } catch (Exception e) {
            try {
                TagLineHTML tag = TagLineHTML.valueOf(identifierTag);
            } catch (Exception ex) {
                currentIndex.back();
                return;
            }
        }
        
        while (Character.isLetterOrDigit(currentChar)) {
            currentIndex.next();
            currentChar = charActual();
        }
        
        if (true) {
            
        }
        
        
        Token token = new Token(String.valueOf(currentChar),"<", TokenType.Etiqueta_de_Apertura, "<", Language.html);
    }
    
    private String getText(){
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();
        
        while (Character.isLetterOrDigit(currentChar)) {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
        }
        
        return stringBuilder.toString();
    }
}
