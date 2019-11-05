package nextstep.di.validation;

import nextstep.di.factory.exception.CircularReferenceException;
import nextstep.di.validation.exception.BeanRegisterHistoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Deque;

public class CircularReferenceValidator implements BeanValidator {

    private static final Logger log = LoggerFactory.getLogger(CircularReferenceValidator.class);

    @Override
    public void validate(final Object target, final Object... validationHints) {
        Deque<Class<?>> history = extractBeanRegisterHistory(validationHints);
        Class<?> clazz = (Class<?>) target;
        if (history.contains(clazz)) {
            log.error("circular reference on {}", clazz);
            throw new CircularReferenceException("circular reference");
        }
    }

    @SuppressWarnings("unchecked")
    private Deque<Class<?>> extractBeanRegisterHistory(final Object[] validationHints) {
        return (Deque<Class<?>>) Arrays.stream(validationHints)
                .filter(hint -> Deque.class.isAssignableFrom(hint.getClass()))
                .findFirst()
                .orElseThrow(BeanRegisterHistoryNotFoundException::new);
    }
}
