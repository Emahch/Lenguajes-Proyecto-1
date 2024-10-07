package josecarlos.analizadorweb.backend.analizadores;

import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.html.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class StateAnalizer extends Analizer {

    private TokenList tokens;
    private Language currentLanguage;

    public StateAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText,currentIndex, tokenList);
    }

    @Override
    public void analize() {
        currentIndex.setBookmark();
        StringBuilder identifier = new StringBuilder();
        char currentChar = charActual();
        identifier.append(currentChar);
        currentIndex.next();
        currentChar = charActual();

        if (currentChar == '>') {
            identifier.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
            if (currentChar == '[') {
                identifier.append(currentChar);
                currentIndex.next();
                Optional<Language> posibleNewLanguage = getLanguageIdentifier(identifier);
                if (posibleNewLanguage.isPresent()) {
                    currentChar = charActual();
                    if (currentChar == ']') {
                        identifier.append(currentChar);
                        currentIndex.next();
                        currentLanguage = posibleNewLanguage.get();
                        Token tokenLanguage = new Token(
                                identifier.toString(),
                                "",
                                TokenType.STATE,
                                ">>[" + currentLanguage.name() + "]",
                                Language.STATE
                        );
                        tokens.addToken(tokenLanguage);
                    }
                }
            }
        }
        currentIndex.back();
    }
    
    private Optional<Language> getLanguageIdentifier(StringBuilder identifier) {
        char currentChar = charActual();
        while (Character.isLetter(currentChar)) {
            identifier.append(currentChar);
            currentIndex.next();
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

        if (language.equals(Language.NO_SPECIFIED)) {
            return Optional.empty();
        }

        return Optional.of(language);
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

}
