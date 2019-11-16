package nextstep.di.factory;

import java.util.Map;

public interface BeanScanner {
    Map<Class<?>, BeanCreator> scan(Object... basePackages);
}
