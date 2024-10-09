package josecarlos.analizadorweb.backend.analizadores.utilities;

/**
 *
 * @author emahch
 */
public class Index {
    private int index;
    private int bookmark;

    public Index() {
    }

    public void setBookmark() {
        this.bookmark = get();
    }

    public void back() {
        if (bookmark < 0) {
            return;
        }
        set(bookmark);
        this.bookmark = -1;
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
