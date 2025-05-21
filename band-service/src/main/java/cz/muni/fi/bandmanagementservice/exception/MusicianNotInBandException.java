package cz.muni.fi.bandmanagementservice.exception;

/**
 * @author Tomáš MAREK
 */
public class MusicianNotInBandException extends RuntimeException {
    public MusicianNotInBandException(Long bandId, Long memberId) {
        super("Member with id " + memberId + " already exists in band " + bandId);
    }
}
