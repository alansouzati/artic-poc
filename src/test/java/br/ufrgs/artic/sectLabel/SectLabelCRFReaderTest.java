package br.ufrgs.artic.sectLabel;

import br.ufrgs.artic.sectLabel.exceptions.SectLabelParsingException;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class SectLabelCRFReaderTest {

    @Test
    public void testSectLabelFeaturesMap() throws SectLabelParsingException {

        String sampleSectLabelOutput = "/preprocessor/firstLevel/sample_sectLabel_output.crf";
        Map<Integer,Set<String>> featuresMap = new SectLabelCRFReader(
                        getClass().getResource(sampleSectLabelOutput).getFile()).getFeaturesMap();

        assertNotNull(featuresMap);
        assertTrue(featuresMap.keySet().size() == 15);

    }
}
