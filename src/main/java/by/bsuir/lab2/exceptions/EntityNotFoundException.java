package by.bsuir.lab2.exceptions;

/**
 * Thrown when there is no entity found
 */
public class EntityNotFoundException extends Exception {

    /**
     * Constructor
     *
     * @param message - exception message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
