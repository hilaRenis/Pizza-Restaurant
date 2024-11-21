package pizza.restaurant.application.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppException extends RuntimeException {

    /**
     * Exception's message
     */
    private String message;

    /**
     * Default Constructor
     */
    public AppException(Exception exception) {
        this.message = exception.getMessage();
        log.error("ALERT: {}", message);
    }

    /**
     * Default Constructor
     */
    public AppException(String message) {
        this.message = message;
        log.error("ALERT: {}", message);
    }

}