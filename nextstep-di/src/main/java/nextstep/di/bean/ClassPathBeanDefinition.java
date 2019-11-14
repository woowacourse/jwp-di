package nextstep.di.bean;

import nextstep.di.factory.BeanFactoryUtils;
import nextstep.di.factory.exception.DefaultConstructorInitException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;

public class ClassPathBeanDefinition implements BeanDefinition {
    private Class<?> clazz;
    private Constructor<?> constructor;

    public ClassPathBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.constructor = initConstructor(clazz);
    }

    private Constructor<?> initConstructor(Class<?> clazz) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        return injectedConstructor == null ? getDefaultConstructor(clazz) : injectedConstructor;
    }

    private Constructor getDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new DefaultConstructorInitException(e);
        }
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public Object invoke(Object[] objects) {
        return BeanUtils.instantiateClass(constructor, objects);
    }

    @Override
    public Class<?>[] getParametersType() {
        return constructor.getParameterTypes();
    }
}
