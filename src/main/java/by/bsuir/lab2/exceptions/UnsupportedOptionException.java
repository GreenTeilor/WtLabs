package by.bsuir.lab2.exceptions;

/**
 * Thrown when there is no certain option available
 */
public class UnsupportedOptionException extends Exception {

    /**
     * Constructor
     *
     * @param message - exception message
     */
    public UnsupportedOptionException(String message) {
        super(message);
    }
}
