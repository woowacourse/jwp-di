package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> preInstantiateBeans;
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    public BeanFactory initialize() {
        this.preInstantiateBeans.forEach(x -> this.beans.putIfAbsent(x, instantiateBean(x)));
        return this;
    }

    private Object instantiateBean(Class<?> clazz) {
        return BeanFactoryUtils.getInjectedConstructor(clazz).map(this::runConstructor).orElseGet(() -> {
            try {
                return BeanFactoryUtils.findConcreteClass(
                        clazz,
                        this.preInstantiateBeans
                ).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        });
    }

    private Object runConstructor(Constructor<?> cons) {
        try {
            return cons.newInstance(
                    Stream.of(
                            cons.getParameterTypes()
                    ).map(type -> this.beans.computeIfAbsent(type, key -> instantiateBean(type))).toArray()
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
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