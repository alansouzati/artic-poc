package br.ufrgs.artic;

import br.ufrgs.artic.exceptions.ArticRunnerException;
import br.ufrgs.artic.exceptions.OmniPageParsingException;
import br.ufrgs.artic.model.ArticMetadata;
import br.ufrgs.artic.parser.ArticParser;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for performing the runner actions for Artic project.
 */
public class ArticRunner {

    private final String omniPageFolder;

    private static final Options OPTIONS = new Options();
    private static final CommandLineParser PARSER = new BasicParser();
    private static final String RUNNING_FOLDER;
    private static final String FIRST_LEVEL_MODEL;
    private static final String HEADER_MODEL;
    private static final String FOOTER_MODEL;
    private static final String AUTHOR_INFORMATION_MODEL;

    static {
        OPTIONS.addOption("omniPageFolder", true, "Represents the folder where the OmniPage batch process is configured.");
        OPTIONS.addOption("pdfFile", true, "Specify a folder or a pdf file to run the metadata extractor. If none is provided uses the current directory.");
        OPTIONS.addOption("outputFolder", true, "Specify the output folder. If none is provided the pdfFile folder is used.");

        RUNNING_FOLDER = new File(ArticRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

        File firstLevelModel = getCRFModel("artic.first.level.model.crf");
        File headerModel = getCRFModel("artic.header.model.crf");
        File footerModel = getCRFModel("artic.footer.model.crf");
        File authorInformationModel = getCRFModel("artic.author.information.model.crf");

        FIRST_LEVEL_MODEL = firstLevelModel.getAbsolutePath();
        HEADER_MODEL = headerModel.getAbsolutePath();
        FOOTER_MODEL = footerModel.getAbsolutePath();
        AUTHOR_INFORMATION_MODEL = authorInformationModel.getAbsolutePath();

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

    public ArticRunner(String omniPageFolder) {
        this.omniPageFolder = omniPageFolder;
    }

    public ArticMetadata getMetadata(String pdfFilePath, String firstLevelModel, String headerModel, String authorInformationModel, String footnoteModel) throws IOException {
        System.out.println("Starting the generation process for :" + pdfFilePath);
        if (!pdfFilePath.endsWith(".pdf") && !pdfFilePath.endsWith(".jpg")) {
            throw new ArticRunnerException("The input should be a valid pdf file.");
        }

        File xmlFile = null;
        try {
            xmlFile = getOmniPageXML(pdfFilePath);

            return new ArticParser(xmlFile.getAbsolutePath()).getMetadata(firstLevelModel, headerModel, authorInformationModel, footnoteModel);
        } catch (IOException e) {
            throw new ArticRunnerException(e);
        } catch (OmniPageParsingException e) {
            throw new ArticRunnerException(e);
        } finally {
            if (xmlFile != null) {
                FileDeleteStrategy.NORMAL.delete(xmlFile);
            }
            System.out.println("Finished the generation process for :" + pdfFilePath);
        }
    }

    private File getOmniPageXML(String pdfFilePath) throws IOException {
        File inputFile = new File(pdfFilePath);

        long time = new Date().getTime();
        final String fileName = inputFile.getName().replace(".pdf", "") + "_" + time;

        File tempXMLFile;
        try {
            File omniPageFile = new File(omniPageFolder, fileName + ".pdf");

            FileUtils.copyFile(new File(pdfFilePath), omniPageFile);

            File omniPageXMLFile = new File(omniPageFolder, fileName + "_0001.xml"); //just interested in the first page for now

            int trials = 0;
            do {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Not able to wait!");
                }
            } while (!omniPageXMLFile.exists() && trials++ < 12);

            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                System.out.println("Not able to wait!");
            }

            if (!omniPageXMLFile.exists()) {
                throw new ArticRunnerException("Could not get the XML from OmniPage. Please configure your batch process.");
            }

            File tempFolder = new File("/temp");
            if (!tempFolder.exists() && !tempFolder.createNewFile()) {
                tempXMLFile = new File("omnipage_temp_" + time + ".xml");
            } else {
                tempXMLFile = new File(tempFolder, "omnipage_temp_" + time + ".xml");
            }


            FileUtils.copyFile(omniPageXMLFile, tempXMLFile);
        } finally {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Not able to wait!");
            }
            deleteOmniPageFiles(fileName, new File(omniPageFolder));
        }

        return tempXMLFile;
    }

    private void deleteOmniPageFiles(final String prefix, File omniPageXMLFolder) throws IOException {
        File[] toBeDeletedFiles = omniPageXMLFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getAbsolutePath().contains(prefix);
            }
        });
        for (File currentFile : toBeDeletedFiles) {
            try {
                FileDeleteStrategy.NORMAL.delete(currentFile);
            } catch (IOException e) {
                System.err.println("Not able to delete " + currentFile.getAbsolutePath() + ". Reason: " + e);
            }
        }
    }

    public static void main(String[] args) throws ParseException, IOException {

        CommandLine cmd = PARSER.parse(OPTIONS, args);

        String omniPageFolder = cmd.getOptionValue("omniPageFolder");
        if (omniPageFolder == null || omniPageFolder.isEmpty()) {
            throw new ArticRunnerException("The omniPageFolder param is mandatory");
        }
        ArticRunner articRunner = new ArticRunner(omniPageFolder);

        String pdfFile = (cmd.hasOption("pdfFile")) ? cmd.getOptionValue("pdfFile") : RUNNING_FOLDER;

        List<String> filesToGenerate = getFilesToGenerate(pdfFile);
        if (!filesToGenerate.isEmpty()) {
            String outputFolder = (cmd.hasOption("outputFolder")) ? cmd.getOptionValue("outputFolder") : new File(filesToGenerate.get(0)).getParent();

            for (String fileToGenerate : filesToGenerate) {
                ArticMetadata articMetadata = articRunner.getMetadata(fileToGenerate, FIRST_LEVEL_MODEL, HEADER_MODEL, AUTHOR_INFORMATION_MODEL, FOOTER_MODEL);

                outputMetadata(outputFolder, fileToGenerate, articMetadata);
            }
        } else {
            System.out.println("Not able to find any pdf to generate from " + pdfFile);
        }


    }

    private static void outputMetadata(String outputFolder, String fileToGenerate, ArticMetadata articMetadata) throws IOException {
        BufferedWriter bw = null;
        try {
            String fileName = new File(fileToGenerate).getName().replaceAll("[\\.pdf|\\.jpg]", "") + ".artic";
            FileWriter fw = new FileWriter(new File(outputFolder, fileName));
            bw = new BufferedWriter(fw);
            bw.write(articMetadata.toJson());
        } finally {
            if (bw != null) {
                bw.close();
            }

        }
    }

    private static List<String> getFilesToGenerate(String pdfFile) {
        List<String> filesToGenerate = new ArrayList<String>();
        if (pdfFile.endsWith(".pdf") || pdfFile.endsWith(".jpg")) {
            filesToGenerate.add(pdfFile);
        } else {
            File[] files = new File(pdfFile).listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getAbsolutePath().endsWith(".pdf") || file.getAbsolutePath().endsWith(".jpg");
                }
            });

            if (files != null && files.length > 0) {
                for (File file : files) {
                    filesToGenerate.add(file.getAbsolutePath());
                }
            }
        }
        return filesToGenerate;
    }

    public ArticMetadata getMetadata(String fileToGenerate) throws IOException {
        return getMetadata(fileToGenerate, FIRST_LEVEL_MODEL, HEADER_MODEL, AUTHOR_INFORMATION_MODEL, FOOTER_MODEL);
    }
}
