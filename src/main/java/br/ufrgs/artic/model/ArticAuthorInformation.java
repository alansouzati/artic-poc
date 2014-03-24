package br.ufrgs.artic.model;

import org.w3c.dom.Element;

public class ArticAuthorInformation extends ArticWord {

    private final int paragraphIndex;

    public ArticAuthorInformation(int index, int lineIndex,
                                  Element element, Element previousElement, Double averagePageFontSize,
                                  String alignment, String previousAlignment,
                                  Element lineElement, Element previousLineElement, int paragraphIndex,
                                  Element sectionElement) {
        super(index, lineIndex, element, previousElement, averagePageFontSize,
                alignment, previousAlignment, lineElement, previousLineElement, sectionElement);

        this.paragraphIndex = paragraphIndex;
    }

    @Override
    public String toString() {
        StringBuilder articWord = new StringBuilder();

        articWord.append(getOriginalTextNoSpace()).append(" ");
        articWord.append(index).append(" ");
        articWord.append(lineIndex).append(" ");
        articWord.append(getCharacterSize()).append(" ");
        articWord.append(isPossibleEmail()).append(" ");
        articWord.append(!isPossibleEmail() && isPossibleAffiliation()).append(" ");
        articWord.append(isWebsite()).append(" ");
        articWord.append(getFormat()).append(" ");
        articWord.append(getFontSize()).append(" ");
        articWord.append(isPossibleEmailApart()).append(" ");

        return articWord.toString();
    }

    public int getParagraphIndex() {
        return paragraphIndex;
    }
}
