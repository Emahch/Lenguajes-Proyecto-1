package josecarlos.analizadorweb.backend.tokens;

import java.awt.Point;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.html.tokens.TokenType;

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

    public Token(String token, String traduction, TokenType type, String regularExpresion) {
        this.token = token;
        this.traduction = traduction;
        this.type = type;
        this.regularExpresion = regularExpresion;
    }
    
    public void SetLocation(int line, int column){
        this.location = new Point(column, line);
    }

    public String getTraduction() {
        return traduction;
    }

    public void setTraduction(String traduction) {
        this.traduction = traduction;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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

    public Point getLocation() {
        return location;
    }
    
    public boolean isIgnored(){
        return type.equals(TokenType.IGNORE);
    }

}
