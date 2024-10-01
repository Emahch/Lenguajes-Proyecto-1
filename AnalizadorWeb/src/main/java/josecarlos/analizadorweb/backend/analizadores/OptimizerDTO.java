package josecarlos.analizadorweb.backend.analizadores;

import java.util.List;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class OptimizerDTO {
    private List<Token> removedTokens;
    private String optimizedText;

    public OptimizerDTO() {
    }

    public List<Token> getRemovedTokens() {
        return removedTokens;
    }

    public void setRemovedTokens(List<Token> removedTokens) {
        this.removedTokens = removedTokens;
    }

    public String getOptimizedText() {
        return optimizedText;
    }

    public void setOptimizedText(String optimizedText) {
        this.optimizedText = optimizedText;
    }
}
