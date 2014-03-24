package br.ufrgs.artic.sectLabel;

import br.ufrgs.artic.parser.OmniPageParser;
import br.ufrgs.artic.exceptions.OmniPageParsingException;
import br.ufrgs.artic.utils.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is responsible for extracting information from a OmniPage XML according to
 * the sectlabel implementation.
 */
public class SectLabelParser extends OmniPageParser {

    private final Map<Integer,String> fontSizeMap = new HashMap<Integer, String>();
    private ArrayList<SectLabelLine> sectLabelLines;
    private String previousFontFace;
    private int pageBucketSize;

    /**
     * Default constructor to build the dom xml Document.
     *
     * @param pathToXML the physical path to the XML file
     * @throws br.ufrgs.artic.exceptions.OmniPageParsingException
     *          if any problem occurs during XML parsing process
     */
    public SectLabelParser(String pathToXML) throws OmniPageParsingException, IOException {
        super(pathToXML);

        setPageSettings();
    }

    /**
     * This method sets the font size occurrence map.
     */
    private void setPageSettings() {

        List<Element> paragraphs = FileUtils.getElementsByTagName("para", omniPageXMLDocument.getDocumentElement());

        Map<Integer,Integer> fontSizeOccurrenceMap = new HashMap<Integer, Integer>();

        if (paragraphs != null && !paragraphs.isEmpty()) {

            int biggestTop = 0;
            for (Element paragraphElement : paragraphs) {

                List<Element> linesOfParagraph = FileUtils.getElementsByTagName("ln", paragraphElement);

                if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                    for (Element lineElement : linesOfParagraph) {
                        String fontFace = lineElement.getAttribute("fontFace");

                        if (fontFace == null || fontFace.isEmpty()) { //if yes, start run merge process
                            augmentLineElement(lineElement);
                        }


                        int fontSize = 0;
                        if(lineElement.getAttribute("fontSize") != null && !lineElement.getAttribute("fontSize").isEmpty()) {
                            fontSize = (int) (Double.valueOf(lineElement.getAttribute("fontSize").replaceAll(",","\\.")) / 100);
                        }

                        if(fontSizeOccurrenceMap.containsKey(fontSize)) {
                            fontSizeOccurrenceMap.put(fontSize,fontSizeOccurrenceMap.get(fontSize)+1);
                        } else {
                            fontSizeOccurrenceMap.put(fontSize,1);
                        }

                        int top = Integer.parseInt(lineElement.getAttribute("t").replaceAll(",","\\."));

                        if(top > biggestTop) {
                          biggestTop = top;
                        }
                    }
                }
            }

            int baseFontSize = getBaseFontSize(fontSizeOccurrenceMap);

            buildFontSizeMap(fontSizeOccurrenceMap, baseFontSize);

            pageBucketSize = (biggestTop / 8) + 1;
        }
    }

    private void buildFontSizeMap(Map<Integer, Integer> fontSizeOccurrenceMap, int baseFontSize) {
        Stack<Integer> fontSizeStack = new Stack<Integer>();
        fontSizeStack.addAll(fontSizeOccurrenceMap.keySet());

        Collections.sort(fontSizeStack);

        boolean isBaseFontSize = false;
        int index = 0;
        while(!fontSizeStack.isEmpty() && !isBaseFontSize) {

            int fontSize = fontSizeStack.pop();

            if(fontSize != baseFontSize) {
                fontSizeMap.put(fontSize,"xmlFontSize_largest"+index--);
            } else {
               isBaseFontSize = true;
               fontSizeMap.put(fontSize,"xmlFontSize_common");
            }

        }

        if(!fontSizeStack.isEmpty()) {
            while(!fontSizeStack.isEmpty()) {
                fontSizeMap.put(fontSizeStack.pop(),"xmlFontSize_smaller");
            }
        }
    }

    private Integer getBaseFontSize(Map<Integer, Integer> fontSizeOccurrenceMap) {
        Integer baseFontSize = 0;
        Integer baseFontSizeOccurrence = 0;
        for(Integer currentFontSize : fontSizeOccurrenceMap.keySet()) {
             Integer currentFontSizeOccurrence = fontSizeOccurrenceMap.get(currentFontSize);
             if(currentFontSizeOccurrence > baseFontSizeOccurrence) {
                 baseFontSizeOccurrence = currentFontSizeOccurrence;
                 baseFontSize = currentFontSize;
             }
        }

        return baseFontSize;
    }

    public List<SectLabelLine> getSectLabelLines() {
        if (sectLabelLines == null) {
            sectLabelLines = new ArrayList<SectLabelLine>();

            List<Element> paragraphs = FileUtils.getElementsByTagName("para", omniPageXMLDocument.getDocumentElement());

            boolean foundIntroOrAbstract = false;
            String previousFormat = null;
            if (paragraphs != null && !paragraphs.isEmpty()) {

                for (Element paragraphElement : paragraphs) {

                    //default is left
                    String alignment = "left";
                    String alignmentOCR = paragraphElement.getAttribute("alignment");
                    if(alignmentOCR!=null && !alignmentOCR.isEmpty()) {
                        alignment = alignmentOCR;
                    }

                    String paragraph = "new";

                    List<Element> linesOfParagraph = FileUtils.getElementsByTagName("ln", paragraphElement);
                    List<Element> bulletElements = FileUtils.getElementsByTagName("bullet", paragraphElement);

                    boolean hasBulletInside = false;
                    if(bulletElements!=null && !bulletElements.isEmpty()) {
                      hasBulletInside = true;
                    }

                    boolean belongsToATable = false;
                    if("cell".equals(paragraphElement.getParentNode().getNodeName())) {
                        belongsToATable = true;
                    }

                    boolean belongsToAPicture = false;

                   if("picture".equals(getPreviousSiblingElementName(paragraphElement)) ||
                            "picture".equals(getNextSiblingElementName(paragraphElement))) {
                        belongsToAPicture = true;
                    }


                    if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                        for (Element lineElement : linesOfParagraph) {

                            SectLabelLine sectLabelLine = getSectLabelLineInstance(lineElement, alignment,previousFormat,hasBulletInside,belongsToATable,belongsToAPicture);

                            String textContent = sectLabelLine.getOriginalText();

                            if(textContent!=null && Pattern.compile("intro|abstract", Pattern.CASE_INSENSITIVE).matcher(textContent).find()) {
                                foundIntroOrAbstract = true;
                            }

                            if (!foundIntroOrAbstract) {  //special case for headers
                                sectLabelLine.setParagraph("xmlParagraph_header");
                            } else {
                                sectLabelLine.setParagraph(String.format("xmlParagraph_%s",paragraph));
                            }

                            paragraph = "continue";
                            previousFormat = sectLabelLine.getFontSize()+sectLabelLine.isBold()+sectLabelLine.isItalic()+sectLabelLine.getFontFace()+sectLabelLine.getAlignment();

                            sectLabelLines.add(sectLabelLine);
                        }
                    }
                }
            }
        }

        return sectLabelLines;
    }

    public static String getPreviousSiblingElementName(Node node) {
        Node prevSibling = node.getPreviousSibling();
        while (prevSibling != null) {
            if (prevSibling.getNodeType() == Node.ELEMENT_NODE) {
                return prevSibling.getNodeName();
            }
            prevSibling = prevSibling.getPreviousSibling();
        }

        return null;
    }

    public static String getNextSiblingElementName(Node node) {
        Node nextSibling = node.getNextSibling();
        while (nextSibling != null) {
            if (nextSibling.getNodeType() == Node.ELEMENT_NODE) {
                return nextSibling.getNodeName();
            }
            nextSibling = nextSibling.getNextSibling();
        }

        return null;
    }

    private SectLabelLine getSectLabelLineInstance(Element lineElement, String alignment, String previousFormat,
                                                   boolean hasBulletInside, boolean belongsToATable, boolean belongsToAPicture) {

        String originalText = lineElement.getTextContent().trim().replaceAll("\\s+", " ");

        SectLabelNumbers sectLabelNumbers = getSectLabelNumbers(originalText);

        SectLabelPunctuation sectLabelPunctuation = getSectLabelPunctuation(originalText);

        int top = Integer.parseInt(lineElement.getAttribute("t").replaceAll(",","\\."));
        int location = top / pageBucketSize;
        String xmlLocation = "xmlLocation_"+location;

        String fontFace = lineElement.getAttribute("fontFace");

        String boldString = lineElement.getAttribute("bold");
        boolean bold = false;
        if (boldString != null && !boldString.isEmpty()) {
            bold = Boolean.valueOf(boldString);
        }

        String italicString = lineElement.getAttribute("italic");
        boolean italic = false;
        if (italicString != null && !italicString.isEmpty()) {
            italic = Boolean.valueOf(italicString);
        }

        String fontSizeString = "xmlFontSize_none";
        if(lineElement.getAttribute("fontSize") != null && !lineElement.getAttribute("fontSize").isEmpty()) {
            int fontSize = (int) (Double.valueOf(lineElement.getAttribute("fontSize").replaceAll(",","\\.")) / 100);
            fontSizeString = fontSizeMap.get(fontSize);
        }

        if(fontFace == null || fontFace.isEmpty()) {
            fontFace = previousFontFace;
        }

        previousFontFace = fontFace;

        if(originalText==null || originalText.isEmpty()) {
            originalText = "undefined";
        }

        return new SectLabelLine(sectLabelNumbers,sectLabelPunctuation,xmlLocation,originalText,
                 bold, italic, fontSizeString, fontFace,alignment,previousFormat,hasBulletInside,belongsToATable,belongsToAPicture);
    }

    private SectLabelPunctuation getSectLabelPunctuation(String originalText) {
        SectLabelPunctuation sectLabelPunctuation = SectLabelPunctuation.LINE_PUNCT_OTHERS;
        if(originalText!=null) {
           Pattern endNumberingPattern = Pattern.compile("^.+?(\\[|\\(|\\{)\\d(\\]|\\)|\\})$");
           if(originalText.contains("http://") || originalText.contains("https://")) {
               sectLabelPunctuation = SectLabelPunctuation.POSSIBLE_WEB;
           } else if(originalText.contains("@")){
               sectLabelPunctuation = SectLabelPunctuation.POSSIBLE_EMAIL;
           } else if(endNumberingPattern.matcher(originalText).matches()) {
               sectLabelPunctuation = SectLabelPunctuation.END_NUMBERING;
           }
        }
        return sectLabelPunctuation;
    }

    private SectLabelNumbers getSectLabelNumbers(String originalText) {
        SectLabelNumbers sectLabelNumbers = SectLabelNumbers.NUMBERS_OTHERS;
        if(originalText!=null) {
            Pattern posSubsecPattern = Pattern.compile("\\d+.\\d+\\s*\\S*");
            Pattern posSubSubsecPattern = Pattern.compile("\\d+.\\d+.\\d+\\s*\\S*");
            Pattern posCategoriesPattern = Pattern.compile("\\S+.\\d+.\\d+\\s*\\S*");
            Pattern webFootnotePattern = Pattern.compile("\\d+\\s*(http|HTTP)(\\S|\\s)*");
            Pattern footnotePattern = Pattern.compile("\\d+\\s*(This|this|THIS)(\\S|\\s)*");

            if(posSubsecPattern.matcher(originalText).matches()) {
              sectLabelNumbers = SectLabelNumbers.NUMBERS_POS_SUBSEC;
           } else if(posSubSubsecPattern.matcher(originalText).matches()) {
               sectLabelNumbers = SectLabelNumbers.NUMBERS_POS_SUBSUBSEC;
           } else if(posCategoriesPattern.matcher(originalText).matches()) {
               sectLabelNumbers = SectLabelNumbers.NUMBERS_POS_CATEGORIES;
           } else if(webFootnotePattern.matcher(originalText).matches()) {
               sectLabelNumbers = SectLabelNumbers.NUMBERS_WEB_FOOTNOTE;
           } else if(footnotePattern.matcher(originalText).matches()) {
                sectLabelNumbers = SectLabelNumbers.NUMBERS_FOOTNOTE;
            }
        }
        return sectLabelNumbers;
    }

    public static void main(String[] args) throws OmniPageParsingException, IOException {

        String pathToXML = System.getProperty("user.dir");
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            pathToXML = args[0];
        }

        if (pathToXML.endsWith(".xml")) {
            run(args, pathToXML);
        } else {
            File dir = new File(pathToXML);
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            });

            for (File currentXML : files) {
                run(args, currentXML.getAbsolutePath());
            }

        }

    }

    private static void run(String[] args, String pathToXML) throws OmniPageParsingException, IOException {

        System.out.println(String.format("Processing XML file %s", pathToXML));
        List<SectLabelLine> sectLabelLines = new SectLabelParser(pathToXML).getSectLabelLines();

        StringBuilder result = new StringBuilder();
        for (SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        File xmlFile = new File(pathToXML);
        String fileName = xmlFile.getName().replace(".xml", ".sectLabel.crf");

        String parentPath = xmlFile.getParent();
        if (args.length > 1 && args[1] != null && !args[1].isEmpty()) {
            parentPath = args[1];
        }
        String outputFilePath = new File(parentPath, fileName).getAbsolutePath();

        File file = new File(outputFilePath);

        if (!file.exists()) {
           if(!file.createNewFile()) System.out.println("Could not create " + file.getAbsolutePath());
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(result.toString());
        bw.close();
        System.out.println(String.format("Processing of %s is done. CRF output has been written to %s", pathToXML, outputFilePath));
    }
}
