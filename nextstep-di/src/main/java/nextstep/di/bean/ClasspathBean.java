package nextstep.di.bean;

import nextstep.di.factory.BeanFactoryUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class ClasspathBean implements BeanDefinition {
    private Constructor<?> constructor;
    private Class[] parameterTypes;

    public ClasspathBean(Class<?> clazz) {
        this.constructor = getConstructor(clazz);
        this.parameterTypes = this.constructor.getParameterTypes();
    }

    @Override
    public Object getInstance(Object[] params) {
        return BeanUtils.instantiateClass(constructor, params);
    }

    @Override
    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (Objects.isNull(injectedConstructor)) {
            try {
                return clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return injectedConstructor;
    }
}
