package br.ufrgs.artic.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CRFLabelCopy {

    public static void main(String[] args) throws IOException {
        File dir = new File(args[0]);
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".sectLabel.crf");
            }
        });

        for(File currentFile : files) {
            int fileIndex = Integer.valueOf(currentFile.getName().split("_")[0]);
            SortedMap<Integer, List<String>> unifiedFeatures = new TreeMap<Integer, List<String>>();

            FileInputStream correctCopyFIS = new FileInputStream(currentFile);
            BufferedReader correctCopyBR = new BufferedReader(new InputStreamReader(correctCopyFIS, Charset.forName("UTF-8")));
            String correctCopyLine;

            int index = 0;
            while ((correctCopyLine = correctCopyBR.readLine()) != null) {

                String[] columns = correctCopyLine.split(" ");

                List<String> currentList = new ArrayList<String>();
                currentList.add(columns[0]);
                if (columns.length >= 2) {
                    currentList.add(columns[1]);
                    currentList.add(columns[2]);
                    currentList.add(columns[3]);
                    currentList.add(columns[4]);
                    currentList.add(columns[5]);
                    currentList.add(columns[6]);
                    currentList.add(columns[7]);
                    currentList.add(columns[8]);
                    currentList.add(columns[9]);
                    currentList.add(columns[10]);
                    currentList.add(columns[11]);
                    currentList.add(columns[12]);
                    currentList.add(columns[13]);
                    currentList.add(columns[14]);
                    currentList.add(columns[15]);
                    currentList.add(columns[16]);
                    currentList.add(columns[17]);
                    currentList.add(columns[18]);
                    currentList.add(columns[19]);
                    currentList.add(columns[20]);
                    currentList.add(columns[21]);
                    currentList.add(columns[22]);
                    currentList.add(columns[23]);
                    currentList.add(columns[24]);
                    currentList.add(columns[25]);
                    currentList.add(columns[26]);
                    currentList.add(columns[27]);
                    currentList.add(columns[28]);
                    currentList.add(columns[29]);
                    currentList.add(columns[30]);
                    currentList.add(columns[31]);
                    currentList.add(columns[32]);
                    currentList.add(columns[33]);
                    currentList.add(columns[34]);
                    currentList.add(columns[35]);
                    currentList.add(columns[36]);
                    currentList.add(columns[37]);
                    currentList.add(columns[38]);
                    currentList.add(columns[39]);
                    currentList.add(columns[40]);
                    currentList.add(columns[41]);
                    currentList.add(columns[42]);
                    currentList.add(columns[43]);
                    currentList.add(columns[44]);
                    currentList.add(columns[45]);
                    currentList.add(columns[46]);
                    currentList.add(columns[47]);
                    currentList.add(columns[48]);
                    currentList.add(columns[49]);
                    currentList.add(columns[50]);
                    currentList.add(columns[51]);
                }


                unifiedFeatures.put(index++, currentList);

            }

            FileInputStream toCopyFIS = new FileInputStream(args[1] + "/"+ fileIndex +".artic");
            BufferedReader toCopyBR = new BufferedReader(new InputStreamReader(toCopyFIS, Charset.forName("UTF-8")));
            String toCopyLine;

            index = 0;
            while ((toCopyLine = toCopyBR.readLine()) != null) {

                String[] columns = toCopyLine.split("\\s+");
                List<String> currentList = new ArrayList<String>(unifiedFeatures.get(index));
                if (!currentList.isEmpty() && currentList.size() > 1) {
                    currentList.add(columns[columns.length - 1]);
                    unifiedFeatures.put(index, currentList);
                }

                index++;

            }

            File articFile = new File(args[0]);
            File file = new File(articFile, fileIndex +".sectLabel");

            if (!file.exists()) {
                if(!file.createNewFile()) System.out.println("Could not create " + file.getAbsolutePath());
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (Integer currentIndex : unifiedFeatures.keySet()) {

                for(String currentFeature : unifiedFeatures.get(currentIndex)) {
                    bw.write(currentFeature + " ");
                }

                bw.write("\n");

            }


            bw.close();
        }

    }
}
