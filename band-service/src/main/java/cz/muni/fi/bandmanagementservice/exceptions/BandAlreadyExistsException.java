package cz.muni.fi.bandmanagementservice.exceptions;

/**
 * @author Tomáš MAREK
 */
public class BandAlreadyExistsException extends RuntimeException {
    public BandAlreadyExistsException(String message) {
        super(message);
    }
}
