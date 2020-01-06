package nextstep.di.definition;

import nextstep.di.factory.BeanFactoryUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ClassPathBeanDefinition implements BeanDefinition {
    private Class<?> clazz;

    public ClassPathBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object instantiate(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (Objects.isNull(constructor)) {
            return clazz.newInstance();
        }
        return constructor.newInstance(parameters);
    }

    @Override
    public Class<?> getClassType() {
        return clazz;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (Objects.isNull(constructor)) {
            return null;
        }
        return constructor.getParameterTypes();
    }
}
