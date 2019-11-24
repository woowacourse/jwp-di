package nextstep.di.factory.strategy;

import nextstep.di.factory.exception.IllegalMethodBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigurationBeanCreationStrategy implements BeanCreationStrategy {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanCreationStrategy.class);
    private final Set<Method> methods;
    private final Set<Class<?>> configurationBeanClass;

    public ConfigurationBeanCreationStrategy(Set<Method> methods, Set<Class<?>> configurationBeanClass) {
        this.methods = methods;
        this.configurationBeanClass = configurationBeanClass;
        log.debug("method beans: {}", methods);
    }

    @Override
    public boolean canHandle(Class<?> clazz) {
        return configurationBeanClass.contains(clazz);
    }

    @Override
    public List<Class<?>> getDependencyTypes(Class<?> clazz) {
        Method method = getMethod(clazz);
        return Arrays.asList(method.getParameterTypes());
    }

    @Override
    public Object createBean(Class<?> clazz, List<Object> parameterInstances, Map<Class<?>, Object> beans) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        Method method = getMethod(clazz);

        Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
        instance = method.invoke(instance, parameterInstances.toArray());
        beans.put(clazz, instance);
        return instance;
    }

    private Method getMethod(Class<?> clazz) {
        return methods.stream()
                .filter(beanMethod -> beanMethod.getReturnType() == clazz)
                .findAny()
                .orElseThrow(IllegalMethodBeanException::new);
    }
}
