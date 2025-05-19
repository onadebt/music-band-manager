package cz.muni.fi.userservice.exception;

public class ArtistNotInBandException extends RuntimeException {
    public ArtistNotInBandException(Long bandId, Long artistId) {
        super("Artist with id " + artistId + " is not linked to band with id " + bandId);
    }
}
