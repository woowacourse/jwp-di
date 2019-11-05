package nextstep.di.factory;

import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanFactory {
    private final Set<Class<?>> preInstantiateBeans;
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    public BeanFactory(Object... basePackages) {
        this.preInstantiateBeans = (new BeanScanner(basePackages)).getPreInstantiateBeans();
    }

    public BeanFactory initialize() {
        this.preInstantiateBeans.forEach(x -> this.beans.put(x, instantiateClass(x)));
        return this;
    }

    private Object instantiateClass(Class<?> clazz) {
        return BeanFactoryUtils.getInjectedConstructor(clazz).map(this::instantiateConstructor).orElseGet(() ->
                BeanUtils.instantiateClass(BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans))
        );
    }

    private Object instantiateConstructor(Constructor<?> cons) {
        return BeanUtils.instantiateClass(
                cons,
                Stream.of(
                        cons.getParameterTypes()
                ).map(x -> this.beans.computeIfAbsent(x, k -> instantiateClass(x))).toArray()
        );
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getAllBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return this.beans.entrySet().stream().filter(x -> x.getKey().isAnnotationPresent(annotation))
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}