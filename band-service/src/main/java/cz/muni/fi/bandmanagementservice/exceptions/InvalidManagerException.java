package cz.muni.fi.bandmanagementservice.exceptions;

/**
 * @author Tomáš MAREK
 */
public class InvalidManagerException extends RuntimeException{
    public InvalidManagerException(Long bandId, Long managerId) {
        super("The manager %d is not manager of band %d and cannot manipulate it".formatted(managerId, bandId));
    }
}
