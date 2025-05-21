package cz.muni.fi.userservice.exception;

public class ArtistAlreadyInBandException extends RuntimeException {
    public ArtistAlreadyInBandException(Long bandId, Long artistId) {
        super("Artist with id " + artistId + " is already linked to band with id " + bandId);
    }
}
