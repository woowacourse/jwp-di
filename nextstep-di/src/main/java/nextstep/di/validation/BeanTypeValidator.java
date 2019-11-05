package nextstep.di.validation;

import nextstep.di.factory.exception.IllegalAnnotationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanTypeValidator implements BeanValidator {

    private static final Logger log = LoggerFactory.getLogger(BeanTypeValidator.class);

    @Override
    public void validate(final Object target, final Object... validationHints) {
        Class<?> clazz = (Class<?>) target;
        if (clazz.isInterface()) {
            log.error("invalid annotation on {}", target);
            throw new IllegalAnnotationException("invalid annotation");
        }
    }
}
