package nextstep.di.definition;

import nextstep.di.factory.BeanFactoryUtils;
import org.reflections.ReflectionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class ClasspathBeanDefinition implements BeanDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(ClasspathBeanDefinition.class);

    private final Class<?> clazz;
    private final Class<?>[] parameterTypes;
    private final Constructor<?> constructor;

    public ClasspathBeanDefinition(final Class<?> clazz) {
        this.clazz = clazz;
        this.constructor = getConstructorFrom(clazz);
        this.parameterTypes = constructor.getParameterTypes();
    }

    private Constructor<?> getConstructorFrom(final Class<?> clazz) {
        final Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (Objects.isNull(injectedConstructor)) {
            return getDefaultConstructor(clazz);
        }
        return injectedConstructor;
    }

    private Constructor<?> getDefaultConstructor(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (final Exception e) {
            LOG.error("Cannot find default constructor : {}", e.getMessage());
            throw new ReflectionsException(e);
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
    public Object instantiate(final Object[] parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (final Exception e) {
            throw new ReflectionsException(e);
        }
    }

}
