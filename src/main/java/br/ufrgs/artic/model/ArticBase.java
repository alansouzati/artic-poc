package br.ufrgs.artic.model;

import br.ufrgs.artic.utils.FileUtils;
import org.w3c.dom.Element;

import java.util.regex.Pattern;

public abstract class ArticBase {

    protected final Element element;
    protected final Element previousElement;
    protected String originalText;
    protected String alignment;
    protected String previousAlignment;
    protected final int index;
    private final Double averagePageFontSize;

    //patterns
    private static final Pattern NUMBER_ONLY_PATTERN = Pattern.compile("\\d+");

    public ArticBase(int index, Element element,
                     Element previousElement, Double averageFontSize,
                     String alignment, String previousAlignment) {
        this.element = element;
        this.previousElement = previousElement;
        this.index = index;
        this.averagePageFontSize = averageFontSize;
        this.alignment = alignment;
        this.previousAlignment = previousAlignment;
    }

    public String getOriginalText() {
        if (originalText == null) {
            String text = element.getTextContent();
            originalText = (text != null && !text.isEmpty()) ? element.getTextContent().trim().replaceAll("\\s+", " ") : "";
        }
        return originalText;
    }

    public String getOriginalTextNoSpace() {
        return getOriginalText().replaceAll("\\n", "").replaceAll(" ", "");
    }

    public String getOriginalTextNoSpaceOnlyLowerCaseText() {
        return getOriginalTextNoSpace().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    public boolean isNumberOnly() {
        return NUMBER_ONLY_PATTERN.matcher(getOriginalTextNoSpaceOnlyLowerCaseText()).matches();
    }

    public boolean isBold() {
        String boldString = element.getAttribute("bold");
        boolean bold = false;
        if (boldString != null && !boldString.isEmpty()) {
            bold = Boolean.valueOf(boldString);
        }
        return bold;
    }

    public boolean isPreviousBold() {
        if (previousElement == null) {
            return false;
        }
        String boldString = previousElement.getAttribute("bold");
        boolean bold = false;
        if (boldString != null && !boldString.isEmpty()) {
            bold = Boolean.valueOf(boldString);
        }
        return bold;
    }

    public boolean isItalic() {
        String italicString = element.getAttribute("italic");
        boolean italic = false;
        if (italicString != null && !italicString.isEmpty()) {
            italic = Boolean.valueOf(italicString);
        }
        return italic;
    }

    public boolean isPreviousItalic() {
        if (previousElement == null) {
            return false;
        }
        String italicString = previousElement.getAttribute("italic");
        boolean italic = false;
        if (italicString != null && !italicString.isEmpty()) {
            italic = Boolean.valueOf(italicString);
        }
        return italic;
    }

    public String getFormat() {
        String currentFormat = getFontSize() + isBold() + isItalic() + getFontFace() + alignment;

        if (currentFormat.equals(getPreviousFormat())) {
            return "same";
        } else {
            return "new";
        }
    }

    private String getPreviousFormat() {
        String fontFace = (previousElement != null) ? previousElement.getAttribute("fontFace") : "";

        return getPreviousFontSize() + isPreviousBold() + isPreviousItalic() + fontFace + previousAlignment;
    }

    public String getFontSize() {
        String fontSizeString = "normal";
        if (element.getAttribute("fontSize") != null && !element.getAttribute("fontSize").isEmpty()) {
            double fontSize = Double.valueOf(element.getAttribute("fontSize").replaceAll(",", "\\."));
            fontSizeString = getFontSizeString(fontSizeString, fontSize);
        }

        return fontSizeString;
    }

    public String getPreviousFontSize() {
        String fontSizeString = "normal";
        if (previousElement != null && previousElement.getAttribute("fontSize") != null && !previousElement.getAttribute("fontSize").isEmpty()) {
            double fontSize = Double.valueOf(previousElement.getAttribute("fontSize").replaceAll(",", "\\."));
            fontSizeString = getFontSizeString(fontSizeString, fontSize);
        }

        return fontSizeString;
    }

    protected String getFontSizeString(String fontSizeString, double fontSize) {
        //line normalization normalization (fontSize, top and left)
        if (fontSize >= averagePageFontSize + (averagePageFontSize * 0.10) && fontSize < averagePageFontSize + (averagePageFontSize * 0.45)) {
            fontSizeString = "medium";
        } else if (fontSize >= averagePageFontSize + (averagePageFontSize * 0.45)) {
            fontSizeString = "big";
        } else if (fontSize < averagePageFontSize - (averagePageFontSize * 0.10)) {
            fontSizeString = "small";
        }
        return fontSizeString;
    }

    public String getFontFace() {
       return element.getAttribute("fontFace");
    }

}
