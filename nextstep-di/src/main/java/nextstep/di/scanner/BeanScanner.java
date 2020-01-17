package nextstep.di.scanner;

import nextstep.exception.EmptyBasePackagesException;

public interface BeanScanner {
    void scanBeans(Object... basePackage);

    default void checkEmptyBasePackage(Object[] basePackage) {
        if (basePackage.length == 0) {
            throw new EmptyBasePackagesException();
        }
    }
}
