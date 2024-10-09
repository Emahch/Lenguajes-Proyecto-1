package josecarlos.analizadorweb.backend.analizadores;

import josecarlos.analizadorweb.backend.tokens.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;
import josecarlos.analizadorweb.backend.css.OtherCss;
import josecarlos.analizadorweb.backend.css.RulesCss;
import josecarlos.analizadorweb.backend.css.StateCss;
import josecarlos.analizadorweb.backend.css.TagCss;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class CssAnalizer extends Analizer {

    private StateCss state;

    public CssAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText, currentIndex, tokenList);
        this.state = StateCss.BEFORE_SELECTOR;
    }

    @Override
    public List<Token> analize() {
        switch (state) {
            case BEFORE_SELECTOR -> {
                return analizeBeforeSelector();
            }
            case AFTER_SELECTOR -> {
                return analizeAfterSelector();
            }
            case IN_BODY_SELECTOR -> {
                return analizeInBodySelector();
            }
            default -> {
                return null;
            }
        }
    }

    private List<Token> analizeBeforeSelector() {
        char currentChar = charActual();
        List<Token> list = null;
        if (currentChar == '*') {
            list = addSelectorUniversal();
        } else if (currentChar == '.') {
            list = verifyClassSelector();
        } else if (currentChar == '#') {
            list = verifyIdSelector();
        } else if (Character.isLetter(currentChar)) {
            list = verifyTagSelector();
        }
        if (list != null) {
            state = StateCss.AFTER_SELECTOR;
            return list;
        }
        return null;
    }

    private List<Token> analizeAfterSelector() {
        char currentChar = charActual();
        List<Token> list = null;
        if (currentChar == '>' || currentChar == '+' || currentChar == '~') {
            list = addCombinator();
            if (list != null) {
                state = StateCss.BEFORE_SELECTOR;
                return list;
            }
        } else if (currentChar == '{') {
            list = addOpenBody();
            if (list != null) {
                state = StateCss.IN_BODY_SELECTOR;
                return list;
            }
        }
        list = verifySpaceCombinator();
        if (list != null) {
            return list;
        }
        return null;
    }

    private List<Token> analizeInBodySelector() {
        char currentChar = charActual();
        List<Token> list = null;
        if (currentChar == '}') {
            list = addCloseBody();
            if (list != null) {
                state = StateCss.BEFORE_SELECTOR;
                return list;
            }
        } else if (currentChar == '`' || currentChar == '\'') {
            return verifyStringChar();
        } else if (currentChar == '#') {
            return verifyColor();
        } else if (Character.isDigit(currentChar)){
            return verifyInteger();
        } else if (Character.isLetter(currentChar)){
            return verifyIdentifierOrRuleOrOther();
        }
        return verifyOther();
    }

    private List<Token> verifyInteger() {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        currentIndex.setBookmark();
        char currentChar = charActual();
        
        while (Character.isDigit(currentChar)) {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
        }
        
        Token token = new Token(stringBuilder.toString(), stringBuilder.toString(), 
                TokenType.INTEGER, "[0-9]+", Language.css);
        tokens.add(token);
        return tokens;
    }
    
    private List<Token> verifyOther() {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        currentIndex.setBookmark();
        char currentChar = charActual();
        
        while (!Character.isWhitespace(currentChar) && currentChar != '\0') {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
        }
        
        Optional<OtherCss> other = OtherCss.getValue(stringBuilder.toString());
        if (other.isPresent()) {
            Token token = new Token(stringBuilder.toString(), stringBuilder.toString(),
                    TokenType.OTHERS_CSS, other.get().getTraduction(),Language.css);
            tokens.add(token);
            return tokens;
        }
        
        currentIndex.back();
        return null;
    }
    
    private List<Token> verifyIdentifierOrRuleOrOther() {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        currentIndex.setBookmark();
        char currentChar = charActual();
        boolean isPosibleIdentifier = false;
        if (Character.isLetter(currentChar) && Character.isLowerCase(currentChar)) {
            isPosibleIdentifier = true;
        }
        
        while (Character.isLetterOrDigit(currentChar) || currentChar == '-') {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
        }
        
        Optional<RulesCss> rule = RulesCss.getValue(stringBuilder.toString());
        if (rule.isPresent()) {
            Token token = new Token(stringBuilder.toString(), stringBuilder.toString(),
                    TokenType.RULE, rule.get().getRule(), Language.css);
            tokens.add(token);
            return tokens;
        }
        
        Optional<OtherCss> other = OtherCss.getValue(stringBuilder.toString());
        if (other.isPresent()) {
            Token token = new Token(stringBuilder.toString(), stringBuilder.toString(),
                    TokenType.OTHERS_CSS, other.get().getTraduction(),Language.css);
            tokens.add(token);
            return tokens;
        }
        
        if (isPosibleIdentifier) {
            Token token = new Token(stringBuilder.toString(), stringBuilder.toString(),
                    TokenType.IDENTIFIER, "[a-z]+ [0-9]* (- ([a-z] | [0-9])+)*",Language.css);
            tokens.add(token);
            return tokens;
        }
        currentIndex.back();
        return null;
    }

    private List<Token> verifyColor() {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();
        currentIndex.setBookmark();
        stringBuilder.append(currentChar);
        currentIndex.next();

        int countChar = 0;
        currentChar = charActual();
        while (Character.isLetterOrDigit(currentChar)) {
            countChar++;
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
        }

        if (countChar == 3 || countChar == 6) {
            Token token = new Token(stringBuilder.toString(), stringBuilder.toString(),
                    TokenType.COLOR, "#[A-F][0-9]", Language.css);
            tokens.add(token);
            return tokens;
        }
        currentIndex.back();
        return null;
    }

    private List<Token> verifyStringChar() {
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        String charString = getStringChar(currentChar);
        
        if (charString.equals("'[A-Za-z]'")) {
            Token token = new Token(charString, charString, TokenType.OTHERS_CSS, "'[A-Za-z]'", Language.css);
            tokens.add(token);
            return tokens;
        }
        
        Token token = new Token(charString, charString,
                TokenType.STRING_CSS, "\"[a-zA-Z]|[0-9]|[.]\"", Language.css);
        tokens.add(token);
        return tokens;
    }

    private List<Token> verifySpaceCombinator() {
        List<Token> list = analizeBeforeSelector();
        List<Token> tokens = new ArrayList<>();

        if (list == null || list.isEmpty()) {
            return null;
        }
        tokens.add(new Token(" ", " ", TokenType.COMBINATOR, " ", Language.css));
        for (Token token : list) {
            tokens.add(token);
        }
        return tokens;
    }

    private List<Token> addCombinator() {
        List<Token> tokens = new ArrayList<>();
        Token token;
        char currentChar = charActual();
        switch (currentChar) {
            case '>' -> {
                token = new Token(">", ">", TokenType.COMBINATOR, ">", Language.css);
            }
            case '#' -> {
                token = new Token("#", "#", TokenType.COMBINATOR, "#", Language.css);
            }
            case '~' -> {
                token = new Token("~", "~", TokenType.COMBINATOR, "~", Language.css);
            }
            default -> {
                return null;
            }
        }
        currentIndex.next();
        tokens.add(token);
        return tokens;
    }

    private List<Token> addSelectorUniversal() {
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        if (currentChar == '*') {
            Token token = new Token("*", "*", TokenType.SELECTOR_U, "*", Language.css);
            tokens.add(token);
            currentIndex.next();
            return tokens;
        }
        return null;
    }

    private List<Token> addOpenBody() {
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        if (currentChar == '{') {
            Token token = new Token("{", "{", TokenType.OTHERS_CSS, "{", Language.css);
            tokens.add(token);
            currentIndex.next();
            return tokens;
        }
        return null;
    }

    private List<Token> addCloseBody() {
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        if (currentChar == '}') {
            Token token = new Token("}", "}", TokenType.OTHERS_CSS, "}", Language.css);
            tokens.add(token);
            currentIndex.next();
            return tokens;
        }
        return null;
    }

    private List<Token> verifyIdSelector() {
        List<Token> tokens = new ArrayList<>();
        currentIndex.setBookmark();
        Token tokenSelectorClass = new Token("#", "#", TokenType.SELECTOR_ID, "#", Language.css);
        currentIndex.next();

        Token tokenIdentifierSelector = getIdentifierSelector();
        if (tokenIdentifierSelector == null) {
            currentIndex.back();
            return null;
        }
        tokens.add(tokenSelectorClass);
        tokens.add(tokenIdentifierSelector);
        return tokens;
    }

    private List<Token> verifyClassSelector() {
        List<Token> tokens = new ArrayList<>();
        currentIndex.setBookmark();
        Token tokenSelectorClass = new Token(".", ".", TokenType.SELECTOR_CLASS, ".", Language.css);
        currentIndex.next();

        Token tokenIdentifierSelector = getIdentifierSelector();
        if (tokenIdentifierSelector == null) {
            currentIndex.back();
            return null;
        }
        tokens.add(tokenSelectorClass);
        tokens.add(tokenIdentifierSelector);
        return tokens;
    }

    private Token getIdentifierSelector() {
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = charActual();
        if (Character.isLowerCase(currentChar) && Character.isLetter(currentChar)) {
            stringBuilder.append(currentChar);
            currentIndex.next();
            currentChar = charActual();

            while ((Character.isLowerCase(currentChar) && Character.isLetter(currentChar)) || Character.isDigit(currentChar) || currentChar == '-') {
                stringBuilder.append(currentChar);
                currentIndex.next();
                currentChar = charActual();
            }
            if (!Character.isLetter(currentChar)) {
                Token token = new Token(stringBuilder.toString(), stringBuilder.toString(), TokenType.SELECTOR_IDENTIFIER,
                        ".[a-z]+ [0-9]* (- ([a-z] | [0-9])+)*", Language.css);
                return token;
            }
        }
        return null;
    }

    private List<Token> verifyTagSelector() {
        currentIndex.setBookmark();
        List<Token> tokens = new ArrayList<>();
        String text = getText();
        Token token;

        try {
            TagCss selectorTag = TagCss.valueOf(text);
            token = new Token(text, selectorTag.name(), TokenType.SELECTOR_TAG, selectorTag.name(), Language.css);
        } catch (Exception e) {
            currentIndex.back();
            return null;
        }

        tokens.add(token);
        return tokens;
    }
}
