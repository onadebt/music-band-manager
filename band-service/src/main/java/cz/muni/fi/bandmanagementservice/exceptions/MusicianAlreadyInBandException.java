package cz.muni.fi.bandmanagementservice.exceptions;

/**
 * @author Tomáš MAREK
 */
public class MusicianAlreadyInBandException extends RuntimeException {
    public MusicianAlreadyInBandException(Long bandId, Long memberId) {
        super("Member with id %d is already part of band %d".formatted(memberId, bandId));
    }
}
