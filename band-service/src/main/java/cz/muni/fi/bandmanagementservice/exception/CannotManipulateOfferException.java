package cz.muni.fi.bandmanagementservice.exception;

import cz.muni.fi.enums.BandOfferStatus;

/**
 * @author Tomáš MAREK
 */
public class CannotManipulateOfferException extends RuntimeException {
    public CannotManipulateOfferException(BandOfferStatus status) {
      super("Offer was already %s and cannot be manipulated anymore!".formatted(status));
    }
}
