package josecarlos.analizadorweb.backend.analizadores;

import java.util.Optional;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
class JsAnalizer extends Analizer{

    public JsAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText,currentIndex, tokenList);
    }

    @Override
    public void analize() {
    }
    
}
