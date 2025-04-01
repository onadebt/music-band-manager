package cz.muni.fi.bandmanagementservice.data.exceptions;

/**
 * @author Tomáš MAREK
 */
public class InvalidBandException extends RuntimeException {
    public InvalidBandException(String message) {
        super(message);
    }
}
