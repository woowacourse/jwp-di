package nextstep.di.factory.scanner;

import nextstep.di.factory.definition.BeanDefinition;

import java.util.Map;

public interface BeanScanner {
    Map<Class<?>, BeanDefinition> scanBeans();
}
