package nextstep.di.factory.scanner;

import nextstep.di.factory.bean.BeanDefinition;

import java.lang.reflect.Constructor;

public class NotAnnotated implements BeanDefinition {
    @Override
    public Class<?> getBeanClass() {
        return null;
    }

    @Override
    public Class<?>[] getBeanParameterClasses() throws NoSuchMethodException {
        return new Class[0];
    }

    @Override
    public Constructor<?> getInjectedConstructor() throws NoSuchMethodException {
        return null;
    }
}
