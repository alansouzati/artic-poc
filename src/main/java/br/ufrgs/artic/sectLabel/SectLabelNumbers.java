package br.ufrgs.artic.sectLabel;

/**
 * Numbers features from sect label
 */
public enum SectLabelNumbers {

    NUMBERS_FOOTNOTE("numFootnote"),
    NUMBERS_WEB_FOOTNOTE("numWebFootnote"),
    NUMBERS_OTHERS("lineNumOthers"), //default case
    NUMBERS_POS_CATEGORIES("posCategory"),
    NUMBERS_POS_SUBSEC("posSubsec"),
    NUMBERS_POS_SUBSUBSEC("posSubsubsec")
    ;
    private final String displayName;

    SectLabelNumbers(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
