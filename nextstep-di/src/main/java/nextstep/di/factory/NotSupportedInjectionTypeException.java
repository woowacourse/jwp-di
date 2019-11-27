package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotSupportedInjectionTypeException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(NotSupportedInjectionTypeException.class);

    public NotSupportedInjectionTypeException(Class<?> clazz) {
        super("지원하지 않는 인젝션 타입");
        log.debug("NotSupportedInjectionType : {}", clazz.getTypeName());
    }
}
