package nextstep.di.factory.definition;

import nextstep.di.factory.exception.InvalidBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodDefinition implements BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(MethodDefinition.class);

    private final Method method;

    public MethodDefinition(final Method method) {
        this.method = method;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object createBean(Object... objects) {
        try {
            Object instance = method.getDeclaringClass().newInstance();
            return method.invoke(instance, objects);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error("생성할 수 없는 빈입니다.");
            throw new InvalidBeanException();
        }
    }
}
