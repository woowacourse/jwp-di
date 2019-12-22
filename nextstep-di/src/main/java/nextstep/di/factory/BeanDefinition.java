package nextstep.di.factory;

import java.util.List;

public interface BeanDefinition {

    Class<?> getType();

    Class<?> getConfigType();

    BeanCreator getBeanCreator();

    List<Class<?>> getParameters();
}
