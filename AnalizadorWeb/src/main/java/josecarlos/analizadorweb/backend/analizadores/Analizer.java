package josecarlos.analizadorweb.backend.analizadores;

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

    public abstract void analize();

}
