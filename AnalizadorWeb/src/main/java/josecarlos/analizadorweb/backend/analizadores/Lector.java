package josecarlos.analizadorweb.backend.analizadores;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class Lector {
    
    private Index currentIndex;
    private String inputText;
    private Position currentPosition;
    private int bookmark;
    private int bookmarkColumn;
    private List<Token> tokens;
    
    private HtmlAnalizer htmlAnalizer;
    private JsAnalizer jsAnalizer;
    private CssAnalizer cssAnalizer;

    private Analizer currentAnalizer;
    private Language currentLanguage;
    
    protected char charActual() {
        if (currentIndex.get() >= inputText.length()) {
            return '\0';  // Fin de la cadena
        }
        return inputText.charAt(currentIndex.get());
    }

    protected void nextLine() {
        currentIndex.next();
        currentPosition.setColumn(0);
    }

    protected void next() {
        currentIndex.next();
        currentPosition.nextColumn();
    }
    
    private void setBookmark() {
        this.bookmark = currentIndex.get();
        this.bookmarkColumn = currentPosition.getColumn();
    }

    private void back() {
        this.currentIndex.set(bookmark);
        this.currentPosition.setColumn(bookmarkColumn);
    }

    /**
     * Metodo encargado de analize todo el texto y regresar una lista de lineas con tokens
     *
     * @return lista de lineas
     */
    public List<Token> generarTokens() {
        tokens = new ArrayList<>();

        while (currentIndex.get() < inputText.length()) {
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
}
