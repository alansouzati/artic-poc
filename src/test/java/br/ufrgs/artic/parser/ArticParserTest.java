package br.ufrgs.artic.parser;

import br.ufrgs.artic.exceptions.OmniPageParsingException;
import br.ufrgs.artic.model.ArticLine;
import br.ufrgs.artic.model.ArticWord;
import br.ufrgs.artic.utils.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ArticParserTest {

    @Test
    public void testArticFirstLevelCRF() throws OmniPageParsingException, IOException {

        List<ArticLine> articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example1.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        StringBuilder result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example1.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example2.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example2.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example3.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example3.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example4.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example4.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example5.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example5.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example6_bullet.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example6.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example7_image.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example7.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example8_table.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example8.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articLines = new ArticParser(getClass().getResource("/preprocessor/firstLevel/example9_header.xml").getFile()).getArticLines();

        assertNotNull(articLines);

        result = new StringBuilder();
        for (ArticLine articLine : articLines) {
            result.append(articLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_example9.txt").getFile(), Charset.forName("UTF-8")), result.toString());
    }

    @Test
    public void testArticHeader() throws OmniPageParsingException, IOException {

        List<ArticWord> articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/header/ieee_header1.xml").getFile()).getArticHeaders(0, 8);

        StringBuilder result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/header/expected_ieee_header1.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/header/ieee_header2.xml").getFile()).getArticHeaders(0, 8);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/header/expected_ieee_header2.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/header/ieee_header3.xml").getFile()).getArticHeaders(0, 8);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/header/expected_ieee_header3.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/header/elsevier_header1.xml").getFile()).getArticHeaders(0, 8);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/header/expected_elsevier_header1.txt").getFile(), Charset.forName("UTF-8")), result.toString());
    }

    @Test
    public void testArticAuthorInformation() throws OmniPageParsingException, IOException {

        List<ArticWord> articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/authorInformation/ieee1.xml").getFile()).getArticAuthorInformationList(null, 99999);

        StringBuilder result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/authorInformation/expected_ieee1.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/authorInformation/ieee2.xml").getFile()).getArticAuthorInformationList(null, 999999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/authorInformation/expected_ieee2.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/authorInformation/ieee3.xml").getFile()).getArticAuthorInformationList(null, 999999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/authorInformation/expected_ieee3.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/authorInformation/ieee4.xml").getFile()).getArticAuthorInformationList(null, 999999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/authorInformation/expected_ieee4.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/authorInformation/ieee5.xml").getFile()).getArticAuthorInformationList(null, 999999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/authorInformation/expected_ieee5.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/authorInformation/elsevier1.xml").getFile()).getArticAuthorInformationList(null, 99999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/authorInformation/expected_elsevier1.txt").getFile(), Charset.forName("UTF-8")), result.toString());

    }

    @Test
    public void testArticFooter() throws OmniPageParsingException, IOException {
        List<ArticWord> articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/footer/example_acm1.xml").getFile()).getArticFooters(null, 999999);

        StringBuilder result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/footer/expected_acm1.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/footer/example_acm2.xml").getFile()).getArticFooters(null, 999999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/footer/expected_acm2.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/footer/example_elsevier1.xml").getFile()).getArticFooters(null, 999999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/footer/expected_elsevier1.txt").getFile(), Charset.forName("UTF-8")), result.toString());

        articWords = new ArticParser(getClass().getResource("/preprocessor/secondLevel/footer/example_ieee1.xml").getFile()).getArticFooters(null, 999999);

        result = new StringBuilder();
        for (ArticWord articWord : articWords) {
            result.append(articWord.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/secondLevel/footer/expected_ieee1.txt").getFile(), Charset.forName("UTF-8")), result.toString());


    }
}
