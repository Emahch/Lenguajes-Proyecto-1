package josecarlos.analizadorweb.archivos;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author emahch
 */
public class ExportFile {

    private File archivoSeleccionado;

    public ExportFile() {
    }

    /**
     * Metodo que permite seleccionar la ubicación y el nombre de donde se desea guardar el archivo
     * 
     * @return 
     */
    public boolean selectPath(String pathName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(new File(pathName));

        int opcion = fileChooser.showSaveDialog(new JFrame());

        if (opcion == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
        }
        return opcion == JFileChooser.APPROVE_OPTION;
    }
    
    public void writeFile(String texto){
        try (FileWriter fw = new FileWriter(archivoSeleccionado);
                BufferedWriter bf = new BufferedWriter(fw)) {
            bf.write(texto);
            bf.flush();
        } catch (Exception e) {
        }
    }

}
