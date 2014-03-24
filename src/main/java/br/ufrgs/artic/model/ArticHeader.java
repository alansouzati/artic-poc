package br.ufrgs.artic.model;

import org.w3c.dom.Element;

import java.util.regex.Pattern;

public class ArticHeader extends ArticWord {

    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    private static final  Pattern NUMERAL_PATTERN = Pattern.compile("(\\d)+(th|st|nd|rd)");

    public ArticHeader(int index, int lineIndex,
                       Element element, Element previousElement, Double averagePageFontSize,
                       String alignment, String previousAlignment,
                       Element lineElement, Element previousLineElement, Element sectionElement) {
        super(index, lineIndex, element, previousElement, averagePageFontSize,
                alignment, previousAlignment, lineElement, previousLineElement, sectionElement);
    }

    @Override
    public String toString() {
        StringBuilder articWord = new StringBuilder();

        articWord.append(getOriginalTextNoSpace()).append(" ");
        articWord.append(index).append(" ");
        articWord.append(lineIndex).append(" ");
        articWord.append(getCharacterSize()).append(" ");
        articWord.append(isNumeral()).append(" ");
        articWord.append(isPossibleConference()).append(" ");
        articWord.append(isMonth()).append(" ");
        articWord.append(isNumberOnly()).append(" ");
        articWord.append(isYear()).append(" ");
        articWord.append(isCountry()).append(" ");
        articWord.append(hasSpecialChar()).append(" ");
        articWord.append(isWebsite()).append(" ");

        return articWord.toString();
    }

    public boolean isNumeral() {
        return NUMERAL_PATTERN.matcher(getOriginalTextNoSpace().replaceAll("[^a-zA-Z0-9]", "")).matches();
    }

    public boolean hasSpecialChar() {
        return SPECIAL_CHAR_PATTERN.matcher(getOriginalTextNoSpace()).find();
    }

}
