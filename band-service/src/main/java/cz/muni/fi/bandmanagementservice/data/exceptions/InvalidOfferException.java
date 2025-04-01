package cz.muni.fi.bandmanagementservice.data.exceptions;

/**
 * @author Tomáš MAREK
 */
public class InvalidOfferException extends RuntimeException {
    public InvalidOfferException(String message) {
        super(message);
    }
}
