package br.ufrgs.artic.validation;

import br.ufrgs.artic.model.ArticMetadata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PrecisionCalculatorTest {

    private static final Gson parser = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testArticMetadata() throws IOException {

        String goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/1.artic").getFile()));

        ArticMetadata goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        String badExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/1_bad_f1.artic").getFile()));

        String goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/1_good_f1.artic").getFile()));

        ArticMetadata badExampleMetadata = parser.fromJson(badExampleJSON, ArticMetadata.class);
        ArticMetadata goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        Map<String, Double> precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, badExampleMetadata);
        System.out.println("Bad Example: " + precisionMap);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("Good Example: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/15.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/15.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("15.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/21.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/21.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("21.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/4.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/4.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("4.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/5.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/5.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("5.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/17.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/17.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("17.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/12.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/12.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("12.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/6.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/6.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("6.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/30.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/30.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("30.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/35.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/35.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("35.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/22.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/22.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("22.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/1.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/perfect1.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("Perfect 1.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/14.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/14.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("Perfect 14.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/sectLabel/27.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/27.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("Perfect 27.artic: "+ precisionMap);

        goldStandardJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/json/total/59.artic").getFile()));
        goldStandardMetadata = parser.fromJson(goldStandardJSON, ArticMetadata.class);

        goodExampleJSON = FileUtils.readFileToString(
                new File(getClass().getResource("/validation/wrong_json/59.artic").getFile()));
        goodExampleMetadata = parser.fromJson(goodExampleJSON, ArticMetadata.class);

        precisionMap = PrecisionCalculator.validateMetadata(goldStandardMetadata, goodExampleMetadata);
        System.out.println("Perfect 59.artic: "+ precisionMap);

    }
}
