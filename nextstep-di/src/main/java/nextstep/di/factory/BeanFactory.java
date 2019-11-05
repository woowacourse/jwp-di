package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.BeanFactoryInitializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanConstructor> constructors;

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        Map<Class<?>, Object> instances = new HashMap<>();
        constructors = preInstantiateBeans.stream()
                .filter(cls -> cls.isAnnotationPresent(Configuration.class))
                .peek(cls -> instances.put(cls, BeanFactoryUtils.instantiate(cls)))
                .flatMap(cls -> Arrays.stream(cls.getMethods()).filter(method -> method.isAnnotationPresent(Bean.class)))
                .map(method -> new MethodBeanConstructor(method, instances.get(method.getDeclaringClass())))
                .collect(Collectors.toMap(MethodBeanConstructor::getReturnType, ctor -> ctor));

        constructors.putAll(
                preInstantiateBeans.stream()
                        .filter(cls -> !cls.isAnnotationPresent(Configuration.class))
                        .map(ClassBeanConstructor::of)
                        .collect(Collectors.toMap(ClassBeanConstructor::getReturnType, ctor -> ctor)));

        constructors.keySet().forEach(this::getOrInstantiate);
    }

    private Object getOrInstantiate(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        Object instance = tryInstantiateBean(clazz);
        beans.put(clazz, instance);
        return instance;
    }

    private Object tryInstantiateBean(Class<?> clazz) {
        try {
            return instantiateBean(clazz);
        } catch (Exception e) {
            logger.error("Error while instantiate bean", e);
            throw new BeanFactoryInitializeException(e);
        }
    }

    private Object instantiateBean(Class<?> clazz) throws Exception {
        BeanConstructor ctor = constructors.get(clazz);
        Object[] params = resolveConstructorParameters(ctor);

        return ctor.construct(params);
    }

    private Object[] resolveConstructorParameters(BeanConstructor ctor) {
        return Arrays.stream(ctor.getParameterTypes())
                .map(param -> BeanFactoryUtils.findConcreteClass(param, constructors.keySet())
                        .orElse(param))
                .map(this::getOrInstantiate)
                .toArray();
    }
}
