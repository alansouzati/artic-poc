package br.ufrgs.artic.sectLabel;

/**
 * Punctuation features from sect label
 */
public enum SectLabelPunctuation {

    POSSIBLE_WEB("possibleWeb"),
    END_NUMBERING("endNumbering"),
    POSSIBLE_EMAIL("possibleEmail"),
    LINE_PUNCT_OTHERS("linePunctOthers");

    private final String displayName;

    SectLabelPunctuation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
