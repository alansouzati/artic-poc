package br.ufrgs.artic.utils;

import java.io.*;
import java.nio.charset.Charset;

public class CRFFileUnifier {

    public static void main(String[] args) throws IOException {

        File dir = new File(args[0]);
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".artic.crf");
            }
        });

        File unifiedFile = new File(dir, "test.sectLabel.crf");
        if (!unifiedFile.exists()) {
            if(!unifiedFile.createNewFile()) System.out.println("Could not create " + unifiedFile.getAbsolutePath());
        }

        FileWriter fw = new FileWriter(unifiedFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);


        for(File file : files) {
           // int index = 0;
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            String line;

            while ((line = br.readLine()) != null) {

                //if(index++==0) {
                  //continue;
                //}
                bw.write(line+"\n");
            }

            bw.write("\n");
        }

        bw.close();

    }
}