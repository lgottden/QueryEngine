/**
 * Custom exception
 */
public class InvalidQueryException extends Exception {

    InvalidQueryException(String query) {
        super(String.format("InvalidQueryException: %s", query));
    }
}
