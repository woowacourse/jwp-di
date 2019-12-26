package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorDefinition implements BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(ConstructorDefinition.class);

    private Constructor constructor;

    public ConstructorDefinition(Class<?> clazz) {
        this.constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (this.constructor == null) {
            try {
                this.constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Object createBean(Object... objects) {
        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
