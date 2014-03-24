package br.ufrgs.artic.model;

import br.ufrgs.artic.utils.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents and Artic word
 */
public abstract class ArticWord extends ArticBase {

    protected final int lineIndex;

    private static final List<String> monthList;
    private static final List<String> countryList;
    private static final List<String> conferenceList;

    //patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile("@|email|e\\-mail");
    private static final Pattern UNIVERSITY_PATTERN = Pattern.compile("university|faculty");
    private static final Pattern DEPARTMENT_PATTERN = Pattern.compile("department|center|laboratory|division|" +
            "school|group|research|computer|dept|education|technology|science|defence|develop|development|" +
            "information|branch|institute|library|medic|system|database|software|state|academic|tech|point|normal|community|language|centre|computation");
    private static final Pattern CONTINENT_OCEANS_PATTERN = Pattern.compile("asia|africa|north|america|south|antarctica|europe|australia|ocean|pacific|atlantic|indian|southern|arctic");
    private static final Pattern WEBSITE_PATTERN = Pattern.compile("www|https?");
    private static final Pattern CONFERENCE_NAME_PATTERN = Pattern.compile("[A-Z]+(('|’|`)?\\d\\d)?(,|\\.|;)?");
    private static final Pattern DAYS_PATTERN = Pattern.compile("(\\d\\d)(-|–)(\\d\\d)(th|st|nd|rd)?(,|\\.|;)?");
    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    static {
        monthList = new ArrayList<String>();
        monthList.add("january");
        monthList.add("february");
        monthList.add("march");
        monthList.add("april");
        monthList.add("may");
        monthList.add("june");
        monthList.add("july");
        monthList.add("august");
        monthList.add("september");
        monthList.add("october");
        monthList.add("november");
        monthList.add("december");
        monthList.add("jan");
        monthList.add("feb");
        monthList.add("mar");
        monthList.add("apr");
        monthList.add("may");
        monthList.add("jun");
        monthList.add("jul");
        monthList.add("aug");
        monthList.add("sep");
        monthList.add("oct");
        monthList.add("nov");
        monthList.add("dec");

        countryList = new ArrayList<String>();
        countryList.add("abkhazia");
        countryList.add("afghanistan");
        countryList.add("akrotiri and dhekelia");
        countryList.add("aland");
        countryList.add("albania");
        countryList.add("algeria");
        countryList.add("american samoa");
        countryList.add("andorra");
        countryList.add("angola");
        countryList.add("anguilla");
        countryList.add("antigua and barbuda");
        countryList.add("argentina");
        countryList.add("armenia");
        countryList.add("aruba");
        countryList.add("ascension island");
        countryList.add("australia");
        countryList.add("austria");
        countryList.add("azerbaijan");
        countryList.add("bahamas, the");
        countryList.add("bahrain");
        countryList.add("bangladesh");
        countryList.add("barbados");
        countryList.add("belarus");
        countryList.add("belgium");
        countryList.add("belize");
        countryList.add("benin");
        countryList.add("bermuda");
        countryList.add("bhutan");
        countryList.add("bolivia");
        countryList.add("bosnia and herzegovina");
        countryList.add("botswana");
        countryList.add("brazil");
        countryList.add("brunei");
        countryList.add("bulgaria");
        countryList.add("burkina faso");
        countryList.add("burundi");
        countryList.add("cambodia");
        countryList.add("cameroon");
        countryList.add("canada");
        countryList.add("cape verde");
        countryList.add("cayman islands");
        countryList.add("central africa republic");
        countryList.add("chad");
        countryList.add("chile");
        countryList.add("china");
        countryList.add("christmas island");
        countryList.add("cocos (keeling) islands");
        countryList.add("colombia");
        countryList.add("comoros");
        countryList.add("congo");
        countryList.add("cook islands");
        countryList.add("costa rica");
        countryList.add("cote d'lvoire");
        countryList.add("croatia");
        countryList.add("cuba");
        countryList.add("cyprus");
        countryList.add("czech republic");
        countryList.add("denmark");
        countryList.add("djibouti");
        countryList.add("dominica");
        countryList.add("dominican republic");
        countryList.add("east timor ecuador");
        countryList.add("egypt");
        countryList.add("el salvador");
        countryList.add("equatorial guinea");
        countryList.add("eritrea");
        countryList.add("estonia");
        countryList.add("ethiopia");
        countryList.add("falkland islands");
        countryList.add("faroe islands");
        countryList.add("fiji");
        countryList.add("finland");
        countryList.add("france");
        countryList.add("french polynesia");
        countryList.add("gabon");
        countryList.add("cambia, the");
        countryList.add("georgia");
        countryList.add("germany");
        countryList.add("ghana");
        countryList.add("gibraltar");
        countryList.add("greece");
        countryList.add("greenland");
        countryList.add("grenada");
        countryList.add("guam");
        countryList.add("guatemala");
        countryList.add("guemsey");
        countryList.add("guinea");
        countryList.add("guinea-bissau");
        countryList.add("guyana");
        countryList.add("haiti");
        countryList.add("honduras");
        countryList.add("hong kong");
        countryList.add("hungary");
        countryList.add("iceland");
        countryList.add("india");
        countryList.add("indonesia");
        countryList.add("iran");
        countryList.add("iraq");
        countryList.add("ireland");
        countryList.add("isle of man");
        countryList.add("israel");
        countryList.add("italy");
        countryList.add("jamaica");
        countryList.add("japan");
        countryList.add("jersey");
        countryList.add("jordan");
        countryList.add("kazakhstan");
        countryList.add("kenya");
        countryList.add("kiribati");
        countryList.add("korea, n");
        countryList.add("korea, s");
        countryList.add("kosovo");
        countryList.add("kuwait");
        countryList.add("kyrgyzstan");
        countryList.add("laos");
        countryList.add("latvia");
        countryList.add("lebanon");
        countryList.add("lesotho");
        countryList.add("liberia");
        countryList.add("libya");
        countryList.add("liechtenstein");
        countryList.add("lithuania");
        countryList.add("luxembourg");
        countryList.add("macao");
        countryList.add("macedonia");
        countryList.add("madagascar");
        countryList.add("malawi");
        countryList.add("malaysia");
        countryList.add("maldives");
        countryList.add("mali");
        countryList.add("malta");
        countryList.add("marshall islands");
        countryList.add("mauritania");
        countryList.add("mauritius");
        countryList.add("mayotte");
        countryList.add("mexico");
        countryList.add("micronesia");
        countryList.add("moldova");
        countryList.add("monaco");
        countryList.add("mongolia");
        countryList.add("montenegro");
        countryList.add("montserrat");
        countryList.add("morocco");
        countryList.add("mozambique");
        countryList.add("myanmar");
        countryList.add("nagorno-karabakh");
        countryList.add("namibia");
        countryList.add("nauru");
        countryList.add("nepal");
        countryList.add("netherlands");
        countryList.add("netherlands antilles");
        countryList.add("new caledonia");
        countryList.add("new zealand");
        countryList.add("nicaragua");
        countryList.add("niger");
        countryList.add("nigeria");
        countryList.add("niue");
        countryList.add("norfolk island");
        countryList.add("northern cyprus");
        countryList.add("northern mariana islands");
        countryList.add("norway");
        countryList.add("oman");
        countryList.add("pakistan");
        countryList.add("palau");
        countryList.add("palestine");
        countryList.add("panama");
        countryList.add("papua new guinea");
        countryList.add("paraguay");
        countryList.add("peru");
        countryList.add("philippines");
        countryList.add("pitcaim islands");
        countryList.add("poland");
        countryList.add("portugal");
        countryList.add("puerto rico");
        countryList.add("qatar");
        countryList.add("romania");
        countryList.add("russia");
        countryList.add("rwanda");
        countryList.add("sahrawi arab democratic republic");
        countryList.add("saint-barthelemy");
        countryList.add("saint helena");
        countryList.add("saint kitts and nevis");
        countryList.add("saint lucia");
        countryList.add("saint martin");
        countryList.add("saint pierre and miquelon");
        countryList.add("saint vincent and grenadines");
        countryList.add("samos");
        countryList.add("san marino");
        countryList.add("sao tome and principe");
        countryList.add("saudi arabia");
        countryList.add("senegal");
        countryList.add("serbia");
        countryList.add("seychelles");
        countryList.add("sierra leone");
        countryList.add("singapore");
        countryList.add("slovakia");
        countryList.add("slovenia");
        countryList.add("solomon islands");
        countryList.add("somalia");
        countryList.add("somaliland");
        countryList.add("south africa");
        countryList.add("south ossetia");
        countryList.add("spain");
        countryList.add("sri lanka");
        countryList.add("sudan");
        countryList.add("suriname");
        countryList.add("svalbard");
        countryList.add("swaziland");
        countryList.add("sweden");
        countryList.add("switzerland");
        countryList.add("syria");
        countryList.add("tajikistan");
        countryList.add("tanzania");
        countryList.add("thailand");
        countryList.add("togo");
        countryList.add("tokelau");
        countryList.add("tonga");
        countryList.add("transnistria");
        countryList.add("trinidad and tobago");
        countryList.add("tristan da cunha");
        countryList.add("tunisia");
        countryList.add("turkey");
        countryList.add("turkmenistan");
        countryList.add("turks and caicos islands");
        countryList.add("tuvalu");
        countryList.add("uganda");
        countryList.add("ukraine");
        countryList.add("united arab emirates");
        countryList.add("united kingdom");
        countryList.add("united states");
        countryList.add("usa");
        countryList.add("uruguay");
        countryList.add("uzbekistan");
        countryList.add("vanuatu");
        countryList.add("vatican city");
        countryList.add("venezuela");
        countryList.add("vietnam");
        countryList.add("virgin islands, british");
        countryList.add("virgin islands, u.s.");
        countryList.add("wallis and futuna");
        countryList.add("yemen");
        countryList.add("zambia");
        countryList.add("zimbabwe");

        conferenceList = new ArrayList<String>();
        File conferenceFile = FileUtils.getArticTempResource("conferences.txt");

        try {
            FileInputStream fis = new FileInputStream(conferenceFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            String line;

            while ((line = br.readLine()) != null) {
                conferenceList.add(line);
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private final Element lineElement;
    private final Element sectionElement;
    private final Element previousLineElement;
    private int biggestLineSpacing;
    private int smallestLineSpacing = 9999999;

    public ArticWord(int index, int lineIndex,
                     Element element, Element previousElement, Double averagePageFontSize,
                     String alignment, String previousAlignment, Element lineElement,
                     Element previousLineElement, Element sectionElement) {
        super(index, element, previousElement, averagePageFontSize, alignment, previousAlignment);
        this.lineIndex = lineIndex;
        this.lineElement = lineElement;
        this.previousLineElement = previousLineElement;
        this.sectionElement = sectionElement;
    }

    public String getCharacterSize() {
        String characterSize = "zero";
        int length = getOriginalTextNoSpace().length();
        if (length >= 1 && length < 5) {
            characterSize = "few";
        } else if (length >= 5 && length < 10) {
            characterSize = "medium";
        } else if (length >= 10) {
            characterSize = "many";
        }

        return characterSize;
    }

    public boolean isPossibleEmail() {
        return EMAIL_PATTERN.matcher(getOriginalText().toLowerCase()).find();
    }

    public boolean isPossibleUniversity() {
        return UNIVERSITY_PATTERN.matcher(getOriginalTextNoSpaceOnlyLowerCaseText()).find();
    }

    public boolean isCountry() {
        return countryList.contains(getOriginalTextNoSpaceOnlyLowerCaseText());
    }

    public boolean isMonth() {
        return monthList.contains(getOriginalTextNoSpaceOnlyLowerCaseText());
    }

    public boolean isPossibleConferenceName() {
        boolean isConferenceName = false;

        for(String conference : conferenceList) {
           if(conference.equals(getOriginalTextNoSpace().replaceAll("[^a-zA-Z]", ""))) {
               isConferenceName = true;
               break;
           }
        }

        return (CONFERENCE_NAME_PATTERN.matcher(getOriginalTextNoSpace()).matches() || isConferenceName ||
                (getOriginalTextNoSpace().length() <= 5 && FileUtils.isUpperCase(getOriginalTextNoSpace()))) && !isCountry();
    }

    public boolean isPossibleConference() {
        String text = getOriginalTextNoSpace().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return "conference".equals(text) || "conf".equals(text);
    }


    public boolean isPossibleDepartment() {
        return DEPARTMENT_PATTERN.matcher(getOriginalTextNoSpaceOnlyLowerCaseText()).find();
    }

    public boolean isWebsite() {
        return WEBSITE_PATTERN.matcher(getOriginalTextNoSpace()).find();
    }

    public boolean isDays() {
        String text = getOriginalTextNoSpace();
        for (String month : monthList) {
            if (text.contains(month)) {
                text = text.replaceAll(month, "");
                break;
            }

        }
        return DAYS_PATTERN.matcher(text).matches();
    }

    public boolean isYear() {
        boolean year = false;
        String wordClean = getOriginalTextNoSpace().replaceAll("c?", "").replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        if (isNumberOnly() && wordClean.length() == 4) {
            Integer possibleYear = Integer.valueOf(wordClean);

            if (possibleYear > 1850 && possibleYear <= CURRENT_YEAR) {
                year = true;
            }
        }
        return year;
    }

    public boolean isPossibleAffiliation() {
        return isPossibleUniversity() || isCountry() || isPossibleDepartment() || isPossibleContinentOrOcean();
    }

    public boolean isPossibleEmailApart() {
        return (getOriginalTextNoSpace().trim().startsWith("{") || getOriginalTextNoSpace().trim().startsWith("[")) &&
                (getOriginalTextNoSpace().trim().endsWith(",") || getOriginalTextNoSpace().trim().endsWith(";"));
    }

    @Override
    public String getFontSize() {
        String fontSizeString = "normal";
        if (lineElement.getAttribute("fontSize") != null && !lineElement.getAttribute("fontSize").isEmpty()) {
            double fontSize = Double.valueOf(lineElement.getAttribute("fontSize").replaceAll(",", "\\."));
            fontSizeString = getFontSizeString(fontSizeString, fontSize);
        }

        return fontSizeString;
    }

    @Override
    public String getPreviousFontSize() {
        String fontSizeString = "normal";
        if (previousLineElement != null && previousLineElement.getAttribute("fontSize") != null && !previousLineElement.getAttribute("fontSize").isEmpty()) {
            double fontSize = Double.valueOf(previousLineElement.getAttribute("fontSize").replaceAll(",", "\\."));
            fontSizeString = getFontSizeString(fontSizeString, fontSize);
        }

        return fontSizeString;
    }

    public boolean isPossibleContinentOrOcean() {
        return CONTINENT_OCEANS_PATTERN.matcher(getOriginalTextNoSpaceOnlyLowerCaseText()).find();
    }

    public Integer getRight() {
        return Integer.valueOf(element.getAttribute("r"));
    }

    public Integer getLeft() {
        return Integer.valueOf(element.getAttribute("l"));
    }

    public Integer getBottom() {
        return Integer.valueOf(element.getAttribute("b"));
    }

    public Integer getTop() {
        return Integer.valueOf(element.getAttribute("t"));
    }

    public String getTextWithNoSuffix() {
        NodeList possibleRuns = element.getElementsByTagName("run");

        if (possibleRuns.getLength() > 0) {
            String text = possibleRuns.item(0).getTextContent();
            return (text != null && !text.isEmpty()) ? text.trim().replaceAll("\\s+", " ") : "";
        } else {
            return getOriginalText();
        }
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public int getBiggestLineSpacing() {
        if (biggestLineSpacing == 0) {
            int previousRight = 0;
            for (Element currentWord : FileUtils.getElementsByTagName("wd", sectionElement)) {
                int right = Integer.valueOf(currentWord.getAttribute("r"));
                if (previousRight > 0) {

                    int left = Integer.valueOf(currentWord.getAttribute("l"));

                    long currentDifference = left - previousRight;
                    if (currentDifference > 0 && currentDifference > biggestLineSpacing) {
                        biggestLineSpacing = (int) currentDifference;
                    }
                }

                previousRight = right;
            }
        }
        return biggestLineSpacing;
    }

    public int getSmallestLineSpacing() {
        if (smallestLineSpacing == 9999999) {
            int previousRight = 0;
            for (Element currentWord : FileUtils.getElementsByTagName("wd", sectionElement)) {
                int right = Integer.valueOf(currentWord.getAttribute("r"));
                if (previousRight > 0) {

                    int left = Integer.valueOf(currentWord.getAttribute("l"));

                    long currentDifference = left - previousRight;
                    if (currentDifference > 0 && currentDifference < smallestLineSpacing) {
                        smallestLineSpacing = (int) currentDifference;
                    }
                }

                previousRight = right;
            }
        }
        return smallestLineSpacing;
    }
}
