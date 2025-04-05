package cz.muni.fi.bandmanagementservice.band.exceptions;

/**
 * @author Tomáš MAREK
 */
public class DataStorageException extends RuntimeException {
    public DataStorageException(String message) {
        super(message);
    }
}
