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

    protected String inputText;
    protected Index currentIndex;
    protected TokenList tokenList;

    public Analizer(String inputText, Index currentIndex, TokenList tokenList) {
        this.inputText = inputText;
        this.currentIndex = currentIndex;
        this.tokenList = tokenList;
    }

    protected char charActual() {
        if (currentIndex.get() >= inputText.length()) {
            return '\0';  // Fin de la cadena
        }
        return inputText.charAt(currentIndex.get());
    }

    public abstract List<Token> analize();

    protected String getText() {
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();

        while (Character.isLetterOrDigit(currentChar)) {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
        }

        return stringBuilder.toString();
    }

    protected String getStringChar() {
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();
        stringBuilder.append(currentChar);
        currentIndex.next();
        currentChar = charActual();

        while (currentChar != '"' && currentChar != '\0') {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
        }
        if (currentChar == '"') {
            stringBuilder.append(currentChar);
            currentIndex.next();
        }

        return stringBuilder.toString();
    }

    protected String getDataText() {
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();

        while (currentChar != '<' && currentChar != '\0' && currentChar != '/') {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
            if (currentChar == '/') {
                currentIndex.setBookmark();
                currentIndex.next();
                currentChar = charActual();
            }
        }
        
        if (currentChar == '/') {
            currentIndex.back();
        }

        return stringBuilder.toString();
    }
}
