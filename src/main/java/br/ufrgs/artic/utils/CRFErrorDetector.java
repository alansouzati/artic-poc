package br.ufrgs.artic.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CRFErrorDetector {

    public static void main(String[] args) throws IOException {
        File dir = new File(args[0]);
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".sectLabel");
            }
        });

        for(File currentFile : files) {

            FileInputStream correctCopyFIS = new FileInputStream(currentFile);
            BufferedReader correctCopyBR = new BufferedReader(new InputStreamReader(correctCopyFIS, Charset.forName("UTF-8")));
            String correctCopyLine;
            System.out.println("Processing "+currentFile.getAbsolutePath());

            int index = 0;
            while ((correctCopyLine = correctCopyBR.readLine()) != null) {

                String[] columns = correctCopyLine.split("\\s+");
                System.out.println("Line: "+index++);
                System.out.println("Length: "+columns.length);

            }

        }

    }
}
