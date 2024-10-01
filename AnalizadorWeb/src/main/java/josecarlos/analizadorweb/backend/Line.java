package josecarlos.analizadorweb.backend;

/**
 *
 * @author emahch
 */
public class Line {
    private StringBuilder text;
    private final int lineNumber;

    public Line(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public StringBuilder getText() {
        return text;
    }

    public void setText(StringBuilder text) {
        this.text = text;
    }
    
}
