package nextstep.di.factory.beanscanner;

import nextstep.di.factory.beancreator.BeanCreator;

import java.util.Map;

public interface BeanScanner {
    Map<Class<?>, BeanCreator> scan(Object... basePackages);
}
