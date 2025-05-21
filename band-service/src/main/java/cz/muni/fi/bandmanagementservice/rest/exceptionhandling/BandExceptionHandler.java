package cz.muni.fi.bandmanagementservice.rest.exceptionhandling;

import cz.muni.fi.bandmanagementservice.exception.BandAlreadyExistsException;
import cz.muni.fi.bandmanagementservice.exception.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.exception.BandOfferAlreadyExistsException;
import cz.muni.fi.bandmanagementservice.exception.BandOfferNotFoundException;
import cz.muni.fi.bandmanagementservice.exception.CannotManipulateOfferException;
import cz.muni.fi.bandmanagementservice.exception.InvalidManagerException;
import cz.muni.fi.bandmanagementservice.exception.MusicianAlreadyInBandException;
import cz.muni.fi.bandmanagementservice.exception.MusicianNotInBandException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Tomáš MAREK
 */
@RestControllerAdvice
public class BandExceptionHandler {
    @ExceptionHandler(BandNotFoundException.class)
    public ResponseEntity<?> handleBandNotFoundException(BandNotFoundException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Band not found");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BandOfferNotFoundException.class)
    public ResponseEntity<?> handleBandOfferNotFoundException(BandOfferNotFoundException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Band Offer not found");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BandAlreadyExistsException.class)
    public ResponseEntity<?> handleBandAlreadyExistsException(BandAlreadyExistsException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Band already exists");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MusicianNotInBandException.class)
    public ResponseEntity<?> handleMusicianNotInBandException(MusicianNotInBandException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Member is not part of band");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MusicianAlreadyInBandException.class)
    public ResponseEntity<?> handleMusicianAlreadyInBandException(MusicianAlreadyInBandException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Member is already part of band");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BandOfferAlreadyExistsException.class)
    public ResponseEntity<?> handleBandOfferAlreadyExistsException(BandOfferAlreadyExistsException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Band offer already exists");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotManipulateOfferException.class)
    public ResponseEntity<?> handleCannotManipulateOfferException(CannotManipulateOfferException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Band Offer can no longer be manipulated");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidManagerException.class)
    public ResponseEntity<?> handleInvalidManagerException(InvalidManagerException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid manager");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }
}
