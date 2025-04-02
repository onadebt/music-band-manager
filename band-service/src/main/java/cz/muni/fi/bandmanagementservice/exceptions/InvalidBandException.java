package cz.muni.fi.bandmanagementservice.exceptions;

/**
 * @author Tomáš MAREK
 */
public class InvalidBandException extends RuntimeException {
    public InvalidBandException(String message) {
        super(message);
    }
}
