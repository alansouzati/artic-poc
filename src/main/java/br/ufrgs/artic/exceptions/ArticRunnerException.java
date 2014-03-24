package br.ufrgs.artic.exceptions;

import java.io.IOException;

/**
 * Checked exception for problems while creating an Artic line.
 */
public class ArticRunnerException extends RuntimeException {

    public ArticRunnerException(String message) {
        super(message);
    }

    public ArticRunnerException(Exception e) {
        super(e);
    }
}
