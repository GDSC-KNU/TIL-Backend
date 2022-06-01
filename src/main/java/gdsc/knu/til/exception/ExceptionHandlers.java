package gdsc.knu.til.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Date;

@RestController
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(value= {UsernameNotFoundException.class})
    public ResponseEntity handleUsernameNotFoundExceptions() {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value= {IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgumentExceptions() {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= {KeyAlreadyExistsException.class})
    public ResponseEntity handleAlreadyExistExceptions() {
        return new ResponseEntity(HttpStatus.CONFLICT);
    }
}
