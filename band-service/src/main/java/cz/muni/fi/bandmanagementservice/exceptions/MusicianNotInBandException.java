package cz.muni.fi.bandmanagementservice.exceptions;

/**
 * @author Tomáš MAREK
 */
public class MusicianNotInBandException extends RuntimeException {
    public MusicianNotInBandException(Long bandId, Long memberId) {
        super("Member with id " + memberId + " already exists in band " + bandId);
    }
}
