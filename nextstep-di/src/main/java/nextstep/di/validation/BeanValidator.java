package nextstep.di.validation;

public interface BeanValidator {

    void validate(Object target, Object... validationHints);
}
