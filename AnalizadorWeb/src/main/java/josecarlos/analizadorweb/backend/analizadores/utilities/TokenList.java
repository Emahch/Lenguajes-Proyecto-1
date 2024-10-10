package josecarlos.analizadorweb.backend.analizadores.utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.analizadorweb.backend.Language;
import josecarlos.analizadorweb.backend.tokens.TokenType;
import josecarlos.analizadorweb.backend.tokens.Token;

/**
 *
 * @author emahch
 */
public class TokenList {

    private final static String HEADER = """
                                          <!DOCTYPE html>
                                         <html lan="en">
                                         <head>
                                            <meta charset="UTF-8">
                                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                            <title>Document</title>
                                            <style>
                                         """;

    private final static String HEADER_REPORT = """
                                                <!DOCTYPE html>
                                                <html lan="en">
                                                
                                                <head>
                                                    <meta charset="UTF-8">
                                                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                                    <title>Report</title>
                                                    <style>
                                                        html,
                                                        body {
                                                            height: 100%;
                                                        }
                                                
                                                        body {
                                                            margin: 0;
                                                            background: linear-gradient(45deg, #690a0a, #000000);
                                                            font-family: sans-serif;
                                                            font-weight: 100;
                                                        }
                                                
                                                        .container {
                                                            position: absolute;
                                                            top: 50%;
                                                            left: 50%;
                                                            transform: translate(-50%, -50%);
                                                            max-height: 100vh;
                                                            overflow: auto;
                                                        }
                                                
                                                        table {
                                                            width: 800px;
                                                            border-collapse: collapse;
                                                            overflow: hidden;
                                                            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
                                                        }
                                                
                                                        th,
                                                        td {
                                                            padding: 15px;
                                                            background-color: rgba(255, 255, 255, 0.2);
                                                            color: #fff;
                                                        }
                                                
                                                        th {
                                                            text-align: left;
                                                        }
                                                
                                                        thead {
                                                            th {
                                                                background-color: #55608f;
                                                            }
                                                        }
                                                
                                                        tbody {
                                                            tr {
                                                                &:hover {
                                                                    background-color: rgba(255, 255, 255, 0.3);
                                                                }
                                                            }
                                                
                                                            td {
                                                                position: relative;
                                                
                                                                &:hover {
                                                                    &:before {
                                                                        content: "";
                                                                        position: absolute;
                                                                        left: 0;
                                                                        right: 0;
                                                                        top: -9999px;
                                                                        bottom: -9999px;
                                                                        background-color: rgba(255, 255, 255, 0.2);
                                                                        z-index: -1;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    </style>
                                                </head>
                                                <body>
                                                    <div class="container">
                                                """;

    private int line;
    private int column;

    private List<Token> tokens;
    private List<Token> tokensError;
    private List<Integer> removedLines;

    private List<Token> tokensHtml;
    private List<Token> tokensJs;
    private List<Token> tokensCss;

    private StringBuilder codeHtml;
    private StringBuilder codeJs;
    private StringBuilder codeCss;

    public TokenList() {
        this.tokens = new ArrayList<>();
        this.tokensHtml = new ArrayList<>();
        this.tokensJs = new ArrayList<>();
        this.tokensCss = new ArrayList<>();
        this.tokensError = new ArrayList<>();
        this.removedLines = new ArrayList<>();
        this.line = 1;
        this.column = 0;
    }

    public void addToken(Token token) {
        if (token.getType().equals(TokenType.JUMP_LINE) || token.getType().equals(TokenType.WHITE_SPACE)) {
            if (token.getType().equals(TokenType.JUMP_LINE)) {
                nextLine();
                this.column = 0;
            }
        } else {
            nextColumn();
            token.SetLocation(line, column);
            tokens.add(token);
        }

        if (token.getType().equals(TokenType.COMMENT)) {
            this.removedLines.add(line);
        }
        addToCorrectLanguage(token);
    }

    private void addToCorrectLanguage(Token token) {
        switch (token.getLanguage()) {
            case html ->
                tokensHtml.add(token);
            case css ->
                tokensCss.add(token);
            case js ->
                tokensJs.add(token);
            default -> {
            }
        }
    }

    public void addErrorToken(char character, Language currentLanguage) {
        Token token = new Token(String.valueOf(character), String.valueOf(character),
                TokenType.ERROR, String.valueOf(character), currentLanguage);
        addToCorrectLanguage(token);
        tokensError.add(token);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<Integer> getRemovedLines() {
        return removedLines;
    }

    public void nextLine() {
        line++;
    }

    public void nextColumn() {
        column++;
    }

    public String generateHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER);

        for (Token token : tokensCss) {
            Optional<Point> posibleLocation = token.getLocation();
            int linea = 0;
            if (posibleLocation.isPresent()) {
                linea = posibleLocation.get().y;
            }
            if (!isRemovedLine(linea)) {
                sb.append(token.getTraduction());
                if (token.getType().equals(TokenType.JUMP_LINE)) {
                    sb.append("       ");
                }
            }
        }
        sb.append("""
                    </style> 
                    <script>""");
        for (Token token : tokensJs) {
            Optional<Point> posibleLocation = token.getLocation();
            int linea = 0;
            if (posibleLocation.isPresent()) {
                linea = posibleLocation.get().y;
            }
            if (!isRemovedLine(linea)) {
                sb.append(token.getTraduction());
                if (token.getType().equals(TokenType.JUMP_LINE)) {
                    sb.append("       ");
                }
            }
        }
        sb.append("""
                    </script> 
                  </head>
                  <body>""");
        for (Token token : tokensHtml) {
            Optional<Point> posibleLocation = token.getLocation();
            int linea = 0;
            if (posibleLocation.isPresent()) {
                linea = posibleLocation.get().y;
            }
            if (!isRemovedLine(linea)) {
                sb.append(token.getTraduction());
                if (token.getType().equals(TokenType.JUMP_LINE)) {
                    sb.append("   ");
                }
            }
        }
        sb.append(""" 
                  </body>
                  </html>""");

