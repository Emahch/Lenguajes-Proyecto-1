package josecarlos.analizadorweb.backend.analizadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;
import josecarlos.analizadorweb.backend.js.ReservedJs;
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
        } else if (Character.isDigit(currentChar)) {
            tokens.add(verifyNumber());
        } else if (isString(currentChar)) {
            tokens.add(verifyString());
        } else if (isOthers(currentChar)) {
            tokens.add(addOthers());
        } else if (Character.isLetter(currentChar)) {
            tokens.add(verifyBooleanOrIdOrRW());
        }
        return tokens;
    }

    private Token verifyBooleanOrIdOrRW() {
        StringBuilder text = new StringBuilder();
        char currentChar = charActual();
        text.append(currentChar);
        index.next();
        currentChar = charActual();

        while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            text.append(currentChar);
            index.next();
            currentChar = charActual();
        }

        if ("false".equals(text.toString()) || "true".equals(text.toString())) {
            return new Token(text.toString(), text.toString(),
                    TokenType.DATO_BOOLEANO, text.toString(), Language.js);
        } else if ("console".equals(text.toString()) && currentChar == '.') {
            StringBuilder stringRW = new StringBuilder();
            index.setBookmark();
            stringRW.append(currentChar);
            index.next();
            currentChar = charActual();
            while (Character.isLetter(currentChar)) {
                stringRW.append(currentChar);
                index.next();
                currentChar = charActual();
            }
            if (stringRW.toString().equals(".log")) {
                text.append(stringRW);
            } else {
                index.back();
            }
        }

        Optional<ReservedJs> posibleRW = ReservedJs.getValue(text.toString());
        if (posibleRW.isPresent()) {
            return new Token(text.toString(), text.toString(), TokenType.RESERVED_JS,
                    posibleRW.get().getTraduction(), Language.js);
        }

        return new Token(text.toString(), text.toString(), TokenType.IDENTIFIER,
                "[a-zA-Z]([a-zA-Z] | [0-9] | [ _ ])*", Language.js);
    }

    private Token addOthers() {
        String character = String.valueOf(charActual());
        index.next();
        return new Token(character, character, TokenType.OTHERS_JS, character, Language.js);
    }

    private Token verifyString() {
        char stringChar = charActual();
        String string = getStringChar(stringChar);
        return new Token(string, string, TokenType.STRING_JS, "\"[a-zA-Z]|[0-9]|[.]\"", Language.js);
    }

    private Token verifyNumber() {
        char currentChar = charActual();
        StringBuilder sb = new StringBuilder();

        while (Character.isDigit(currentChar)) {
            sb.append(currentChar);
            index.next();
            currentChar = charActual();
        }

        if (currentChar != '.') {
            return new Token(sb.toString(), sb.toString(), TokenType.INTEGER, "[0-9]+", Language.js);
        }

        sb.append(currentChar);
        index.next();
        currentChar = charActual();

        while (Character.isDigit(currentChar)) {
            sb.append(currentChar);
            index.next();
            currentChar = charActual();
        }

        return new Token(sb.toString(), sb.toString(), TokenType.DECIMAL, "[0-9]+ . [0-9]+", Language.js);
    }

    private Token verifyArithmeticOrIncrement() {
        char currentChar = charActual();
        index.next();
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
        index.next();

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
            case '=' -> {
                if (nextCharIsEquals()) {
                    return new Token("==", "==", TokenType.OPERATOR_RE,
                            "==", Language.js);
                }
                return new Token("=", "=", TokenType.OTHERS_JS,
                        "=", Language.js);
            }
            default -> {
            }
        }

        Optional<String> repeated = verifyIsRepeated(currentChar);
        if (repeated.isPresent()) {
            if (repeated.get().equals("||") || repeated.get().equals("&&")) {
                return new Token(repeated.get(), repeated.get(), TokenType.OPERATOR_LO,
                        repeated.get(), Language.js);
            }
        }
        return null;
    }

    private boolean nextCharIsEquals() {
        char currentChar = charActual();
        return currentChar == '=';
    }

    private Optional<String> verifyIsRepeated(char currentChar1) {
        StringBuilder sb = new StringBuilder();
        char currentChar2 = charActual();
        if (currentChar1 == currentChar2) {
            index.next();
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

    private boolean isString(char character) {
        return character == '"' || character == '\'' || character == '`';
    }

    private boolean isOthers(char character) {
        return character == '(' || character == ')' || character == '{' || character == '}' || character == '[' || character == ']'
                || character == ';' || character == ':' || character == ',' || character == '.';
    }
}
