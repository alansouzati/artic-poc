package br.ufrgs.artic.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: soualan
 * Date: 11/19/13
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkRename {
    public static void main(String[] args) throws IOException {
        int index = 41;
        int index2 = 0;
        for (File fileString : new File(args[0]).listFiles()) {
            fileString.renameTo(new File(fileString.getParentFile(), fileString.getName().replaceAll("_0001.artic.footer.crf",".artic")));
        }
    }
}