        return sb.toString();
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        String endHead = """
                               <table>
                                     <thead>
                                         <tr>
                                            <th>Token</th>
                                            <th>Expresion Regular</th>
                                            <th>Lenguaje</th>
                                            <th>Tipo</th>
                                            <th>Fila</th>
                                            <th>Columna</th>
                                         </tr>
                                     </thead>
                                     <tbody>""";
        String title = """
                               <h1 style="color: #fff;">Reporte de Tokens</h1>
                       """;

        sb.append(HEADER_REPORT);
        sb.append(title);
        sb.append(endHead);

        for (Token token : tokens) {
            Optional<Point> posibleLocation = token.getLocation();
            int linea = 0;
            int columna = 0;
            if (posibleLocation.isPresent()) {
                linea = posibleLocation.get().y;
                columna = posibleLocation.get().x;
            }

            if (!isRemovedLine(linea)) {
                String item = "                <tr> "
                        + "                   <td>" + token.getToken() + "</td>"
                        + "                   <td>" + token.getRegularExpresion() + "</td>"
                        + "                   <td>" + token.getLanguage().getLanguageType() + "</td>"
                        + "                   <td>" + token.getType().getTokenName() + "</td>"
                        + "                   <td>" + String.valueOf(linea) + "</td>"
                        + "                   <td>" + String.valueOf(columna) + "</td>"
                        + "                <tr> ";
                sb.append(item);
            }
        }
        sb.append("""
                              </tbody>
                          </table>
                      </div>
                  </body>
                  """);
        return sb.toString();
    }

    public String generateErrorReport() {
        StringBuilder sb = new StringBuilder();
        String endHead = """
                               <table>
                                     <thead>
                                         <tr>
                                            <th>Token</th>
                                            <th>Lenguaje</th>
                                            <th>Fila</th>
                                            <th>Columna</th>
                                         </tr>
                                     </thead>
                                     <tbody>""";
        String title = """
                               <h1 style="color: #fff;">Reporte de Errores</h1>
                       """;

        sb.append(HEADER_REPORT);
        sb.append(title);
        sb.append(endHead);

        for (Token token : tokens) {
            Optional<Point> posibleLocation = token.getLocation();
            int linea = 0;
            int columna = 0;
            if (posibleLocation.isPresent()) {
                linea = posibleLocation.get().y;
                columna = posibleLocation.get().x;
            }
            if (token.getType() == TokenType.ERROR && !isRemovedLine(linea)) {
                String item = "                <tr> "
                        + "                   <td>" + token.getToken() + "</td>"
                        + "                   <td>" + token.getLanguage().getLanguageType() + "</td>"
                        + "                   <td>" + String.valueOf(linea) + "</td>"
                        + "                   <td>" + String.valueOf(columna) + "</td>"
                        + "                <tr> ";
                sb.append(item);
            }
        }
        sb.append("""
                              </tbody>
                          </table>
                      </div>
                  </body>
                  """);
        return sb.toString();
    }

    public String generateOptimizationReport() {
        StringBuilder sb = new StringBuilder();
        String endHead = """
                               <table>
                                     <thead>
                                         <tr>
                                            <th>Token</th>
                                            <th>Expresion Regular</th>
                                            <th>Lenguaje</th>
                                            <th>Tipo</th>
                                            <th>Fila</th>
                                            <th>Columna</th>
                                         </tr>
                                     </thead>
                                     <tbody>""";
        String title = """
                               <h1 style="color: #fff;">Reporte de Optimización</h1>
                       """;

        sb.append(HEADER_REPORT);
        sb.append(title);
        sb.append(endHead);

        for (Token token : tokens) {
            Optional<Point> posibleLocation = token.getLocation();
            int linea = 0;
            int columna = 0;
            if (posibleLocation.isPresent()) {
                linea = posibleLocation.get().y;
                columna = posibleLocation.get().x;
            }

            if (isRemovedLine(linea)) {
                String item = "                <tr> "
                        + "                   <td>" + token.getToken() + "</td>"
                        + "                   <td>" + token.getRegularExpresion() + "</td>"
                        + "                   <td>" + token.getLanguage().getLanguageType() + "</td>"
                        + "                   <td>" + token.getType().getTokenName() + "</td>"
                        + "                   <td>" + String.valueOf(linea) + "</td>"
                        + "                   <td>" + String.valueOf(columna) + "</td>"
                        + "                <tr> ";
                sb.append(item);
            }
        }
        sb.append("""
                              </tbody>
                          </table>
                      </div>
                  </body>
                  """);
        return sb.toString();
    }

    private boolean isRemovedLine(int line) {
        for (Integer removedLine : removedLines) {
            if (removedLine == line) {
                return true;
            }
        }
        return false;
    }
}
