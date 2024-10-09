package josecarlos.analizadorweb.backend.analizadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;
import josecarlos.analizadorweb.backend.tokens.Token;
import josecarlos.analizadorweb.backend.tokens.TokenType;

/**
 *
 * @author emahch
 */
public class JsAnalizer extends Analizer {

    public JsAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText, currentIndex, tokenList);
    }

    @Override
    public List<Token> analize() {
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();

        if (isArithmeticOrIncremental(currentChar)) {
            tokens.add(verifyArithmeticOrIncrement());
        } else if (isRelationalOrLogic(currentChar)) {
            tokens.add(verifyRelationalOrLogic());
        }
    }

    private Token verifyArithmeticOrIncrement() {
        char currentChar = charActual();
        currentIndex.next();
        switch (currentChar) {
            case '*' -> {
                return new Token("*", "*", TokenType.OPERATOR_AR, "*", Language.js);
            }
            case '/' -> {
                return new Token("/", "/", TokenType.OPERATOR_AR, "/", Language.js);
            }
            default -> {
            }
        }

        Optional<String> repeated = verifyIsRepeated(currentChar);
        if (repeated.isPresent()) {
            return new Token(repeated.get(), repeated.get(), TokenType.OPERATOR_IN,
                    repeated.get(), Language.js);
        }

        return new Token(String.valueOf(currentChar), String.valueOf(currentChar), TokenType.OPERATOR_AR,
                String.valueOf(currentChar), Language.js);
    }
    
    private Token verifyRelationalOrLogic() {
        char currentChar = charActual();
        currentIndex.next();
        
        switch (currentChar) {
            case '>' -> {
                if (nextCharIsEquals()) {
                return new Token(">=", ">=", TokenType.OPERATOR_RE,
                        ">=", Language.js);
                }
                return new Token(">", ">", TokenType.OPERATOR_RE,
                        ">", Language.js);
            }
            case '<' -> {
                if (nextCharIsEquals()) {
                return new Token("<=", "<=", TokenType.OPERATOR_RE,
                        "<=", Language.js);
                }
                return new Token("<", "<", TokenType.OPERATOR_RE,
                        "<", Language.js);
            }
            case '!' -> {
                if (nextCharIsEquals()) {
                return new Token("!=", "!=", TokenType.OPERATOR_RE,
                        "!=", Language.js);
                }
                return new Token("!", "!", TokenType.OPERATOR_LO,
                        "!", Language.js);
            }
            default -> {}
        }
        
        Optional<String> repeated = verifyIsRepeated(currentChar);
        if (repeated.isPresent()) {
            if (repeated.get().equals("||") || repeated.get().equals("&&")) {
                return new Token(repeated.get(), repeated.get(), TokenType.OPERATOR_LO,
                        repeated.get(), Language.js);
            } else if (repeated.get().equals("==")) {
                return new Token(repeated.get(), repeated.get(), TokenType.OPERATOR_RE,
                        repeated.get(), Language.js);
            }
        }
        return null;
    }
    
    private boolean nextCharIsEquals(){
        char currentChar = charActual();
        return currentChar == '=';
    }

    private Optional<String> verifyIsRepeated(char currentChar1) {
        StringBuilder sb = new StringBuilder();
        char currentChar2 = charActual();
        if (currentChar1 == currentChar2) {
            currentIndex.next();
            sb.append(currentChar1);
            sb.append(currentChar2);
            return Optional.of(sb.toString());
        }
        return Optional.empty();
    }

    private boolean isArithmeticOrIncremental(char character) {
        return character == '+' || character == '-' || character == '*' || character == '/';
    }

    private boolean isRelationalOrLogic(char character) {
        return character == '=' || character == '<' || character == '>' || character == '!' || character == '&' || character == '|';
    }
}
