package combase.pubsubpublisher.globalexceptionhandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler({ExecutionException.class, InterruptedException.class, TimeoutException.class})
    public ResponseEntity<ErrorDTO> handleApiFutureException(Exception ex) {
        LOG.error(ex.toString(), ex);
        return new ResponseEntity<>(new ErrorDTO("error", ex.getMessage(), ex.toString()), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        LOG.error(ex.toString(), ex);
        return new ResponseEntity<>(new ErrorDTO("error", ex.getMessage(), ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConnectionNotEstablishedException.class, ConnectionNotClosedException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(Exception ex) {
        LOG.error(ex.toString(), ex);
        return new ResponseEntity<>(new ErrorDTO("error", ex.getMessage(), ex.toString()), HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler({MessageNotSentException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(MessageNotSentException ex) {
        LOG.error("FAILED TO PUBLISH MESSAGE");
        LOG.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorDTO("FAILED TO PUBLISH MESSAGE", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleExceptions(RuntimeException ex) {
        LOG.error(ex.toString(), ex);
        return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }


}