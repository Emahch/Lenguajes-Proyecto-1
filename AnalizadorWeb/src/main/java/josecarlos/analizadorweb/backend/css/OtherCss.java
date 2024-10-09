package josecarlos.analizadorweb.backend.css;

import java.util.Optional;

/**
 *
 * @author emahch
 */
public enum OtherCss {
    PX("px"),
    PRC("%"),
    REM("rem"),
    EM("em"),
    VW("vw"),
    VH("vh"),
    HV(":hover"),
    AC(":active"),
    NT(":not()"),
    NC(":nth-child()"),
    ODD("odd"),
    EVEN("even"),
    BF("::before"),
    AF("::after"),
    CLN(":"),
    SC(";"),
    CM(","),
    RNG("'[A-Za-z]'"),
    OP("("),
    CP(")"),
    OCB("{");

    private final String traduction;

    private OtherCss(String traduction) {
        this.traduction = traduction;
    }

    public String getTraduction() {
        return traduction;
    }

    public static Optional<OtherCss> getValue(String text) {
        OtherCss[] values = OtherCss.values();
        for (OtherCss value : values) {
            if (text != null && value.getTraduction().equals(text)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
