package cz.muni.fi.bandmanagementservice.exceptions;

/**
 * @author Tomáš MAREK
 */
public class BandOfferNotFoundException extends RuntimeException {
    public BandOfferNotFoundException(Long bandId) {
        super("BandOffer with id %d was not found".formatted(bandId));
    }
}
