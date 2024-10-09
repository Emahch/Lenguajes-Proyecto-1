package josecarlos.analizadorweb.backend.css;

import java.util.Optional;

/**
 *
 * @author emahch
 */
public enum RulesCss {
    COL("color"),
    BC("background-color"),
    BG("background"),
    FS("font-size"),
    FW("font-weight"),
    FF("font-family"),
    FA("font-align"),
    W("width"),
    H("height"),
    MW("min-width"),
    MH("min-height"),
    XW("max-width"),
    XH("max-height"),
    D("display"),
    I("inline"),
    B("block"),
    IB("inline-block"),
    F("flex"),
    G("grid"),
    N("none"),
    M("margin"),
    BR("border"),
    P("padding"),
    C("content"),
    BCO("border-color"),
    BST("border-style"),
    BW("border-width"),
    BT("border-top"),
    BB("border-bottom"),
    BL("border-left"),
    BRG("border-right"),
    BS("box-sizing"),
    BBX("border-box"),
    PST("position"),
    STA("static"),
    REL("relative"),
    ABS("absolute"),
    STI("sticky"),
    FX("fixed"),
    T("top"),
    BTTM("bottom"),
    L("left"),
    R("right"),
    ZI("z-index"),
    JC("justify-content"),
    AI("align-items"),
    BRD("border-radius"),
    A("auto"),
    FLO("float"),
    LS("list-style"),
    TA("text-align"),
    BSH("box-shadow");

    private final String rule;

    private RulesCss(String rule) {
        this.rule = rule;
    }

    public static Optional<RulesCss> getValue(String text) {
        RulesCss[] rules = RulesCss.values();
        for (RulesCss rule : rules) {
            if (text != null && rule.getRule().equals(text)) {
                return Optional.of(rule);
            }
        }
        return Optional.empty();
    }

    public String getRule() {
        return rule;
    }

}
