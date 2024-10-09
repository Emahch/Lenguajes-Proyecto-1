package josecarlos.analizadorweb.backend.tokens;

import java.awt.Point;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;

/**
 *
 * @author emahch
 */
public class Token {
    private String token;
    private String traduction;
    private TokenType type;
    private Language language;
    private String regularExpresion;
    private Point location;
    private boolean errorToken;
    private Language originalLanguage;

    public Token(String token, String traduction, TokenType type, String regularExpresion, Language language) {
        this.token = token;
        this.language = language;
        this.traduction = traduction;
        this.type = type;
        this.regularExpresion = regularExpresion;
        this.errorToken = false;
    }
    
    public void SetLocation(int line, int column){
        this.location = new Point(column, line);
    }

    public String getTraduction() {
        return traduction;
    }

    public Language getLanguage() {
        return language;
    }

    public String getToken() {
        return token;
    }

    public TokenType getType() {
        return type;
    }

    public String getRegularExpresion() {
        return regularExpresion;
    }

    public Optional<Point> getLocation() {
        if (location == null) {
            return Optional.empty();
        }
        return Optional.of(location);
    }
    
    public boolean isIgnored(){
        return type.equals(TokenType.JUMP_LINE) || type.equals(TokenType.WHITE_SPACE);
    }

}
