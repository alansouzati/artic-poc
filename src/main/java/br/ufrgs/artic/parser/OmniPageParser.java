package br.ufrgs.artic.parser;

import br.ufrgs.artic.exceptions.OmniPageParsingException;
import br.ufrgs.artic.utils.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This class is responsible for extracting information from a OmniPage XML.
 * Tables and images are completely ignored.
 */
public abstract class OmniPageParser {

    //handles the instance of the given xml file
    protected final Document omniPageXMLDocument;
    protected final String fileName;

    /**
     * Default constructor to build the dom xml Document.
     *
     * @param pathToXML the physical path to the XML file
     * @throws OmniPageParsingException if any problem occurs during XML parsing process
     */
    protected OmniPageParser(String pathToXML) throws OmniPageParsingException, IOException {

        File xmlFile = new File(pathToXML);
        this.fileName = xmlFile.getName().replace(".xml", "");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        InputStream inputStream = null;
        try {
            db = factory.newDocumentBuilder();

            inputStream = new FileInputStream(xmlFile);

            omniPageXMLDocument = db.parse(inputStream);

        } catch (ParserConfigurationException e) {
            throw new OmniPageParsingException(String.format("The content of %s is an invalid XML file.", pathToXML), e);
        } catch (SAXException e) {
            throw new OmniPageParsingException(String.format("The content of %s is an invalid XML file.", pathToXML), e);
        } catch (IOException e) {
            throw new OmniPageParsingException(String.format("Could not open %s. The does not exists or do not allow us to open.", pathToXML), e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

        }

    }

    /**
     * This augment the line element with the properties from the "run" child tags.
     *
     * @param lineElement the line element without the desired properties
     */
    protected void augmentLineElement(Element lineElement) {

        List<Element> runsOfLine = FileUtils.getElementsByTagName("run", lineElement);

        if (runsOfLine != null && !runsOfLine.isEmpty()) {
            Map<Double, Integer> fontSizeMapCount = new HashMap<Double, Integer>();
            for (Element currentRun : runsOfLine) {
                String textContent = currentRun.getTextContent().replaceAll(" ", "").replaceAll("\n", "");
                if (textContent.length() > 3) {
                    String lineBoldString = lineElement.getAttribute("bold");
                    String runBoldString = currentRun.getAttribute("bold");
                    if ((lineBoldString == null || lineBoldString.isEmpty())
                            && runBoldString != null && !runBoldString.isEmpty()
                            && Boolean.valueOf(runBoldString)) {
                        lineElement.setAttribute("bold", "true");
                    }

                    String lineItalicString = lineElement.getAttribute("italic");
                    String runItalicString = currentRun.getAttribute("italic");
                    if ((lineItalicString == null || lineItalicString.isEmpty())
                            && runItalicString != null && !runItalicString.isEmpty()
                            && Boolean.valueOf(runItalicString)) {
                        lineElement.setAttribute("italic", "true");
                    }

                    String lineUnderlineString = lineElement.getAttribute("underlined");
                    String runUnderlineString = currentRun.getAttribute("underlined");
                    if ((lineUnderlineString == null || lineUnderlineString.isEmpty()
                            || lineUnderlineString.equals("none")) &&
                            runUnderlineString != null && !runUnderlineString.isEmpty()
                            && !runUnderlineString.equals("none")) {
                        lineElement.setAttribute("underlined", runUnderlineString);
                    }

                    String lineFontFace = lineElement.getAttribute("fontFace");
                    String runFontFace = currentRun.getAttribute("fontFace");
                    if ((lineFontFace == null || lineFontFace.isEmpty()) && runFontFace != null && !runFontFace.isEmpty()) {
                        lineElement.setAttribute("fontFace", runFontFace);
                    }

                    String runFontSize = currentRun.getAttribute("fontSize");
                    if (runFontSize != null && !runFontSize.isEmpty()) {
                        Double currentFontSize = Double.valueOf(runFontSize);
                        Integer currentFontCount = fontSizeMapCount.containsKey(currentFontSize) ? fontSizeMapCount.get(currentFontSize) + 1 : 0;
                        fontSizeMapCount.put(currentFontSize, currentFontCount);
                    }

                    String lineFontFamily = lineElement.getAttribute("fontFamily");
                    String runFontFamily = currentRun.getAttribute("fontFamily");
                    if ((lineFontFamily == null || lineFontFamily.isEmpty()) && runFontFamily != null && !runFontFamily.isEmpty()) {
                        lineElement.setAttribute("fontFamily", runFontFamily);
                    }
                }
            }

            String lineFontSize = lineElement.getAttribute("fontSize");

            if ((lineFontSize == null || lineFontSize.isEmpty()) && !fontSizeMapCount.isEmpty()) {
                lineElement.setAttribute("fontSize", String.format("%.2f", getFontSize(fontSizeMapCount)));
            }

        }

    }

    private Double getFontSize(Map<Double, Integer> fontSizeMapCount) {

        Set<Map.Entry<Double, Integer>> fontSizeEntries = fontSizeMapCount.entrySet();
        Map.Entry<Double, Integer> bestEntry = fontSizeEntries.iterator().next();

        for (Map.Entry<Double, Integer> currentFontSizeEntry : fontSizeEntries) {

            if (currentFontSizeEntry.getValue() > bestEntry.getValue() ||
                    currentFontSizeEntry.getKey() < bestEntry.getKey()) {
                bestEntry = currentFontSizeEntry;
            }
        }

        return bestEntry.getKey();
    }


}
