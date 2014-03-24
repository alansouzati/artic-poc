package br.ufrgs.artic.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to handle file operations
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static String readFile(String path, Charset charset) throws IOException {
        FileInputStream stream = new FileInputStream(new File(URLDecoder.decode(path, "UTF-8")));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            return charset.decode(bb).toString();
        } finally {
            stream.close();
        }
    }

    /**
     * This method extracts the list of elements from the @param element that contains the @param tagName.
     *
     * @param tagName the tag name used to get the list of elements
     * @param element the dom element to look for the tags with the given name
     * @return list of elements that have the @param tagName
     */
    public static List<Element> getElementsByTagName(String tagName, Element element) {
        List<Element> elements = new ArrayList<Element>();

        NodeList nodeList = element.getElementsByTagName(tagName);

        if (nodeList != null && nodeList.getLength() > 0) {

            for (int index = 0; index < nodeList.getLength(); index++) {
                Node currentNode = nodeList.item(index);

                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    elements.add((Element) currentNode);
                }
            }
        }
        return elements;
    }

    public static File createTempDirectory()
            throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }

    public static File getArticTempResource(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File destination = new File(fileName);
        try {
            if (!destination.exists()) {
                destination.createNewFile();
                org.apache.commons.io.FileUtils.copyFile(new File(classLoader.getResource(fileName).getFile()), destination);
            }

        } catch (IOException e) {
            System.err.println("Could not create temp folder");
        }
        return destination;
    }

    public static boolean isUpperCase(String string) {
        for(Character currentChar : string.toCharArray()) {
           if(!Character.isUpperCase(currentChar)) {
               return false;
           }
        }
        return true;
    }
}
