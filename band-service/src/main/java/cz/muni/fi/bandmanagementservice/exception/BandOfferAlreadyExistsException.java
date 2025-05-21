package cz.muni.fi.bandmanagementservice.exception;

/**
 * @author Tomáš MAREK
 */
public class BandOfferAlreadyExistsException extends RuntimeException {
    public BandOfferAlreadyExistsException(Long bandId, Long invitedMusicianId) {
        super("There is already a pending band offer for musician %d to join band %d".formatted(invitedMusicianId, bandId));
    }
}
