package br.ufrgs.artic.validation;

import br.ufrgs.artic.ArticRunner;
import br.ufrgs.artic.model.ArticMetadata;
import br.ufrgs.artic.utils.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Utility class that performs k-fold validations for all levels of CRF project.
 */
public class KFoldValidation {

    private int k;

    private static final String SECTLABEL_FIRST_LEVEL_TEMPLATE;
    private static final String FIRST_LEVEL_TEMPLATE;
    private static final String AUTHOR_INFORMATION_TEMPLATE;
    private static final String FOOTNOTE_TEMPLATE;
    private static final String HEADER_MODEL;

    static {
        File firstLevelTemplate = FileUtils.getArticTempResource("artic.first.level.template.crf");
        File sectLabelFirstLevelTemplate = FileUtils.getArticTempResource("sectLabel.first.level.template.crf");
        File authorInformationTemplate = FileUtils.getArticTempResource("artic.author.information.template.crf");
        File footnoteTemplate = FileUtils.getArticTempResource("artic.footnote.template.crf");
        File headerModel = getCRFModel("artic.header.model.crf");

        FIRST_LEVEL_TEMPLATE = firstLevelTemplate.getAbsolutePath();
        AUTHOR_INFORMATION_TEMPLATE = authorInformationTemplate.getAbsolutePath();
        FOOTNOTE_TEMPLATE = footnoteTemplate.getAbsolutePath();
        HEADER_MODEL = headerModel.getAbsolutePath();
        SECTLABEL_FIRST_LEVEL_TEMPLATE = sectLabelFirstLevelTemplate.getAbsolutePath();
    }

    private static File getCRFModel(String modelName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File destination = new File(modelName);
        try {
            if (!destination.exists()) {
                destination.createNewFile();
                org.apache.commons.io.FileUtils.copyFile(new File(classLoader.getResource(modelName).getFile()), destination);
            }

        } catch (IOException e) {
            System.err.println("Could not create temp folder");
        }
        return destination;
    }

    public KFoldValidation(int k) {
        this.k = k;
    }

    public void firstLevelCrossValidation(List<File> goldStandardSet, String layer) {
        List<File> goldStandardCloneSet = getRandomSet(goldStandardSet);
        File tempDir;
        try {
            tempDir = FileUtils.createTempDirectory();
            build(goldStandardCloneSet, tempDir, layer);
        } catch (IOException e) {
            System.err.println("Not able to create required files for validation: " + e.getMessage());
        }
    }

    public void authorInformationCrossValidation(List<File> goldStandardSet) {
        List<File> goldStandardCloneSet = getRandomSet(goldStandardSet);
        File tempDir;
        try {
            tempDir = FileUtils.createTempDirectory();
            build(goldStandardCloneSet, tempDir, "AUTHOR_INFORMATION");
        } catch (IOException e) {
            System.err.println("Not able to create required files for validation: " + e.getMessage());
        }
    }

    public void footnoteCrossValidation(List<File> goldStandardSet) {
        List<File> goldStandardCloneSet = getRandomSet(goldStandardSet);
        File tempDir;
        try {
            tempDir = FileUtils.createTempDirectory();
            build(goldStandardCloneSet, tempDir, "FOOTNOTE");
        } catch (IOException e) {
            System.err.println("Not able to create required files for validation: " + e.getMessage());
        }
    }

