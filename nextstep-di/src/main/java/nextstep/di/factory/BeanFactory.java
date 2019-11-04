package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.BeanCreateFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
    }

    private void createInstance(Constructor<?> constructor) {
        try {
            addBean(constructor);
        } catch (Exception e) {
            throw new BeanCreateFailException();
        }
    }

    private void addBean(Constructor<?> constructor) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        Class<?> clazz = constructor.getDeclaringClass();
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, constructor.newInstance(getParameterInstance(constructor).toArray()));
        }
    }

    private List<Object> getParameterInstance(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();

        return Arrays.stream(parameters)
            .map(parameter -> BeanFactoryUtils.findConcreteClass(parameter.getType(), preInstanticateBeans))
            .filter(implement -> beans.containsKey(implement))
            .map(implement -> beans.get(implement))
            .collect(Collectors.toList());
    }
}
