package br.ufrgs.artic.sectLabel;

import br.ufrgs.artic.sectLabel.exceptions.SectLabelParsingException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class aims to provide the user with utility methods regarding the SectLabel project.
 */
public class SectLabelCRFReader {

    private final Map<Integer, Set<String>> featuresMap;

    public SectLabelCRFReader(String pathToSectLabelCRF) throws SectLabelParsingException {
        try {
            featuresMap = new HashMap<Integer, Set<String>>();

            FileInputStream sectLabelInputStream = new FileInputStream(URLDecoder.decode(pathToSectLabelCRF,"UTF-8"));
            BufferedReader sectLabelReader = new BufferedReader(new InputStreamReader(sectLabelInputStream, Charset.forName("UTF-8")));
            String currentSectLabelCRFLine;

            while ((currentSectLabelCRFLine = sectLabelReader.readLine()) != null) {

                String[] features = currentSectLabelCRFLine.split("\\s+");

                int index = 0;
                for(String currentFeature : features) {
                    if(index > 14) {
                        break;
                    }
                    if(featuresMap.containsKey(index)) {
                        featuresMap.get(index).add(currentFeature);
                    } else {
                        Set<String> featureValues = new HashSet<String>();
                        featureValues.add(currentFeature);
                        featuresMap.put(index,featureValues);
                    }

                    index++;
                }

            }
        } catch (IOException e) {
            throw new SectLabelParsingException(String.format("Could not read the %s file.",pathToSectLabelCRF),e);
        }
    }

    public Map<Integer,Set<String>> getFeaturesMap() {
        return featuresMap;
    }

    public static void main(String[] args) throws SectLabelParsingException {
        Map<Integer,Set<String>> featuresMap = new SectLabelCRFReader(args[0]).getFeaturesMap();
        featuresMap.remove(0);
        for(Integer currentFeature : featuresMap.keySet()) {
            System.out.println(currentFeature + ": " + featuresMap.get(currentFeature) + "\n");
        }
    }
}
