package br.ufrgs.artic.sectLabel.exceptions;

/**
 * Checked exception for problems while loading the sectlabel files XML file.
 */
public class SectLabelParsingException extends Exception {

    public SectLabelParsingException(String message, Exception e) {
        super(message,e);
    }
}
