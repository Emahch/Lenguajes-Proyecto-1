package josecarlos.analizadorweb.backend.analizadores;

/**
 *
 * @author emahch
 */
public class Position {
    private int line;
    private int column;

    public Position() {
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    
    public void nextLine(){
        line++;
    }
    
    public void nextColumn(){
        column++;
    }
}
