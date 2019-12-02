package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;

public interface BeanScanner {
    void scan(Object... basePackage);

    void registerBeans(BeanFactory beanFactory);
}
