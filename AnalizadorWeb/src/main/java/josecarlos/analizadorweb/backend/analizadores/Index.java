package josecarlos.analizadorweb.backend.analizadores;

/**
 *
 * @author emahch
 */
public class Index {
    private int index;

    public Index() {
    }

    public int get() {
        return index;
    }

    public void set(int index) {
        this.index = index;
    }
    
    public void next(){
        this.index++;
    }
}
