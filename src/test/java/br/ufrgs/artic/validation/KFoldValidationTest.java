package br.ufrgs.artic.validation;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class KFoldValidationTest {

    @Test
    public void testFirstLevelCrossValidationSectLabel() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/firstLevel/sectLabel").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.firstLevelCrossValidation(Arrays.asList(firstLevelSet), "FIRST_LEVEL");
    }

    @Test
    public void testFirstLevelCrossValidationSectLabel2() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/firstLevel/sectLabel2").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.firstLevelCrossValidation(Arrays.asList(firstLevelSet), "SECTLABEL_FIRST_LEVEL");
    }

    @Test
    public void testFirstLevelCrossValidationTotal() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/firstLevel/total").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.firstLevelCrossValidation(Arrays.asList(firstLevelSet), "FIRST_LEVEL");
    }

    @Test
    public void testAuthorInformationCrossValidationSectLabel() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/secondLevel/author_information/sectLabel").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.authorInformationCrossValidation(Arrays.asList(firstLevelSet));
    }

    @Test
    public void testAuthorInformationCrossValidationTotal() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/secondLevel/author_information/total").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.authorInformationCrossValidation(Arrays.asList(firstLevelSet));
    }

    @Test
    public void testFootnoteCrossValidationSectLabel() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/secondLevel/footnote/sectLabel").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(3);

        tenFoldValidation.footnoteCrossValidation(Arrays.asList(firstLevelSet));
    }

    @Test
    public void testFootnoteCrossValidationTotal() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/secondLevel/footnote/total").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.footnoteCrossValidation(Arrays.asList(firstLevelSet));
    }

    public void testJSONCrossValidationSectLabel() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/json/sectLabel").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.jsonCrossValidation(Arrays.asList(firstLevelSet),
                new File(getClass().getResource("/validation/papers/sectLabel").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/firstLevel/sectLabel").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/secondLevel/author_information/sectLabel").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/secondLevel/footnote/sectLabel").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/json/sectLabel").getFile()).getAbsolutePath());
    }

    public void testJSONCrossValidationTotal() {

        File[] firstLevelSet = new File(getClass().getResource("/validation/json/total").getFile()).listFiles();
        KFoldValidation tenFoldValidation = new KFoldValidation(10);

        tenFoldValidation.jsonCrossValidation(Arrays.asList(firstLevelSet),
                new File(getClass().getResource("/validation/papers/total").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/firstLevel/total").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/secondLevel/author_information/total").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/secondLevel/footnote/total").getFile()).getAbsolutePath(),
                new File(getClass().getResource("/validation/json/total").getFile()).getAbsolutePath());
    }

}
