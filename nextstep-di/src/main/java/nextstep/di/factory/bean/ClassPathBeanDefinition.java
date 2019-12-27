package nextstep.di.factory.bean;

import nextstep.annotation.Inject;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.reflections.ReflectionUtils.*;

public class ClassPathBeanDefinition implements BeanDefinition {
    private Class<?> clazz;

    public ClassPathBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public Class<?>[] getBeanParameterClasses() throws NoSuchMethodException {
        Constructor<?> constructor = getInjectedConstructor();
        if (constructor.getParameterCount() == 0) {
            return new Class<?>[0];
        }
        return getInjectedConstructor().getParameterTypes();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<?> getInjectedConstructor() throws NoSuchMethodException {
        Set<Constructor> injectedConstructor = getAllConstructors(clazz, withAnnotation(Inject.class));
        if (injectedConstructor.size() == 0) {
            return clazz.getDeclaredConstructor();
        }
        return injectedConstructor.iterator()
                .next();
    }
}
