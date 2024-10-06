package josecarlos.analizadorweb.backend.analizadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.html.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class StateAnalizer extends Analizer {

    private HtmlAnalizer htmlAnalizer;
    private JsAnalizer jsAnalizer;
    private CssAnalizer cssAnalizer;
    private int bookmark;
    private List<Token> tokens;

    private Analizer currentAnalizer;
    private Language currentLanguage;

    public StateAnalizer(String inputText) {
        super(inputText);
        this.currentIndex = 0;
        this.currentLine = 0;
        this.currentColumn = 0;
        this.currentLanguage = Language.NO_ESPECIFIED;
        this.htmlAnalizer = new HtmlAnalizer(inputText);
        this.cssAnalizer = new CssAnalizer(inputText);
        this.jsAnalizer = new JsAnalizer(inputText);
    }

    private void setBookmark() {
        this.bookmark = currentIndex;
    }

    private void back() {
        currentIndex = bookmark;
    }

    /**
     * Metodo encargado de analize todo el texto y regresar una lista de lineas con tokens
     *
     * @param inputCode
     * @return lista de lineas
     */
    public List<Token> generarTokens() {
        tokens = new ArrayList<>();

        while (currentIndex < inputText.length()) {
            char currentChar = charActual();

            if (currentChar == '>') {
                setBookmark();
                Optional<Token> posibleNewLanguage = analize(currentIndex);
                if (posibleNewLanguage.isPresent()) {
                    tokens.add(posibleNewLanguage.get());
                } else {
                    back();
                }
            }
            currentChar = charActual();

            if (currentLanguage.equals(Language.NO_ESPECIFIED)) {
                next();
            } else {
                if (Character.isWhitespace(currentChar)) {
                    next();
                } else if (currentChar == '\n') {
                    nextLine();
                } else {
                    setBookmark();
                    Optional<Token> posibleToken = currentAnalizer.analize(currentIndex);
                    if (posibleToken.isPresent()) {
                        currentIndex = currentAnalizer.getCurrentIndex();
                        Token token = posibleToken.get();
                        token.SetLocation(currentLine, currentColumn);
                        tokens.add(token);
                    } else {
                        back();
                        tokens.add(analizeErrorToken());
                    }
                }

            }
        }

        for (Token token : tokens) {
            System.out.println(token.getToken() + token.getRegularExpresion());
        }

        return tokens;
    }

    private Token analizeErrorToken() {
        return null;
    }

    private void changeActiveAnalizer() {
        switch (currentLanguage) {
            case css ->
                currentAnalizer = cssAnalizer;
            case html ->
                currentAnalizer = htmlAnalizer;
            case js ->
                currentAnalizer = jsAnalizer;
            default ->
                currentAnalizer = null;
        }
    }

    private Optional<Language> getLanguageIdentifier(StringBuilder identifier) {
        char currentChar = charActual();
        while (Character.isLetter(currentChar)) {
            identifier.append(currentChar);
            next();
            currentChar = charActual();
        }
        if (identifier.isEmpty()) {
            return Optional.empty();
        }

        Language language;
        try {
            language = Language.valueOf(identifier.toString());
        } catch (Exception e) {
            return Optional.empty();
        }

        if (language.equals(Language.NO_ESPECIFIED)) {
            return Optional.empty();
        }

        return Optional.of(language);
    }

    @Override
    public Optional<Token> analize(int currentIndex) {
        StringBuilder identifier = new StringBuilder();
        char currentChar = charActual();
        identifier.append(currentChar);
        next();
        currentChar = charActual();

        if (currentChar == '>') {
            identifier.append(currentChar);
            next();
            currentChar = charActual();
            if (currentChar == '[') {
                identifier.append(currentChar);
                next();
                Optional<Language> posibleNewLanguage = getLanguageIdentifier(identifier);
                if (posibleNewLanguage.isPresent()) {
                    currentChar = charActual();
                    if (currentChar == ']') {
                        identifier.append(currentChar);
                        next();
                        currentLanguage = posibleNewLanguage.get();
                        changeActiveAnalizer();
                        Token tokenLanguage = new Token(
                                identifier.toString(),
                                "",
                                TokenType.STATE,
                                ">>[" + currentLanguage.name() + "]"
                        );
                        return Optional.of(tokenLanguage);
                    }
                }
            }
        }
        return Optional.empty();
    }

}
