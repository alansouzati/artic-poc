package br.ufrgs.artic.model;

import org.w3c.dom.Element;

import java.util.regex.Pattern;

public class ArticFooter extends ArticWord {

    // patterns
    private static final Pattern ISBN_PATTERN = Pattern.compile("(\\d+(\\-|–))(\\d+(\\-|–))(\\d+(\\-|–))[\\d+(\\-|–)]+(/)?([/\\d+]+)(\\.\\.\\.[$|\\.|,|\\d]+)?(,|\\.|;)*");
    private static final Pattern PUBLISHER_PATTERN = Pattern.compile("acm|elsevier|ieee|springer-verlag");
    private static final Pattern ISSN_PATTERN = Pattern.compile("(\\d\\d\\d\\d)(-|–)(\\d\\d\\d\\d)[/|$]*(,|\\.|;)?");
    private static final Pattern DOI_PATTERN = Pattern.compile("(doi:)?(\\d\\d\\.\\d\\d\\d\\d)/[[A-Za-z0-9]+\\.]+");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("@");

    public ArticFooter(int index, int lineIndex,
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
        articWord.append(isMonth()).append(" ");
        articWord.append(isPossibleConferenceName()).append(" ");
        articWord.append(isDays()).append(" ");
        articWord.append(isCountry()).append(" ");
        articWord.append(isYear()).append(" ");
        articWord.append(isWebsite()).append(" ");
        articWord.append(isISBN()).append(" ");
        articWord.append(isPublisher()).append(" ");
        articWord.append(isPossibleEmail()).append(" ");
        articWord.append(isNumberOnly()).append(" ");
        articWord.append(isPossibleAffiliation()).append(" ");
        articWord.append(isISSN()).append(" ");
        articWord.append(isDOI()).append(" ");

        return articWord.toString();
    }

    @Override
    public boolean isPossibleEmail() {
        return EMAIL_PATTERN.matcher(getOriginalText().toLowerCase()).find();
    }

    @Override
    public boolean isPossibleAffiliation() {
        return isPossibleUniversity() || isCountry() || isPossibleDepartment();
    }

    public boolean isISBN() {
        return ISBN_PATTERN.matcher(getOriginalTextNoSpace()).matches();
    }

    public boolean isPublisher() {
        return PUBLISHER_PATTERN.matcher(getOriginalTextNoSpace().toLowerCase()).find();
    }

    public boolean isISSN() {
        return ISSN_PATTERN.matcher(getOriginalTextNoSpace()).matches();
    }

    public boolean isDOI() {
        return DOI_PATTERN.matcher(getOriginalTextNoSpace()).matches() || getOriginalTextNoSpace().toLowerCase().trim().startsWith("doi:");
    }

}
