package nextstep.di.factory;

import java.util.Map;

public interface BeanScanner {
    Map<Class<?>, BeanDefinition> scanBeans();
}
