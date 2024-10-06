package josecarlos.analizadorweb.backend.analizadores;

import java.util.Optional;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
class JsAnalizer extends Analizer{

    public JsAnalizer(String inputText) {
        super(inputText);
    }

    @Override
    public Optional<Token> analize(int currentIndex) {
    }
    
}
