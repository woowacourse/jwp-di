package nextstep.di.factory.beandefinition;

import nextstep.di.factory.BeanFactoryUtils;
import org.reflections.ReflectionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClasspathBeanDefinition implements BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanDefinition.class);

    private final Class<?> clazz;
    private final Class<?>[] parameterTypes;
    private final Constructor<?> constructor;

    public ClasspathBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.constructor = getConstructorFrom(clazz);
        this.parameterTypes = constructor.getParameterTypes();
    }

    private Constructor<?> getConstructorFrom(Class<?> clazz) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            return getDefaultConstructor(clazz);
        }
        return injectedConstructor;
    }

    private Constructor<?> getDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error("Cannot find default constructor! : {}", e.getMessage());
            throw new ReflectionsException(e.getMessage(), e);
        }
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object instantiate(Object[] parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionsException(e);
        }
    }
}
