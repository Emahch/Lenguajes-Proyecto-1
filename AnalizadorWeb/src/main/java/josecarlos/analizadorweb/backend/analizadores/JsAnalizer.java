package josecarlos.analizadorweb.backend.analizadores;

import josecarlos.analizadorweb.backend.analizadores.Analizer;
import josecarlos.analizadorweb.backend.analizadores.utilities.*;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;

/**
 *
 * @author emahch
 */
public class JsAnalizer extends Analizer{

    public JsAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText,currentIndex, tokenList);
    }

    @Override
    public boolean analize() {
        return false;
    }
    
}
