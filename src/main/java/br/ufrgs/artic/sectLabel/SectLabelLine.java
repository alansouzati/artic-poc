package br.ufrgs.artic.sectLabel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a sectlabel line.
 */
public class SectLabelLine {

    private final SectLabelNumbers number;
    private final SectLabelPunctuation punctuation;
    private final String location; //xml location
    private final boolean bold;
    private final boolean italic;
    private final String fontSize;
    private String paragraph;
    private final String originalText;
    private final String fontFace;
    private final String alignment;
    private final String format;
    private final boolean hasBulletInside;
    private final boolean belongsToATable;
    private final boolean belongsToAPicture;
    private final List<SectLabelToken> sectLabelTokens;

    public SectLabelLine(SectLabelNumbers sectLabelNumbers, SectLabelPunctuation sectLabelPunctuation,
                         String xmlLocation, String originalText, boolean bold, boolean italic,
                         String fontSizeString, String fontFace, String alignment, String previousFormat,
                         boolean hasBulletInside, boolean belongsToATable, boolean belongsToAPicture) {
        this.number = sectLabelNumbers;
        this.punctuation = sectLabelPunctuation;
        this.location = xmlLocation;
        this.originalText = originalText;
        this.bold = bold;
        this.italic = italic;
        this.fontSize = fontSizeString;
        this.fontFace = fontFace;
        this.alignment = alignment;
        this.hasBulletInside = hasBulletInside;
        this.belongsToATable = belongsToATable;
        this.belongsToAPicture = belongsToAPicture;

        String currentFormat = getFontSize() + isBold() + isItalic() + getFontFace() + getAlignment();

        if (currentFormat.equals(previousFormat)) {
            format = "xmlFormat_continue";
        } else {
            format = "xmlFormat_new";
        }

        sectLabelTokens = new ArrayList<SectLabelToken>();

        List<String> words = Arrays.asList(originalText.split(" "));

        if (words.size() == 0) {
            sectLabelTokens.add(new SectLabelToken("EMPTY"));
            sectLabelTokens.add(new SectLabelToken("EMPTY"));
            sectLabelTokens.add(new SectLabelToken("EMPTY"));
        } else if (words.size() == 1) {
            String word = words.get(0).replaceAll("[^a-zA-Z0-9]", "");
            sectLabelTokens.add(new SectLabelToken(word.isEmpty() ? "EMPTY" : words.get(0)));
            sectLabelTokens.add(new SectLabelToken("EMPTY"));
            sectLabelTokens.add(new SectLabelToken("EMPTY"));
        } else if (words.size() == 2) {
            String word = words.get(0).replaceAll("[^a-zA-Z0-9]", "");
            sectLabelTokens.add(new SectLabelToken(word.isEmpty() ? "EMPTY" : words.get(0)));
            word = words.get(1).replaceAll("[^a-zA-Z0-9]", "");
            sectLabelTokens.add(new SectLabelToken(word.isEmpty() ? "EMPTY" : words.get(1)));
            sectLabelTokens.add(new SectLabelToken("EMPTY"));
        } else if (words.size() >= 3) {
            String word = words.get(0).replaceAll("[^a-zA-Z0-9]", "");
            sectLabelTokens.add(new SectLabelToken(word.isEmpty() ? "EMPTY" : words.get(0)));
            word = words.get(1).replaceAll("[^a-zA-Z0-9]", "");
            sectLabelTokens.add(new SectLabelToken(word.isEmpty() ? "EMPTY" : words.get(1)));
            word = words.get(2).replaceAll("[^a-zA-Z0-9]", "");
            sectLabelTokens.add(new SectLabelToken(word.isEmpty() ? "EMPTY" : words.get(2)));
        }
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getFontSize() {
        return fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public String getFontFace() {
        return fontFace;
    }

    public String getAlignment() {
        return alignment;
    }

    @Override
    public String toString() {
        StringBuilder sectLabelLine = new StringBuilder();

        sectLabelLine.append(number.getDisplayName()).append(" ");
        String position = "POS_0";
        sectLabelLine.append(position).append(" ");
        sectLabelLine.append(punctuation.getDisplayName()).append(" ");

        String[] words = originalText.split(" ");
        String length = "0Word";
        if (words.length == 1) {
            length = "1Word";
        } else if (words.length == 2) {
            length = "2Words";
        } else if (words.length == 3) {
            length = "3Words";
        } else if (words.length == 4) {
            length = "4Words";
        } else if (words.length > 4) {
            length = "5+Words";
        }

        sectLabelLine.append(length).append(" ");
        sectLabelLine.append(location).append(" ");
        sectLabelLine.append(bold ? "xmlBold_yes" : "xmlBold_no").append(" ");
        sectLabelLine.append(italic ? "xmlItalic_yes" : "xmlItalic_no").append(" ");
        sectLabelLine.append(fontSize).append(" ");
        sectLabelLine.append(paragraph).append(" ");
        sectLabelLine.append(format).append(" ");
        sectLabelLine.append(hasBulletInside ? "xmlBullet_yes" : "xmlBullet_no").append(" ");
        sectLabelLine.append(belongsToATable ? "xmlTable_yes" : "xmlTable_no").append(" ");
        sectLabelLine.append(belongsToAPicture ? "xmlPicture_yes" : "xmlPicture_no").append(" ");
        for (SectLabelToken token : sectLabelTokens) {
            sectLabelLine.append(token.toString()).append(" ");
        }

        return sectLabelLine.toString();
    }

    public String getOriginalText() {
        return originalText;
    }
}
