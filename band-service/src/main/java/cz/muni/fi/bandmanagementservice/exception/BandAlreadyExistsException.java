package cz.muni.fi.bandmanagementservice.exception;

/**
 * @author Tomáš MAREK
 */
public class BandAlreadyExistsException extends RuntimeException {
    public BandAlreadyExistsException(String message) {
        super(message);
    }
}
