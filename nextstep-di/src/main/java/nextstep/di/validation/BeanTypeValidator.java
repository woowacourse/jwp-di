package nextstep.di.validation;

import nextstep.di.beans.factory.exception.IllegalAnnotationException;
import nextstep.di.beans.specification.BeanSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanTypeValidator implements BeanValidator {

    private static final Logger log = LoggerFactory.getLogger(BeanTypeValidator.class);

    @Override
    public void validate(final Object target, final Object... validationHints) {
        BeanSpecification beanSpecification = (BeanSpecification) target;
        Class<?> type = beanSpecification.getType();
        if (type.isInterface() && beanSpecification.canInterface()) {
            log.error("invalid annotation on {}", target);
            throw new IllegalAnnotationException("invalid annotation");
        }
    }
}
