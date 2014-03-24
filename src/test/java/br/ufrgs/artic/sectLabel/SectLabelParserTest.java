package br.ufrgs.artic.sectLabel;

import br.ufrgs.artic.exceptions.OmniPageParsingException;
import br.ufrgs.artic.utils.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SectLabelParserTest {

    @Test
    public void testXMLParsing() throws OmniPageParsingException, IOException {
        List<SectLabelLine> sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example1.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        StringBuilder result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example1.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example2.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example2.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example3.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example3.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example4.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example4.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example5.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example5.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example6_bullet.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example6.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example7_image.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example7.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example8_table.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example8.txt").getFile(), Charset.forName("UTF-8")),result.toString());

        sectLabelLines = new SectLabelParser(getClass().getResource("/preprocessor/firstLevel/example9_header.xml").getFile()).getSectLabelLines();

        assertNotNull(sectLabelLines);

        result = new StringBuilder();
        for(SectLabelLine sectLabelLine : sectLabelLines) {
            result.append(sectLabelLine.toString()).append("\n");
        }

        assertEquals(FileUtils.readFile(getClass().getResource("/preprocessor/firstLevel/expected_sectLabel_example9.txt").getFile(), Charset.forName("UTF-8")),result.toString());
    }
}
