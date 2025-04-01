package cz.muni.fi.bandmanagementservice.data.exceptions;

/**
 * @author Tomáš MAREK
 */
public class DataStorageException extends RuntimeException {
    public DataStorageException(String message) {
        super(message);
    }
}
