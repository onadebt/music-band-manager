package cz.muni.fi.bandmanagementservice.band.exceptions;

/**
 * @author Tomáš MAREK
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