    public void jsonCrossValidation(List<File> goldStandardSet, String pdfBaseFolder, String firstLevelBaseFolder,
                                    String authorInformationBaseFolder, String footnoteBaseFolder, String jsonBaseFolder) {
        List<File> goldStandardCloneSet = getRandomSet(goldStandardSet);

        Map<Integer, List<File>> bucketMap = getBuckets(goldStandardCloneSet);

        Map<String, List<Double>> totalPrecisionMap = new HashMap<String, List<Double>>();

        for (int i = 0; i < k; i++) {

            List<File> trainingSetFirstLevel = new ArrayList<File>();
            List<File> trainingSetAuthorInformation = new ArrayList<File>();
            List<File> trainingSetFootnote = new ArrayList<File>();
            Map<Integer, List<File>> clonedBucketMap = new HashMap<Integer, List<File>>(bucketMap);

            for (Map.Entry<Integer, List<File>> currentEntry : clonedBucketMap.entrySet()) {
                for (File toBeAdded : currentEntry.getValue()) {
                    trainingSetFirstLevel.add(new File(firstLevelBaseFolder, toBeAdded.getName()));
                    trainingSetAuthorInformation.add(new File(authorInformationBaseFolder, toBeAdded.getName()));
                    trainingSetFootnote.add(new File(footnoteBaseFolder, toBeAdded.getName()));
                }
            }


            try {
                File tempDir = FileUtils.createTempDirectory();

                buildCRFModel(trainingSetFirstLevel, tempDir, "FIRST_LEVEL");
                buildCRFModel(trainingSetAuthorInformation, tempDir, "AUTHOR_INFORMATION");
                buildCRFModel(trainingSetFootnote, tempDir, "FOOTNOTE");

                ArticRunner runner = new ArticRunner("C:\\omnipage");

                List<File> testSet = bucketMap.get(i);
                for (File testFile : testSet) {
                    String name = testFile.getName().split("\\.")[0];
                    ArticMetadata targetMetadata = runner.getMetadata(
                            new File(pdfBaseFolder, name + ".pdf").getAbsolutePath(),
                            getFileInTempDir("FIRST_LEVEL.model.artic", tempDir).getAbsolutePath(),
                            HEADER_MODEL,
                            getFileInTempDir("AUTHOR_INFORMATION.model.artic", tempDir).getAbsolutePath(),
                            getFileInTempDir("FOOTNOTE.model.artic", tempDir).getAbsolutePath());

                    String goldStandardJSON = org.apache.commons.io.FileUtils.readFileToString(
                            new File(jsonBaseFolder, name + ".artic"));
                    ArticMetadata goldMetadata = ArticMetadata.getInstance(goldStandardJSON);

                    System.out.println("Gold: " + goldMetadata.toJson());
                    System.out.println("Generated: " + targetMetadata.toJson());

                    Map<String, Double> precisionMap = PrecisionCalculator.validateMetadata(goldMetadata, targetMetadata);
                    PrecisionCalculator.augmentPrecisionMap(totalPrecisionMap, precisionMap);

                    System.out.println("Local Precision: " + precisionMap);
                    outputTotalPrecision(totalPrecisionMap);
                }

            } catch (IOException e) {
                System.err.println("Not able to create required files for validation: " + e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("General error: " + e.getMessage());
            }
        }
    }

    private void buildCRFModel(List<File> trainingSet, File tempDir, String option) throws IOException, InterruptedException {
        String templateFile = getTemplateFile(option);
        File trainingFile = getFileInTempDir(option + ".training.artic", tempDir);
        File modelFile = getFileInTempDir(option + ".model.artic", tempDir);
        writeSetToFile(trainingSet, trainingFile);
        buildCRFModel(trainingFile, modelFile, templateFile);
    }

    private void validate(File tempDir, String option, Map<String, List<Double>> totalPrecisionMap) throws IOException, InterruptedException {

        File testFile = getFileInTempDir(option + "test.artic", tempDir);
        File modelFile = getFileInTempDir(option + "model.artic", tempDir);

        Map<Integer, List<String>> resultMap = getCRFResult(testFile.getAbsolutePath(), modelFile.getAbsolutePath());

        Map<String, Double> precisionMap = PrecisionCalculator.getPrecisionMap(resultMap, option);
        PrecisionCalculator.augmentPrecisionMap(totalPrecisionMap, precisionMap);

    }

    private void outputTotalPrecision(Map<String, List<Double>> totalPrecisionMap) {
        for (Map.Entry<String, List<Double>> currentEntry : totalPrecisionMap.entrySet()) {
            double total = 0;
            for (Double currentPrecision : currentEntry.getValue()) {
                total += currentPrecision;
            }

            System.out.println(String.format("Total %s precision is %.2f %%", currentEntry.getKey(), (total / currentEntry.getValue().size())));
        }
    }

    private void build(List<File> goldStandardSet, File tempDir, String option) {
        try {

            String templateFile = getTemplateFile(option);

            File testFile = getFileInTempDir(option + "test.artic", tempDir);
            File trainingFile = getFileInTempDir(option + "training.artic", tempDir);
            File modelFile = getFileInTempDir(option + "model.artic", tempDir);

            buildCRFModel(goldStandardSet, testFile, trainingFile, modelFile, templateFile, tempDir, option);

        } catch (IOException e) {
            System.err.println("Not able to create required files for validation: " + e.getMessage());
        }
    }

    private String getTemplateFile(String option) {
        String templateFile;
        if ("FIRST_LEVEL".equals(option)) {
            templateFile = FIRST_LEVEL_TEMPLATE;
        } else if ("FOOTNOTE".equals(option)) {
            templateFile = FOOTNOTE_TEMPLATE;
        } else if ("SECTLABEL_FIRST_LEVEL".equals(option)) {
            templateFile = SECTLABEL_FIRST_LEVEL_TEMPLATE;
        } else {
            templateFile = AUTHOR_INFORMATION_TEMPLATE;
        }

        return templateFile;
    }

    private void buildCRFModel(List<File> goldStandardCloneSet, File testFile, File trainingFile,
                               File modelFile, String templateFile, File tempDir, String option) {
        Map<Integer, List<File>> bucketMap = getBuckets(goldStandardCloneSet);

        Map<String, List<Double>> totalPrecisionMap = new HashMap<String, List<Double>>();

        for (int i = 0; i < k; i++) {
            List<File> testSet = bucketMap.get(i);

            List<File> trainingSet = new ArrayList<File>();
            trainingSet.addAll(new HashMap<Integer, List<File>>(bucketMap).remove(i));

            try {
                writeSetToFile(testSet, testFile);
                writeSetToFile(trainingSet, trainingFile);

                buildCRFModel(trainingFile, modelFile, templateFile);

                validate(tempDir, option, totalPrecisionMap);

            } catch (IOException e) {
                System.err.println("Not able to create required files for validation: " + e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("General error: " + e.getMessage());
            }

        }

        outputTotalPrecision(totalPrecisionMap);
    }

    private void buildCRFModel(File trainingFile, File modelFile, String templateFile) throws IOException, InterruptedException {
        ProcessBuilder ps = new ProcessBuilder("crf_learn", "-f", "2", "-c", "2",
                templateFile, trainingFile.getAbsolutePath(),
                modelFile.getAbsolutePath());

        Process pr = ps.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((reader.readLine()) != null) {
        }
        pr.waitFor();
    }

    private Map<Integer, List<String>> getCRFResult(String testFile, String model) throws IOException, InterruptedException {
        Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();

        BufferedReader in = null;
        try {
            ProcessBuilder ps = new ProcessBuilder("crf_test", "-m", model, testFile);

            Process pr = ps.start();

            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            int index = 0;
            while ((line = in.readLine()) != null) {
                String[] columns = line.split("\t");
                List<String> resultClass = new ArrayList<String>();
                if (columns.length > 2) {
                    resultClass.add(columns[columns.length - 1]);
                    resultClass.add(columns[columns.length - 2]);
                    result.put(index++, resultClass);
                }

            }
            pr.waitFor();
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return result;

    }


    private void writeSetToFile(List<File> set, File file) throws IOException {
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            for (File currentFile : set) {
                FileInputStream fis = new FileInputStream(currentFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
                String line;

                while ((line = br.readLine()) != null) {
                    bw.write(line + "\n");
                }

                bw.write("\n");
            }
        } finally {
            bw.close();
        }

    }

    private File getFileInTempDir(String fileName, File tempDir) throws IOException {
        File file;

        if (tempDir != null) {
            file = new File(tempDir, fileName);
        } else {
            file = new File(fileName);
        }

        if (!file.exists()) {
            if (!file.createNewFile())
                System.out.println("Could not create " + file.getAbsolutePath());
        }

        return file;

    }

    private Map<Integer, List<File>> getBuckets(List<File> goldStandardCloneSet) {
        int bucket = (int) Math.round((double) goldStandardCloneSet.size() / k);
        Map<Integer, List<File>> bucketMap = new HashMap<Integer, List<File>>();
        int fromIndex = 0;
        for (int i = 0; i < k; i++) {
            int toIndex = fromIndex + bucket;
            if (toIndex > goldStandardCloneSet.size()) {
                toIndex = goldStandardCloneSet.size();
            }
            bucketMap.put(i, goldStandardCloneSet.subList(fromIndex, toIndex));
            fromIndex = toIndex;
        }
        return bucketMap;
    }

    private List<File> getRandomSet(List<File> goldStandardSet) {
        List<File> goldStandardCloneSet = new ArrayList<File>(goldStandardSet);
        Collections.shuffle(goldStandardCloneSet);
        return goldStandardCloneSet;
    }

}
