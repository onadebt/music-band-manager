package cz.muni.fi.bandmanagementservice.exception;

/**
 * @author Tomáš MAREK
 */
public class BandOfferNotFoundException extends RuntimeException {
    public BandOfferNotFoundException(Long bandOfferId) {
        super("Band offer with id %d was not found".formatted(bandOfferId));
    }
}
