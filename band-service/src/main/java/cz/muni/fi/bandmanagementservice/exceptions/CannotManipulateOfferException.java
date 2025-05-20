package cz.muni.fi.bandmanagementservice.exceptions;

import cz.muni.fi.shared.enums.BandOfferStatus;

/**
 * @author Tomáš MAREK
 */
public class CannotManipulateOfferException extends RuntimeException {
    public CannotManipulateOfferException(BandOfferStatus status) {
      super("Offer was already %s and cannot be manipulated anymore!".formatted(status));
    }
}
