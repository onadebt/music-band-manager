package cz.muni.fi.bandmanagementservice.rest.exceptionhandling;

import cz.muni.fi.bandmanagementservice.exceptions.BandNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.BandOfferNotFoundException;
import cz.muni.fi.bandmanagementservice.exceptions.InvalidOperationException;
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

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<?> handleBandNotFoundException(InvalidOperationException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid operation");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }


}
