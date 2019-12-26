package nextstep.di.validation;

import nextstep.di.beans.factory.exception.CircularReferenceException;
import nextstep.di.beans.specification.BeanSpecification;
import nextstep.di.validation.exception.BeanRegisterHistoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Deque;

public class CircularReferenceValidator implements BeanValidator {

    private static final Logger log = LoggerFactory.getLogger(CircularReferenceValidator.class);

    @Override
    public void validate(final Object target, final Object... validationHints) {
        Deque<BeanSpecification> history = extractBeanRegisterHistory(validationHints);
        BeanSpecification beanSpecification = (BeanSpecification) target;
        if (history.contains(beanSpecification)) {
            log.error("circular reference on {}", beanSpecification);
            throw new CircularReferenceException("circular reference");
        }
    }

    @SuppressWarnings("unchecked")
    private Deque<BeanSpecification> extractBeanRegisterHistory(final Object[] validationHints) {
        return (Deque<BeanSpecification>) Arrays.stream(validationHints)
                .filter(hint -> Deque.class.isAssignableFrom(hint.getClass()))
                .findFirst()
                .orElseThrow(BeanRegisterHistoryNotFoundException::new);
    }
}
