package br.ufrgs.artic.parser;

import br.ufrgs.artic.exceptions.ArticRunnerException;
import br.ufrgs.artic.exceptions.OmniPageParsingException;
import br.ufrgs.artic.model.*;
import br.ufrgs.artic.utils.DynamicProgramming;
import org.apache.commons.io.FileDeleteStrategy;
import org.w3c.dom.Element;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ArticParser extends OmniPageParser {

    //all the artic lines of the given
    private List<ArticLine> articLines;

    //page settings
    private Double averagePageFontSize;
    private int topBucketSize;
    private int leftBucketSize;

    private static final Pattern SEPARATOR_SIMPLE_PATTERN = Pattern.compile(",|;");

    /**
     * Default constructor to build the dom xml Document.
     *
     * @param pathToXML the physical path to the XML file
     * @throws br.ufrgs.artic.exceptions.OmniPageParsingException
     *          if any problem occurs during XML parsing process
     */
    public ArticParser(String pathToXML) throws OmniPageParsingException, IOException {
        super(pathToXML);
        setPageSettings();
    }

    /**
     * This method creates a list of artic lines representing the omnipage XML document.
     *
     * @return the list of artic lines from the omnipage document
     */
    public List<ArticLine> getArticLines() {

        if (articLines == null) {
            articLines = new ArrayList<ArticLine>();

            List<Element> paragraphs = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("para", omniPageXMLDocument.getDocumentElement());

            Integer lineCounter = 0;
            Boolean foundIntroOrAbstract = false;
            String previousAlignment = null;
            Element previousElement = null;
            if (paragraphs != null && !paragraphs.isEmpty()) {

                for (Element paragraphElement : paragraphs) {

                    String alignment = getAlignment(paragraphElement);

                    String paragraph = "new";

                    List<Element> linesOfParagraph = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("ln", paragraphElement);

                    if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                        for (Element lineElement : linesOfParagraph) {
                            ArticLine articLine = new ArticLine(lineCounter, lineElement, previousElement,
                                    averagePageFontSize, alignment, previousAlignment, topBucketSize, leftBucketSize);
                            articLines.add(articLine);

                            String textContent = articLine.getOriginalText();

                            if (textContent != null && Pattern.compile("intro|abstract", Pattern.CASE_INSENSITIVE).
                                    matcher(textContent).find()) {
                                foundIntroOrAbstract = true;
                            }

                            if (!foundIntroOrAbstract) {  //special case for headers
                                articLine.setParagraph("header");
                            } else {
                                articLine.setParagraph(paragraph);
                            }

                            paragraph = "same";
                            previousElement = lineElement;
                            previousAlignment = alignment;
                            lineCounter++;
                        }
                    }
                }
            }
        }

        return articLines;
    }

    public List<ArticWord> getArticHeaders(int fromIndex, int toIndex) {

        List<ArticWord> articWords = new ArrayList<ArticWord>();

        List<Element> lines = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("ln", omniPageXMLDocument.getDocumentElement());

        int currentLineIndex = 0;
        Element previousElement = null;
        Element previousLine = null;
        String previousAlignment = null;
        for (Element currentLine : lines) {
            String alignment = getAlignment((Element) currentLine.getParentNode());

            if (currentLineIndex >= fromIndex && currentLineIndex <= toIndex) {
                List<Element> words = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("wd", currentLine);
                int currentWordIndex = 0;
                for (Element currentWord : words) {
                    ArticWord articWord = new ArticHeader(currentWordIndex, currentLineIndex, currentWord,
                            previousElement, averagePageFontSize, alignment, previousAlignment, currentLine, previousLine, (Element) currentLine.getParentNode().getParentNode().getParentNode());
                    articWords.add(articWord);
                    previousElement = currentWord;
                    previousLine = currentLine;
                    previousAlignment = alignment;
                    currentWordIndex++;
                }

            } else if (currentLineIndex > toIndex) {
                break;
            }
            currentLineIndex++;
        }

        return articWords;
    }

    public List<ArticWord> getArticAuthorInformationList(Set<Integer> authorKeys, Integer toIndex) {

        List<ArticWord> articWords = new ArrayList<ArticWord>();

        List<Element> paragraphs = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("para", omniPageXMLDocument.getDocumentElement());

        if (paragraphs != null && !paragraphs.isEmpty()) {
            String previousAlignment = null;
            Element previousElement = null;
            Element previousLine = null;
            int totalLineIndex = 0;
            int paragraphIndex = 0;
            for (Element paragraphElement : paragraphs) {
                if (totalLineIndex > toIndex) {
                    break;
                }

                //default is left
                String alignment = getAlignment(paragraphElement);

                List<Element> linesOfParagraph = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("ln", paragraphElement);

                if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                    int currentLineIndex = 0;
                    for (Element currentLine : linesOfParagraph) {
                        if (authorKeys == null || authorKeys.contains(totalLineIndex)) {
                            List<Element> words = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("wd", currentLine);
                            int currentWordIndex = 0;
                            ArticAuthorInformation articWord;
                            for (Element currentWord : words) {

                                articWord = new ArticAuthorInformation(currentWordIndex, currentLineIndex, currentWord,
                                        previousElement, averagePageFontSize, alignment, previousAlignment, currentLine, previousLine, paragraphIndex, (Element) currentLine.getParentNode().getParentNode().getParentNode());
                                articWords.add(articWord);
                                previousElement = currentWord;
                                previousLine = currentLine;
                                previousAlignment = alignment;
                                currentWordIndex++;

                            }
                        } else if ((totalLineIndex > toIndex)) {
                            break;
                        }

                        currentLineIndex++;
                        totalLineIndex++;
                    }
                }

                paragraphIndex++;
            }
        }

        return articWords;
    }

    private String getAlignment(Element paragraphElement) {
        //default is left
        String alignment = "left";
        String alignmentOCR = paragraphElement.getAttribute("alignment");
        if (alignmentOCR != null && !alignmentOCR.isEmpty()) {
            alignment = alignmentOCR;
        }
        return alignment;
    }

    public List<ArticWord> getArticFooters(Set<Integer> footerKeys, Integer toIndex) {

        List<ArticWord> articWords = new ArrayList<ArticWord>();
        List<Element> paragraphs = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("para", omniPageXMLDocument.getDocumentElement());

        if (paragraphs != null && !paragraphs.isEmpty()) {
            Element previousWord = null;
            Element previousLine = null;
            String previousAlignment = null;
            int currentLineTotalIndex = 0;
            for (Element paragraphElement : paragraphs) {

                //default is left
                String alignment = getAlignment(paragraphElement);

                List<Element> linesOfParagraph = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("ln", paragraphElement);

                if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                    int currentLineIndex = 0;
                    for (Element currentLine : linesOfParagraph) {
                        if (footerKeys == null || footerKeys.contains(currentLineTotalIndex)) {
                            List<Element> words = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("wd", currentLine);
                            int currentWordIndex = 0;

                            for (Element currentWord : words) {

                                articWords.add(new ArticFooter(currentWordIndex, currentLineIndex, currentWord, previousWord,
                                        averagePageFontSize, alignment, previousAlignment, currentLine, previousLine, (Element) currentLine.getParentNode().getParentNode().getParentNode()));

                                previousWord = currentWord;
                                previousLine = currentLine;
                                previousAlignment = alignment;
                                currentWordIndex++;
                            }
                        } else if (currentLineIndex > toIndex) {
                            break;
                        }

                        currentLineIndex++;
                        currentLineTotalIndex++;
                    }
                }
            }
        }

        return articWords;
    }

    /**
     * This method sets the minimum page top, minimum page left and average font size.
     * It could be used in the future to normalize these properties in the final output.
     */
    private void setPageSettings() {

        List<Element> paragraphs = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("para", omniPageXMLDocument.getDocumentElement());

        int lineCounter = 0;
        int fontSizeSum = 0;
        if (paragraphs != null && !paragraphs.isEmpty()) {

            int biggestTop = 0;
            int biggestLeft = 0;
            for (Element paragraphElement : paragraphs) {

                List<Element> linesOfParagraph = br.ufrgs.artic.utils.FileUtils.getElementsByTagName("ln", paragraphElement);

                if (linesOfParagraph != null && !linesOfParagraph.isEmpty()) {
                    for (Element lineElement : linesOfParagraph) {
                        String fontFace = lineElement.getAttribute("fontFace");

                        if (fontFace == null || fontFace.isEmpty()) { //if yes, start run merge process
                            augmentLineElement(lineElement);
                        }

                        if (lineElement.getAttribute("t") != null && !lineElement.getAttribute("t").isEmpty() &&
                                lineElement.getAttribute("l") != null && !lineElement.getAttribute("l").isEmpty()) {
                            int top = Integer.parseInt(lineElement.getAttribute("t").replaceAll(",", "\\."));

                            if (top > biggestTop) {
                                biggestTop = top;
                            }

                            int left = Integer.parseInt(lineElement.getAttribute("l").replaceAll(",", "\\."));

                            if (left > biggestLeft) {
                                biggestLeft = left;
                            }
                        }

                        double fontSize = 0;
                        if (lineElement.getAttribute("fontSize") != null && !lineElement.getAttribute("fontSize").isEmpty()) {
                            fontSize = Double.valueOf(lineElement.getAttribute("fontSize").replaceAll(",", "\\."));
                        }


                        fontSizeSum += fontSize;
                        lineCounter++;
                    }
                }
            }

            averagePageFontSize = (double) fontSizeSum / lineCounter;

            topBucketSize = (biggestTop / 8) + 1;
            leftBucketSize = (biggestLeft / 8) + 1;
        } else {
            averagePageFontSize = null;
        }


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

        ArticParser articParser = new ArticParser(pathToXML);
        List<ArticLine> articLines = articParser.getArticLines();
        List<ArticWord> headerWords = articParser.getArticHeaders(0, 8);
        List<ArticWord> authorInformationWords = articParser.getArticAuthorInformationList(null, 999999);
        List<ArticWord> footerWords = articParser.getArticFooters(null, 999999);

        StringBuilder lineResult = new StringBuilder();
        for (ArticLine articLine : articLines) {
            lineResult.append(articLine.toString()).append("\n");
        }

        StringBuilder headerResult = new StringBuilder();
        for (ArticWord headerWord : headerWords) {
            headerResult.append(headerWord.toString()).append("\n");
        }

        StringBuilder authorInformationResult = new StringBuilder();
        for (ArticWord authorInformationWord : authorInformationWords) {
            authorInformationResult.append(authorInformationWord.toString()).append("\n");
        }

        StringBuilder footerResult = new StringBuilder();
        for (ArticWord footerWord : footerWords) {
            footerResult.append(footerWord.toString()).append("\n");
        }

        File xmlFile = new File(pathToXML);
        String lineFileName = xmlFile.getName().replace(".xml", ".artic.crf");
        String headerFileName = xmlFile.getName().replace(".xml", ".artic.header.crf");
        String authorInformationFileName = xmlFile.getName().replace(".xml", ".artic.author.information.crf");
        String footerFileName = xmlFile.getName().replace(".xml", ".artic.footer.crf");

        String parentPath = xmlFile.getParent();
        if (args.length > 1 && args[1] != null && !args[1].isEmpty()) {
            parentPath = args[1];
        }
        String outputFilePathLine = new File(parentPath, lineFileName).getAbsolutePath();
        String outputFilePathHeader = new File(parentPath, headerFileName).getAbsolutePath();
        String outputFilePathAuthorInformation = new File(parentPath, authorInformationFileName).getAbsolutePath();
        String outputFilePathFooter = new File(parentPath, footerFileName).getAbsolutePath();

        File lineFile = new File(outputFilePathLine);

        if (!lineFile.exists()) {
            if (!lineFile.createNewFile()) System.out.println("Could not create " + lineFile.getAbsolutePath());
        }

        File headerFile = new File(outputFilePathHeader);

        if (!headerFile.exists()) {
            if (!headerFile.createNewFile()) System.out.println("Could not create " + lineFile.getAbsolutePath());
        }

        File authorInformationFile = new File(outputFilePathAuthorInformation);

        if (!authorInformationFile.exists()) {
            if (!authorInformationFile.createNewFile())
                System.out.println("Could not create " + lineFile.getAbsolutePath());
        }

        File footerFile = new File(outputFilePathFooter);

        if (!footerFile.exists()) {
            if (!footerFile.createNewFile()) System.out.println("Could not create " + lineFile.getAbsolutePath());
        }

        FileWriter fw = new FileWriter(lineFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(lineResult.toString());
        bw.close();
        System.out.println(String.format("Processing of %s is done. CRF output has been written to %s", pathToXML, outputFilePathLine));

        fw = new FileWriter(headerFile.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        bw.write(headerResult.toString());
        bw.close();
        System.out.println(String.format("Processing of %s is done. CRF output has been written to %s", pathToXML, outputFilePathHeader));

        fw = new FileWriter(authorInformationFile.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        bw.write(authorInformationResult.toString());
        bw.close();
        System.out.println(String.format("Processing of %s is done. CRF output has been written to %s", pathToXML, outputFilePathAuthorInformation));

        fw = new FileWriter(footerFile.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        bw.write(footerResult.toString());
        bw.close();
        System.out.println(String.format("Processing of %s is done. CRF output has been written to %s", pathToXML, outputFilePathFooter));
    }


    public ArticMetadata getMetadata(String firstLevelModel, String headerModel, String authorInformationModel, String footnoteModel) throws IOException {

        ArticMetadata metadata = new ArticMetadata();

        String coreName = super.fileName + new Date().getTime() + ".crf";

        String firstLevelFile = getFile("first_level_" + coreName);
        try {
            outputArticResult(firstLevelFile, getArticLines());

            Map<Integer, String> firstLevelResult = getCRFResult(firstLevelFile, firstLevelModel);

            //title
            metadata.title = getOriginalContent(firstLevelResult, "TITLE", getArticLines());

            //header
            TreeMap<Integer, String> headerResult = getSecondLevelMap(firstLevelResult, "HEADER");
            if (!headerResult.isEmpty()) {
                putHeaderMetadata(metadata, headerResult, coreName, headerModel);
            }

            //preprocessor.secondLevel.footer
            TreeMap<Integer, String> footerResult = getSecondLevelMap(firstLevelResult, "FOOTNOTE");
            List<ArticWord> emailWordList = new ArrayList<ArticWord>();

            if (!footerResult.isEmpty()) {
                putFooterMetadata(metadata, footerResult, coreName, emailWordList, footnoteModel);
            }

            //author information
            TreeMap<Integer, String> authorInformationResult = getSecondLevelMap(firstLevelResult, "AUTHOR_INFORMATION");
            if (!authorInformationResult.isEmpty()) {
                putAuthorInformationMetadata(metadata, authorInformationResult, coreName, emailWordList, authorInformationModel);
            }


        } catch (IOException e) {
            throw new ArticRunnerException(e);
        } catch (InterruptedException e) {
            throw new ArticRunnerException(e);
        } finally {
            if (firstLevelFile != null) {
                FileDeleteStrategy.NORMAL.delete(new File(firstLevelFile));
            }
        }

        return metadata;
    }

    private void putAuthorInformationMetadata(ArticMetadata metadata, TreeMap<Integer,
            String> firstLevelAuthor, String coreName, List<ArticWord> emailWordList, String authorInformationModel) throws IOException, InterruptedException {
        Integer to = firstLevelAuthor.lastKey();

        String authorInformationFile = getFile("author_information_" + coreName);
        List<ArticWord> authorInformation = getArticAuthorInformationList(firstLevelAuthor.keySet(), to);
        outputArticResult(authorInformationFile, authorInformation);

        Map<Integer, String> authorInformationResult = getCRFResult(authorInformationFile, authorInformationModel);

        List<ArticWord> authors = new ArrayList<ArticWord>();
        List<ArticWord> affiliationWordList = new ArrayList<ArticWord>();
        for (Map.Entry<Integer, String> result : authorInformationResult.entrySet()) {
            if ("AUTHOR".equals(result.getValue())) {
                authors.add(authorInformation.get(result.getKey()));
            } else if ("EMAIL".equals(result.getValue())) {
                emailWordList.add(authorInformation.get(result.getKey()));
            } else if ("AFFILIATION".equals(result.getValue())) {
                ArticWord word = authorInformation.get(result.getKey());
                if (word.getOriginalText().contains("@")) {
                    emailWordList.add(word);
                } else {
                    affiliationWordList.add(word);
                }
            }
        }

        TreeMap<Integer, ArticBoundingBox> authorsMap = new TreeMap<Integer, ArticBoundingBox>();
        if (!authors.isEmpty()) {
            authorsMap = buildAuthorName(metadata, authors);
        }

        if (!affiliationWordList.isEmpty()) {

            buildAffiliation(metadata, affiliationWordList, authorsMap);
        }

        if (!emailWordList.isEmpty()) {
            List<String> emails = getEmailList(emailWordList);

            //removing duplicates
            HashSet<String> uniqueEmailSet = new LinkedHashSet<String>(emails);
            emails.clear();
            emails.addAll(uniqueEmailSet);

            if (metadata.authors != null && emails.size() == metadata.authors.size()) {
                int authorIndex = metadata.authors.size() - 1;
                for (ArticAuthor author : metadata.authors) {
                    author.email = emails.get(authorIndex--);
                }
            } else {
                buildEmail(metadata, emails);
            }

        }

        String text = getOriginalContent(authorInformationResult, "WEBSITE", authorInformation);
        if (text != null && !text.isEmpty()) {
            text = text.replaceAll("null", "");
            if (metadata.links != null) {
                metadata.links += text;
            } else {
                metadata.links = text;
            }
        }


    }

    private void buildAffiliation(ArticMetadata metadata, List<ArticWord> affiliationWordList, TreeMap<Integer, ArticBoundingBox> authorsMap) {

        TreeMap<Integer, ArticBoundingBox> affiliationsMap = getBoundingBoxElements(affiliationWordList, 17, 40);

        TreeMap<Integer, ArticBoundingBox> affiliationsGroup = regroupLines(affiliationsMap);

        if (affiliationsGroup.size() == 1) {
            metadata.addAffiliation(affiliationsGroup.get(0).getText());
        } else {
            List<String> visitedAffiliations = new ArrayList<String>();
            for (Map.Entry<Integer, ArticBoundingBox> currentAuthorEntry : authorsMap.entrySet()) {

                ArticBoundingBox authorBoundingBox = currentAuthorEntry.getValue();
                Integer index = getGroupIndex(affiliationsGroup.descendingMap(), authorBoundingBox, 17, 70, false);

                if (index != null) {
                    String affiliationText = affiliationsGroup.get(index).getText();
                    visitedAffiliations.add(affiliationText);

                    String authorNames = currentAuthorEntry.getValue().getText();
                    for (ArticAuthor author : metadata.authors) {
                        if (authorNames.contains(author.name)) {
                            author.affiliation = affiliationText;
                        }
                    }

                }
            }

            for (Map.Entry<Integer, ArticBoundingBox> currentAffiliationEntry : affiliationsGroup.entrySet()) {
                if (!visitedAffiliations.contains(currentAffiliationEntry.getValue().getText())) {
                    metadata.addAffiliation(currentAffiliationEntry.getValue().getText());
                }
            }
        }

    }

    private TreeMap<Integer, ArticBoundingBox> regroupLines(TreeMap<Integer, ArticBoundingBox> affiliationsMap) {

        List<Integer> toBeRemoved = new ArrayList<Integer>();
        for (Map.Entry<Integer, ArticBoundingBox> currentAffiliationEntry : affiliationsMap.descendingMap().entrySet()) {
            ArticBoundingBox affiliationBoundingBox = currentAffiliationEntry.getValue();

            Integer index = getGroupIndex(affiliationsMap, affiliationBoundingBox, 17, 40, false);

            if (index != null && !index.equals(currentAffiliationEntry.getKey()) && !toBeRemoved.contains(index)) {
                ArticBoundingBox mergingBoundingBox = affiliationsMap.get(index);
                if (!mergingBoundingBox.getText().equals(affiliationBoundingBox.getText())) {
                    mergingBoundingBox.words.addAll(affiliationBoundingBox.words);

                    int right = affiliationBoundingBox.dimension.getRight();
                    ArticDimension dimension = mergingBoundingBox.dimension;
                    if (right > dimension.getRight()) {
                        dimension.setRight(right);
                    }

                    int bottom = affiliationBoundingBox.dimension.getBottom();
                    if (bottom > dimension.getBottom()) {
                        dimension.setBottom(bottom);
                    }

                    int left = affiliationBoundingBox.dimension.getLeft();
                    if (left < dimension.getLeft()) {
                        dimension.setLeft(left);
                    }

                    int top = affiliationBoundingBox.dimension.getBottom();
                    if (top < dimension.getTop()) {
                        dimension.setTop(top);
                    }

                    toBeRemoved.add(currentAffiliationEntry.getKey());
                    toBeRemoved.remove(index);
                }

            } else {
                if (affiliationBoundingBox.words.size() <= 1) toBeRemoved.add(currentAffiliationEntry.getKey());
            }
        }

        TreeMap<Integer, ArticBoundingBox> resultMap = new TreeMap<Integer, ArticBoundingBox>();
        int index = 0;
        for (Integer currentIndex : affiliationsMap.keySet()) {
            if (!toBeRemoved.contains(currentIndex)) {
                resultMap.put(index++, affiliationsMap.get(currentIndex));
            }
        }

        return resultMap;
    }

    private List<String> getEmailList(List<ArticWord> emailWordList) {
        List<String> emails = new ArrayList<String>();

        String suffix = "";
        for (int i = emailWordList.size() - 1; i >= 0; i--) {
            ArticWord word = emailWordList.get(i);

            String originalText = word.getOriginalText().replaceAll("[{}]", "");

            if (originalText.lastIndexOf(",") == originalText.length() - 1) {
                originalText = originalText.substring(0, originalText.length() - 1);
            }

            String[] possibleEmails = originalText.split("[,\\|]");
            for (int j = possibleEmails.length - 1; j >= 0; j--) {
                String currentEmail = possibleEmails[j];
                String prefix = currentEmail;
                if (currentEmail.contains("@")) {
                    String[] emailSplit = currentEmail.split("@");
                    prefix = emailSplit[0];

                    if (emailSplit.length > 1) {
                        suffix = emailSplit[1].toLowerCase().trim();
                    }

                }


                if (originalText.length() + suffix.length() > 3 && !prefix.isEmpty()) {
                    emails.add(prefix.trim() + "@" + suffix);
                }
            }


        }

        return emails;
    }

    private void buildEmail(ArticMetadata metadata, List<String> emails) {

        for (String email : emails) {

            String prefix = email.split("@")[0];

            if (metadata.authors != null) {
                boolean foundEmail = false;
                for (ArticAuthor author : metadata.authors) {
                    String[] names = author.name.split(" ");

                    String stringLeft = prefix.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();
                    String stringRight = author.name.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();

                    int distance = DynamicProgramming.distance(stringLeft, stringRight);

                    int distanceRelative = (distance * 100) / (stringLeft.length() + stringRight.length());

                    for (String name : names) {
                        stringRight = name.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();
                        int distanceSplit = DynamicProgramming.distance(stringLeft, stringRight);

                        int distanceSplitRelative = (distanceSplit * 100) / (stringLeft.length() + stringRight.length());
                        if (distanceSplitRelative <= 20 || distanceRelative <= 20) {
                            author.email = email.trim().toLowerCase();
                            foundEmail = true;
                            break;
                        }

                    }
                    if (foundEmail) {
                        break;
                    }
                }

                if (!foundEmail) { //adding as generic email
                    addEmailToMetadata(metadata, email);
                }
            } else {  //adding to generic emails
                addEmailToMetadata(metadata, email);
            }

        }
    }

    private void addEmailToMetadata(ArticMetadata metadata, String email) {
        if (metadata.emails == null) {
            metadata.emails = new ArrayList<String>();
        }

        String emailContent = email.trim().toLowerCase();
        if(!emailContent.toLowerCase().contains("email:")) {
            metadata.emails.add(email.trim().toLowerCase());
        }

    }

    private TreeMap<Integer, ArticBoundingBox> buildAuthorName(ArticMetadata metadata, List<ArticWord> authors) {

        TreeMap<Integer, ArticBoundingBox> authorsMap = getBoundingBoxElements(authors, 20, 33);

        for (Map.Entry<Integer, ArticBoundingBox> currentAuthorEntry : authorsMap.entrySet()) {

            StringBuilder authorName = new StringBuilder();
            ArticBoundingBox articBoundingBox = currentAuthorEntry.getValue();
            for (ArticWord currentName : articBoundingBox.words) {
                String originalContent = currentName.getOriginalText();

                if (!originalContent.toLowerCase().equals("and")) {
                    String content = currentName.getTextWithNoSuffix().replaceAll("[^a-zA-Z\\-'\\.]", "");
                    if (content.length() > 1) {
                        authorName.append(content).append(" ");
                    }

                    if (SEPARATOR_SIMPLE_PATTERN.matcher(originalContent).find()) {
                        addNameToMetadata(metadata, authorName);
                        authorName = new StringBuilder();
                    }
                } else {
                    if (authorName.length() != 0) {
                        addNameToMetadata(metadata, authorName);
                        authorName = new StringBuilder();
                    }
                }
            }

            if (authorName.length() != 0) {
                addNameToMetadata(metadata, authorName);
            }

        }

        return authorsMap;
    }

    private void addNameToMetadata(ArticMetadata metadata, StringBuilder authorName) {
        if (metadata.authors == null) {
            metadata.authors = new ArrayList<ArticAuthor>();
        }
        ArticAuthor author = new ArticAuthor();
        author.name = authorName.toString().trim();
        metadata.authors.add(author);
    }

    private TreeMap<Integer, ArticBoundingBox> getBoundingBoxElements(List<ArticWord> words, int horizontalBoundary, int verticalBoundary) {

        TreeMap<Integer, ArticBoundingBox> groupMap = new TreeMap<Integer, ArticBoundingBox>();

        for (ArticWord word : words) {
            ArticBoundingBox articBoundingBox = getArticBoundingBox(word);
            Integer index = getGroupIndex(groupMap.descendingMap(), articBoundingBox, horizontalBoundary, verticalBoundary, false);

            if (index == null) {
                int size = groupMap.size();
                groupMap.put(size, articBoundingBox);

            } else {
                ArticBoundingBox currentBoundingBox = groupMap.get(index);
                currentBoundingBox.words.add(word);

                int right = word.getRight();
                articBoundingBox.dimension = currentBoundingBox.dimension;
                if (right > articBoundingBox.dimension.getRight()) {
                    articBoundingBox.dimension.setRight(right);
                }

                int bottom = word.getBottom();
                if (bottom > articBoundingBox.dimension.getBottom()) {
                    articBoundingBox.dimension.setBottom(bottom);
                }

                int left = word.getLeft();
                if (left < articBoundingBox.dimension.getLeft()) {
                    articBoundingBox.dimension.setLeft(left);
                }

                int top = word.getBottom();
                if (top < articBoundingBox.dimension.getTop()) {
                    articBoundingBox.dimension.setTop(top);
                }
            }
        }
        return groupMap;
    }

    private ArticBoundingBox getArticBoundingBox(ArticWord word) {
        List<ArticWord> list = new ArrayList<ArticWord>();
        list.add(word);
        ArticBoundingBox articBoundingBox = new ArticBoundingBox();
        articBoundingBox.words = list;
        articBoundingBox.dimension = new ArticDimension(word.getLeft(),
                word.getRight(), word.getTop(), word.getBottom());
        return articBoundingBox;
    }

    private Integer getGroupIndex(Map<Integer, ArticBoundingBox> groupMap, ArticBoundingBox boundingBox,
                                  int horizontalBoundary, int verticalBoundary, boolean validateParagraph) {
        Integer index = null;

        if (groupMap != null && !groupMap.isEmpty()) {

            for (Integer currentIndex : groupMap.keySet()) {
                int left = boundingBox.dimension.getLeft();
                int right = boundingBox.dimension.getRight();
                int top = boundingBox.dimension.getTop();
                int bottom = boundingBox.dimension.getBottom();

                ArticBoundingBox currentBoundingBox = groupMap.get(currentIndex);
                int biggestLineSpacing = currentBoundingBox.words.get(0).getBiggestLineSpacing();
                int smallestLineSpacing = currentBoundingBox.words.get(0).getSmallestLineSpacing();
                ArticDimension currentDimension = currentBoundingBox.dimension;
                long diff = (top - currentDimension.getTop());
                long topSpacing = Math.round((double) (1000 * diff) / (top + currentDimension.getTop()));
                if (diff < 0) {
                    topSpacing *= -1;
                }
                if (topSpacing <= 13) {
                    if (validateHorizontal(boundingBox, horizontalBoundary, left,
                            currentBoundingBox, currentDimension, biggestLineSpacing, smallestLineSpacing))
                        return currentIndex;
                } else {
                    diff = (bottom - currentDimension.getTop());
                    long verticalSpacingLeft = Math.round((double) (1000 * diff) / (currentDimension.getTop() + bottom));

                    diff = (top - currentDimension.getBottom());
                    long verticalSpacingRight = Math.round((double) (1000 * diff) / (top + currentDimension.getBottom()));

                    if (verticalSpacingLeft < 0) {
                        verticalSpacingLeft *= -1;
                    }

                    if (verticalSpacingRight < 0) {
                        verticalSpacingRight *= -1;
                    }

                    if (verticalSpacingLeft < verticalBoundary || verticalSpacingRight < verticalBoundary) {
                        if (!validateParagraph || validateHorizontal(boundingBox, horizontalBoundary, left, currentBoundingBox, currentDimension, biggestLineSpacing, smallestLineSpacing)) {
                            if ((left + (left / 100) + ((left * 5) / 100) >= currentDimension.getLeft() && right - (right / 100) - ((right * 5) / 100) <= currentDimension.getRight()) ||
                                    (currentDimension.getLeft() >= left - (left / 100) - ((left * 5) / 100) && currentDimension.getRight() <= right + (right / 100) + ((right * 5) / 100))) {
                                return currentIndex;
                            }
                        }

                    }
                }

            }
        }
        return index;
    }

    private boolean validateHorizontal(ArticBoundingBox boundingBox,
                                       int horizontalBoundary, int left,
                                       ArticBoundingBox currentBoundingBox, ArticDimension currentDimension,
                                       int biggestLineSpacing, int smallestLineSpacing) {
        long diff;
        long horizontalSpacing = Math.round((double) (1000 * (left - currentDimension.getRight())) / (currentDimension.getRight() + left));
        diff = horizontalSpacing - horizontalBoundary;
        if (diff < 0) {
            diff *= -1;
        }

        if (horizontalSpacing < 0) {
            horizontalSpacing *= -1;
        }

        long differenceToLineSpacing = 0;
        if (biggestLineSpacing > 0) {
            differenceToLineSpacing = Math.round((double) (100 * (left - currentDimension.getRight())) / biggestLineSpacing);
        }

        long spacingVariation = 0;
        if (biggestLineSpacing > 0) {
            spacingVariation = Math.round((double) (100 * (biggestLineSpacing - smallestLineSpacing)) / (biggestLineSpacing + smallestLineSpacing));
        }

        return ((horizontalSpacing <= horizontalBoundary)
                || ((boundingBox.getText().length() <= 4 || currentBoundingBox.getText().length() <= 4) && diff <= 5))
                && (differenceToLineSpacing <= 95 || spacingVariation < 35);
    }

    private TreeMap<Integer, String> getSecondLevelMap(Map<Integer, String> firstLevelResult, String clazz) {
        TreeMap<Integer, String> headerResult = new TreeMap<Integer, String>();
        for (Map.Entry<Integer, String> currentEntry : firstLevelResult.entrySet()) {
            if (currentEntry.getValue().equals(clazz)) {
                headerResult.put(currentEntry.getKey(), currentEntry.getValue());
            }
        }
        return headerResult;
    }

    private void putHeaderMetadata(ArticMetadata metadata, TreeMap<Integer, String> firstLevelHeader, String coreName, String headerModel) throws IOException, InterruptedException {

        Integer from = firstLevelHeader.firstKey();
        Integer to = firstLevelHeader.lastKey();

        String headerFile = getFile("header_" + coreName);
        List<ArticWord> headers = getArticHeaders(from, to);
        outputArticResult(headerFile, headers);

        Map<Integer, String> headerResult = getCRFResult(headerFile, headerModel);

        buildJournal(metadata, headers, headerResult);
        buildConference(metadata, headers, headerResult);
        String website = getOriginalContent(headerResult, "WEBSITE", headers);

        if (website != null && !website.isEmpty()) {
            website = website.replaceAll("null", "");
            metadata.links = (metadata.links != null && !metadata.links.isEmpty()) ? "," + website : website;
        }

    }

    private void putFooterMetadata(ArticMetadata metadata, TreeMap<Integer, String> firstLevelFooter, String coreName, List<ArticWord> emailWordList, String footerModel) throws IOException, InterruptedException {

        Integer to = firstLevelFooter.lastKey();

        String footerFile = getFile("footer_" + coreName);
        List<ArticWord> footers = getArticFooters(firstLevelFooter.keySet(), to);
        outputArticResult(footerFile, footers);

        Map<Integer, String> footerResult = getCRFResult(footerFile, footerModel);

        for (Map.Entry<Integer, String> result : footerResult.entrySet()) {
            if ("EMAIL".equals(result.getValue())) {
                emailWordList.add(footers.get(result.getKey()));
            }
        }

        buildConference(metadata, footers, footerResult);
        String website = getOriginalContent(footerResult, "WEBSITE", footers);

        if (website != null && !website.isEmpty()) {
            website = website.replaceAll("null", "");
            metadata.links = (metadata.links != null && !metadata.links.isEmpty()) ? "," + website : website;
        }

    }

    private void buildConference(ArticMetadata metadata, List<ArticWord> results, Map<Integer, String> headerResult) {
        String conferenceName = getOriginalContent(headerResult, "CONFERENCE_NAME", results);
        String date = getOriginalContent(headerResult, "CONFERENCE_DATE", results);
        String publisher = getOriginalContent(headerResult, "PUBLISHER", results);
        String isbn = getOriginalContent(headerResult, "ISBN", results);
        String issn = getOriginalContent(headerResult, "ISSN", results);
        String doi = getOriginalContent(headerResult, "DOI", results);
        String conferenceYear = getOriginalContent(headerResult, "CONFERENCE_YEAR", results);
        String conferencePage = getOriginalContent(headerResult, "CONFERENCE_PAGE", results);
        String conferenceVolume = getOriginalContent(headerResult, "CONFERENCE_VOLUME", results);
        String conferenceNumber = getOriginalContent(headerResult, "CONFERENCE_NUMBER", results);
        String conferenceLocation = getOriginalContent(headerResult, "CONFERENCE_LOCATION", results);

        if (conferenceName != null || conferenceYear != null || conferencePage != null ||
                conferenceVolume != null || conferenceNumber != null
                || conferenceLocation != null || date != null
                || publisher != null || isbn != null
                || issn != null || doi != null) {
            if (metadata.venues == null) {
                metadata.venues = new ArrayList<ArticVenue>();
            }

            ArticVenue conference = new ArticVenue();

            if (conferenceName != null && !conferenceName.isEmpty()) {
                if (conferenceName.endsWith(",")) {
                    conferenceName = conferenceName.substring(0, conferenceName.length() - 1).trim();
                }

                if (!conferenceName.isEmpty()) {
                    conference.name = conferenceName;
                }

            }

            if (conference.year == null) {
                conference.year = (conferenceYear != null) ? conferenceYear.replaceAll("[^0-9]", "").trim().substring(0, 4) : conferenceYear;
            }

            if (conference.page == null) {
                conference.page = conferencePage;
            }

            if (conference.volume == null) {
             //  conference.volume = (conferenceVolume != null) ? conferenceVolume.replaceAll("[^0-9]", "") : conferenceVolume;
            }

            if (conference.number == null) {
                conference.number = (conferenceNumber != null) ? conferenceNumber.replaceAll("[^0-9]", "") : conferenceNumber;
            }

            if (conference.location == null) {
                if (conferenceLocation != null && !conferenceLocation.isEmpty()) {

                    if (conferenceLocation.endsWith(",") || conferenceLocation.endsWith(".")) {
                        conferenceLocation = conferenceLocation.substring(0, conferenceLocation.length() - 1).trim();
                    }

                    if (!conferenceLocation.isEmpty()) {
                        conference.location = conferenceLocation;
                    }

                }
            }

            if (conference.date == null) {
                conference.date = (date != null) ? date.replaceAll("[,|©|;|.]", "").trim() : date;
            }

            if (conference.isbn == null) {

                if (isbn != null && (isbn.endsWith(",") || isbn.endsWith("."))) {
                    isbn = isbn.substring(0, isbn.length() - 1).trim();
                }

                conference.isbn = (isbn != null) ? isbn.split("\\.\\.\\.")[0].split("/\\$")[0] : isbn;
            }

            if (conference.issn == null) {
                conference.issn = (issn != null) ? issn.split("/")[0] : issn;
            }

            if (conference.doi == null) {
                conference.doi = doi;
            }

            if (conference.publisher == null) {
                conference.publisher = (publisher != null) ? publisher.replaceAll("[;|\\.|,]", "").trim() : publisher;
            }

            metadata.venues.add(conference);
        }
    }

    private void buildJournal(ArticMetadata metadata, List<ArticWord> headers, Map<Integer, String> headerResult) {

        String journalName = getOriginalContent(headerResult, "JOURNAL_NAME", headers);
        String journalVolume = getOriginalContent(headerResult, "JOURNAL_VOLUME", headers);
        String journalYear = getOriginalContent(headerResult, "JOURNAL_YEAR", headers);
        String journalPage = getOriginalContent(headerResult, "JOURNAL_PAGE", headers);

        if (journalName != null || journalVolume != null || journalYear != null || journalPage != null) {
            if (metadata.venues == null) {
                metadata.venues = new ArrayList<ArticVenue>();
            }

            ArticVenue journal = new ArticVenue();

            journal.name = journalName;
            //journal.volume = (journalVolume != null) ? journalVolume.replaceAll("[^0-9]", "") : journalVolume;
            if (journalYear != null) {
                journalYear = journalYear.replaceAll("[^0-9]", "");
                if (journalYear.length() >= 4) {
                    journalYear = journalYear.substring(0, 4);
                }
                journal.year = journalYear;
            }

            journal.page = journalPage;

            metadata.venues.add(journal);
        }
    }

    private String getFile(String responseFile) throws IOException {
        File tempFolder = new File("/temp");
        if (!tempFolder.exists() && !tempFolder.createNewFile()) {
            return responseFile;
        } else {
            return new File(tempFolder, responseFile).getAbsolutePath();
        }
    }

    private String getOriginalContent(Map<Integer, String> result, String clazz, List<? extends ArticBase> contents) {
        StringBuilder contentBuilder = new StringBuilder();
        for (Map.Entry<Integer, String> currentEntry : result.entrySet()) {
            if (currentEntry.getValue().equals(clazz)) {
                contentBuilder.append(contents.get(currentEntry.getKey()).getOriginalText()).append(" ");
            }
        }
        String originalContent = contentBuilder.toString().trim().replaceAll("\\s+", " ");
        return (originalContent.isEmpty()) ? null : originalContent;
    }

    private TreeMap<Integer, String> getCRFResult(String outputFile, String model) throws IOException, InterruptedException {

        TreeMap<Integer, String> result = new TreeMap<Integer, String>();

        BufferedReader in = null;
        try {
            ProcessBuilder ps = new ProcessBuilder("crf_test", "-m", model, outputFile);

            Process pr = ps.start();

            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            int index = 0;
            while ((line = in.readLine()) != null) {
                String[] columns = line.split("\t");
                String clazz = columns[columns.length - 1];
                result.put(index++, clazz);
            }
            pr.waitFor();
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return result;
    }

    private void outputArticResult(String destination, List<? extends ArticBase> result) {
        StringBuilder lineResult = new StringBuilder();
        for (ArticBase base : result) {
            lineResult.append(base.toString()).append("\n");
        }

        BufferedWriter bw = null;
        try {
            FileWriter fw = new FileWriter(destination);
            bw = new BufferedWriter(fw);
            bw.write(lineResult.toString());
        } catch (IOException e) {
            throw new ArticRunnerException("An error occurred trying to output");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                System.err.println("Not able to close the buffer!");
            }
        }

    }
}
