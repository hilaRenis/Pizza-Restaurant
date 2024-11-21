package pizza.restaurant.application.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to format the Json Response
 */
public class Response {

    /**
     * This method returns a ResponseEntity object that helps to format the Json Response
     *
     * @param statusText
     * @param status
     * @param responseObj
     * @param message
     * @return
     */
    public static ResponseEntity<Object> rest(String statusText, HttpStatus status, Object responseObj, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", statusText);
        map.put("data", responseObj);
        map.put("message", message);
        return new ResponseEntity<>(map, status);
    }
}
