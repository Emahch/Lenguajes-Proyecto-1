package josecarlos.analizadorweb.backend;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import josecarlos.analizadorweb.frontend.FramePrincipal;

/**
 *
 * @author emahch
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        FramePrincipal frame = new FramePrincipal();
        frame.setVisible(true);
    }
}
