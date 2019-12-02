package nextstep.di.initiator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ConfigurationBeanInitiator implements BeanInitiator {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanInitiator.class);

    private final Method method;
    private final Object object;

    public ConfigurationBeanInitiator(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    @Override
    public Object instantiate(Object... beans) {
        try {
            return method.invoke(object, beans);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("bean을 생성할 수 없습니다.");
        }
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        return Arrays.asList(method.getParameterTypes());
    }
}
