package josecarlos.analizadorweb.backend.analizadores;

import josecarlos.analizadorweb.backend.analizadores.utilities.TokenList;
import josecarlos.analizadorweb.backend.analizadores.utilities.Index;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.html.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class StateAnalizer extends Analizer {

    private Language currentLanguage;

    public StateAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText, currentIndex, tokenList);
    }

    @Override
    public boolean analize() {
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
                Optional<Language> posibleNewLanguage = getLanguageIdentifier();
                if (posibleNewLanguage.isPresent()) {
                    identifier.append(posibleNewLanguage.get().name());
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
                        tokenList.addToken(tokenLanguage);
                        return true;
                    }
                }
            }
        }
        currentIndex.back();
        return false;
    }

    private Optional<Language> getLanguageIdentifier() {
        char currentChar = charActual();
        StringBuilder identifier = new StringBuilder();
        while (Character.isLetter(currentChar)) {
            identifier.append(currentChar);
            currentIndex.next();
            currentChar = charActual();
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
