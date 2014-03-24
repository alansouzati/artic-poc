package br.ufrgs.artic.exceptions;

import java.io.IOException;

/**
 * Checked exception for problems while loading the OmniPage XML file.
 */
public class OmniPageParsingException extends Exception {

    public OmniPageParsingException(String message, Exception e) {
        super(message,e);
    }

    public OmniPageParsingException(IOException e) {
        super(e);
    }
}
