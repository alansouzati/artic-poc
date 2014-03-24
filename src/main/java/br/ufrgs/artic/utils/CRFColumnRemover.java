package br.ufrgs.artic.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
public class CRFColumnRemover {

    public static void main(String[] args) throws IOException {

        SortedMap<Integer,List<String>> unifiedFeatures;

        //int index2 = 91;
        for(File fileString  : new File(args[0]).listFiles() ) {
            unifiedFeatures = new TreeMap<Integer, List<String>>();
            FileInputStream correctCopyFIS = new FileInputStream(fileString);
            BufferedReader correctCopyBR = new BufferedReader(new InputStreamReader(correctCopyFIS, Charset.forName("UTF-8")));
            String correctCopyLine;

            int index = 0;
            while ((correctCopyLine = correctCopyBR.readLine()) != null) {

                String[] columns = correctCopyLine.split("\\s+");
                List<String> currentList = new ArrayList<String>();
                if(columns.length >=2) {
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
                }


                unifiedFeatures.put(index++, currentList);

            }

            File folder = new File(fileString.getParent(),"output");

            File file = new File(folder, fileString.getName().replace("_0001.artic.crf","") + ".artic");

            if (!file.exists()) {
                if(!file.createNewFile()) System.out.println("Could not create " + file.getAbsolutePath());
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(Integer currentIndex : unifiedFeatures.keySet()) {

                bw.write(unifiedFeatures.get(currentIndex).toString().replace("[", "").replace("]", "")
                        .replace(",", "")+"\n");
            }


            bw.close();
        }

    }
}
