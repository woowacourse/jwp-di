package nextstep.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationCreateException extends RuntimeException {
    public ConfigurationCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
