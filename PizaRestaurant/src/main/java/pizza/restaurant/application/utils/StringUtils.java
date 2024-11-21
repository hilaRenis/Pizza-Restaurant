package pizza.restaurant.application.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is used to manipulate strings
 */
public class StringUtils {
    /**
     * Default Constructor
     */
    private StringUtils() {
    }

    /**
     * Method that takes a parameter of type string (called url) and returns the id of this url
     *
     * @param url e.g: /api/v1/users/show/5
     * @return e.g: id = 5
     */
    public static Long parseIdFromURL(String url) {
        String[] array = url.split("/");
        String stringId = array[array.length - 1];
        return Long.valueOf(stringId.trim());
    }

    /**
     * Converts String to JsonNode
     *
     * @param string e.g {"name":"John","last_name":"doe"}
     * @return
     * @throws JsonProcessingException
     */
    public static JsonNode stringToJson(String string) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(string);
    }

    /**
     * Convert object to Json
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static JsonNode convertToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String string = mapper.writeValueAsString(object);
        return mapper.readTree(string);
    }

}
