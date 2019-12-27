package nextstep.di.factory.bean;

import java.lang.reflect.Constructor;

public interface BeanDefinition {
    Class<?> getBeanClass();

    Class<?>[] getBeanParameterClasses() throws NoSuchMethodException;

    Constructor<?> getInjectedConstructor() throws NoSuchMethodException;
}
