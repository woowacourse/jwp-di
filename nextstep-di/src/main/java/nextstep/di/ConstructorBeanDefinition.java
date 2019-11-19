package nextstep.di;

import com.google.common.collect.Sets;
import nextstep.di.exception.CreateBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class ConstructorBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(ConstructorBeanDefinition.class);

    private Class<?> clazz;
    private Constructor<?> constructor;

    public ConstructorBeanDefinition(Class<?> clazz, Constructor<?> constructor) {
        this.clazz = clazz;
        this.constructor = constructor;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public boolean isType(Class<?> type) {
        if (!type.isInterface()) {
            return clazz.equals(type);
        }

        Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
        return interfaces.contains(type);
    }

    @Override
    public Object createBean(Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("{} Bean instance fail. error message : {}", getType(), e.getMessage());
            throw new CreateBeanException(e);
        }
    }
}
