package josecarlos.analizadorweb.backend.analizadores;

import java.util.List;
import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public abstract class Analizer {
    
    protected final String inputText;
    protected Index index;
    protected TokenList tokenList;

    public Analizer(String inputText, Index currentIndex, TokenList tokenList) {
        this.inputText = inputText;
        this.index = currentIndex;
        this.tokenList = tokenList;
    }

    protected char charActual() {
        if (index.get() >= inputText.length()) {
            return '\0';  // Fin de la cadena
        }
        return inputText.charAt(index.get());
    }

    public abstract List<Token> analize();
    
    protected String getText() {
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();

        while (Character.isLetterOrDigit(currentChar)) {
            stringBuilder.append(currentChar);
            index.next();
            currentChar = charActual();
        }

        return stringBuilder.toString();
    }

    protected String getStringChar(char caracter) {
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();
        stringBuilder.append(currentChar);
        index.next();
        currentChar = charActual();

        while (currentChar != caracter && currentChar != '\0') {
            stringBuilder.append(currentChar);
            index.next();
            currentChar = charActual();
        }
        if (currentChar == caracter) {
            stringBuilder.append(currentChar);
            index.next();
        }

        return stringBuilder.toString();
    }

    protected String getDataText() {
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();

        while (currentChar != '<' && currentChar != '\0' && currentChar != '/') {
            stringBuilder.append(currentChar);
            index.next();
            currentChar = charActual();
            if (currentChar == '/') {
                index.setBookmark();
                index.next();
                currentChar = charActual();
            }
        }
        
        if (currentChar == '/') {
            index.back();
        }

        return stringBuilder.toString();
    }
}
