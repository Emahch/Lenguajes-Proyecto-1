package josecarlos.analizadorweb.backend.analizadores;

import josecarlos.analizadorweb.backend.tokens.TokenType;
import java.util.ArrayList;
import java.util.List;
import josecarlos.analizadorweb.backend.analizadores.utilities.*;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.html.RWHTML;
import josecarlos.analizadorweb.backend.html.StateHtml;
import josecarlos.analizadorweb.backend.html.TagHTML;
import josecarlos.analizadorweb.backend.html.TagLineHTML;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class HtmlAnalizer extends Analizer {

    private StateHtml state;

    public HtmlAnalizer(String inputText, Index currentIndex, TokenList tokenList) {
        super(inputText, currentIndex, tokenList);
        this.state = StateHtml.WAITING;
    }

    @Override
    public List<Token> analize() {
        char currentChar = charActual();

        if (state.equals(StateHtml.IN_TAG)) {
            return analizeInTag();
        } else {
            if (currentChar == '<') {
                List<Token> list = verifyTag();
                if (list != null) {
                    state = StateHtml.IN_TAG;
                }
                return list;
            } else {
                return verifyDataText();
            }
        }
    }

    private List<Token> analizeInTag() {
        char currentChar = charActual();
        if (currentChar == '/') {
            List<Token> list = verifyEndTag();
            if (list != null) {
                state = StateHtml.WAITING;
            }
            return list;
        } else if (currentChar == '>') {
            List<Token> list = addCloseToken();
            if (list != null) {
                state = StateHtml.WAITING;
            }
            return list;
        } else if (currentChar == '=') {
            return addEqualsToken();
        } else if (currentChar == '"') {
            return verifyString();
        } else if (Character.isLetter(currentChar)) {
            return verifyPR();
        }
        return null;
    }

    private List<Token> verifyDataText() {
        List<Token> tokens = new ArrayList<>();
        String text = getDataText();
        Token openToken = new Token(text, text, TokenType.DATA_TEXT_HTML, "[a-zA-Z]|[0-9]|[.]", Language.html);
        tokens.add(openToken);
        return tokens;
    }

    private List<Token> verifyTag() {
        List<Token> tokens = new ArrayList<>();
        // Creando token de apertura
        index.setBookmark();
        index.next();
        boolean endTag = false;
        char currentChar = charActual();
        if (currentChar == '/') {
            index.next();
            endTag = true;
        }
        String identifierTag = getText();

        Optional<Token> posibleTokenTag = getTokenTag(identifierTag);
        if (posibleTokenTag.isEmpty()) {
            index.back();
            return null;
        }

        if (endTag) {
            Token openToken = new Token("<", "<", TokenType.Etiqueta_de_Cierre, "<", Language.html);
            tokens.add(openToken);
            Token endToken = new Token("/", "/", TokenType.Cierre_Etiqueta, "/", Language.html);
            tokens.add(endToken);
            tokens.add(posibleTokenTag.get());
            return tokens;
        }
        Token openToken = new Token("<", "<", TokenType.Etiqueta_de_Apertura, "<", Language.html);
        tokens.add(openToken);
        tokens.add(posibleTokenTag.get());
        return tokens;
    }

    private List<Token> verifyEndTag() {
        List<Token> tokens = new ArrayList<>();
        index.setBookmark();
        char currentChar = charActual();
        if (currentChar == '/') {
            Token tokenClose = new Token("/", "/", TokenType.Cierre_Etiqueta, "/", Language.html);
            index.next();
            currentChar = charActual();
            if (currentChar == '>') {
                Token tokenEnd = new Token(">", ">", TokenType.Etiqueta_de_Cierre, ">", Language.html);
                tokens.add(tokenClose);
                tokens.add(tokenEnd);
                index.next();
                return tokens;
            }
        }
        index.back();
        return null;
    }

    private List<Token> addCloseToken() {
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        if (currentChar == '>') {
            Token tokenEnd = new Token(">", ">", TokenType.Etiqueta_de_Cierre, ">", Language.html);
            tokens.add(tokenEnd);
            index.next();
            return tokens;
        }
        return null;
    }

    private Optional<Token> getTokenTag(String identifierTag) {
        Token tokenTag;
        try {
            TagHTML tag = TagHTML.valueOf(identifierTag);
            tokenTag = new Token(identifierTag, tag.getTraduction(), TokenType.TAG,
                    tag.name(), Language.html);
        } catch (Exception e) {
            try {
                TagLineHTML tag = TagLineHTML.valueOf(identifierTag);
                tokenTag = new Token(identifierTag, tag.getTraduction(), TokenType.TAG_LINE,
                        tag.name(), Language.html);
            } catch (Exception ex) {
                return Optional.empty();
            }
        }
        return Optional.of(tokenTag);
    }

    private List<Token> verifyPR() {
        List<Token> tokens = new ArrayList<>();
        index.setBookmark();
        String reservedWord = getText();

        Optional<RWHTML> posibleReservedWord = RWHTML.getValue(reservedWord);
        if (posibleReservedWord.isEmpty()) {
            index.back();
            return null;
        }

        Token tokenRW = new Token(reservedWord, posibleReservedWord.get().getReservedWord(),
                TokenType.RESERVED_HTML, posibleReservedWord.get().getReservedWord(), Language.html);

        tokens.add(tokenRW);
        return tokens;
    }

    private List<Token> addEqualsToken() {
        List<Token> tokens = new ArrayList<>();
        char currentChar = charActual();
        if (currentChar == '=') {
            index.next();
            Token tokenEqual = new Token("=", "=",
                    TokenType.RESERVED_HTML, "=", Language.html);
            tokens.add(tokenEqual);
            return tokens;
        }
        return null;
    }

    private List<Token> verifyString() {
        List<Token> tokens = new ArrayList<>();
        String charString = getStringChar('"');
        Token token = new Token(charString, charString,
                TokenType.DATA_STRING_HTML, "\"[a-zA-Z]|[0-9]|[.]\"", Language.html);
        tokens.add(token);
        return tokens;
    }
}
