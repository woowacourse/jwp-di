package nextstep.di.factory.strategy;

import nextstep.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodBeanCreationStrategy implements  BeanCreationStrategy{
    private static final Logger log = LoggerFactory.getLogger(MethodBeanCreationStrategy.class);
    private final Set<Method> methods;
    private final Set<Class<?>> methodBeanClass;



    public MethodBeanCreationStrategy(Set<Method> methods, Set<Class<?>> methodBeanClass) {
        this.methods = methods;
        this.methodBeanClass = methodBeanClass;
        log.debug("method beans: {}", methods);
    }

    private Set<Method> getMethods(Set<Class<?>> preInstantiateBeans) {
        Set<Class<?>> configurationBeans = preInstantiateBeans.stream()
                .filter(key -> key.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());

        log.debug("configuration beans: {}", configurationBeans);

        return configurationBeans.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean canHandle(Class<?> clazz) {
        return methodBeanClass.contains(clazz);
    }

    @Override
    public List<Class<?>> getDependencyTypes(Class<?> clazz) {
        Method method = getMethod(clazz);
        return Arrays.asList(method.getParameterTypes());
    }

    @Override
    public Object createBean(Class<?> clazz, List<Object> parameterInstances,  Map<Class<?>, Object> beans) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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
                    .orElseThrow(IllegalArgumentException::new);
    }
}
