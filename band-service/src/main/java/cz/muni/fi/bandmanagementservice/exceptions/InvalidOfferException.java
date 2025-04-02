package cz.muni.fi.bandmanagementservice.exceptions;

/**
 * @author Tomáš MAREK
 */
public class InvalidOfferException extends RuntimeException {
    public InvalidOfferException(String message) {
        super(message);
    }
}
