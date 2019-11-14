package nextstep.di.factory;

import java.util.Map;

public interface BeanScanner {
    Map<Class<?>, BeanCreator> scan2(Object... basePackages);
}
