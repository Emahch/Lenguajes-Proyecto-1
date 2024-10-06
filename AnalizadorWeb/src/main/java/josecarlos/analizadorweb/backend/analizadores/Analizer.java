package josecarlos.analizadorweb.backend.analizadores;

import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public abstract class Analizer {

    protected String inputText;
    protected int currentIndex;
    protected int currentLine;
    protected int currentColumn;

    public Analizer(String inputText) {
        this.inputText = inputText;
    }

    protected char charActual() {
        if (currentIndex >= inputText.length()) {
            return '\0';  // Fin de la cadena
        }
        return inputText.charAt(currentIndex);
    }

    protected void nextLine() {
        currentIndex++;
        currentLine++;
        currentColumn = 0;
    }

    protected void next() {
        currentIndex++;
        currentColumn++;
    }

    public abstract Optional<Token> analize(int currentIndex);

    public int getCurrentIndex() {
        return currentIndex;
    }

}
