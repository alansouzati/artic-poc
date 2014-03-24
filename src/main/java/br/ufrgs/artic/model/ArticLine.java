package br.ufrgs.artic.model;

import org.w3c.dom.Element;

/**
 * This class represents an Artic line.
 */
public class ArticLine extends ArticBase {

    private final int leftBucketSize;
    private int topBucketSize;
    private String paragraph;

    public ArticLine(int index, Element element, Element previousElement, Double averagePageFontSize,
                       String alignment, String previousAlignment, int topBucketSize, int leftBucketSize) {
        super(index, element, previousElement, averagePageFontSize, alignment, previousAlignment);
        this.topBucketSize = topBucketSize;
        this.leftBucketSize = leftBucketSize;
    }

    @Override
    public String toString() {
        StringBuilder articleLine = new StringBuilder();

        //articleLine.append(getOriginalTextNoSpace()).append(" ");
        articleLine.append("line_"+index).append(" ");
        articleLine.append(alignment).append(" ");
        articleLine.append(isBold()).append(" ");
        articleLine.append(isUnderline()).append(" ");
        articleLine.append(isItalic()).append(" ");
        articleLine.append(getFontSize()).append(" ");

        int topNormalized = 0;
        int leftNormalized = 0;
        if (element.getAttribute("t") != null && !element.getAttribute("t").isEmpty() &&
                element.getAttribute("l") != null && !element.getAttribute("l").isEmpty()) {

            int top = Integer.parseInt(element.getAttribute("t").replaceAll(",", "\\."));
            topNormalized = top / topBucketSize;

            int left = Integer.parseInt(element.getAttribute("l").replaceAll(",", "\\."));
            leftNormalized = left / leftBucketSize;

        }

        articleLine.append(topNormalized).append(" ");
        articleLine.append(leftNormalized).append(" ");
        articleLine.append(getOriginalText().contains("@")).append(" ");
        articleLine.append(getLineSize()).append(" ");
        articleLine.append(paragraph).append(" ");
        articleLine.append(getFormat()).append(" ");

        return articleLine.toString();
    }

    public boolean isUnderline() {
        String underlineString = element.getAttribute("underlined");
        boolean underline = false;
        if (underlineString != null && !underlineString.isEmpty() && !underlineString.equals("none")) {
            underline = true;
        }
        return underline;
    }

    private String getLineSize() {
        String[] words = getOriginalText().split(" ");
        String characterSize = "zero";
        if (words.length >= 1 && words.length < 5) {
            characterSize = "few";
        } else if (words.length >= 5 && words.length <10) {
            characterSize = "medium";
        } else if (words.length >= 10) {
            characterSize = "many";
        }
        return characterSize;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

}
