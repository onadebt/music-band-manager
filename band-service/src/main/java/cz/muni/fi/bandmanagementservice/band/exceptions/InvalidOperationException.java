package cz.muni.fi.bandmanagementservice.band.exceptions;

/**
 * @author Tomáš MAREK
 */
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
