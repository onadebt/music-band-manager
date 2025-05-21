package cz.muni.fi.bandmanagementservice.exception;

/**
 * @author Tomáš MAREK
 */
public class BandNotFoundException extends RuntimeException {
    public BandNotFoundException(Long bandId) {
        super("Band with id %d was not found".formatted(bandId));
    }
}
