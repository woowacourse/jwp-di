package nextstep.di.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final Map<Class<?>, Method> configBeansToInstantiate;
    private final Set<Class<?>> classpathBeansToInstantiate;
    private final Map<Class<?>, Object> configBeanGenMethodContainers = new HashMap<>();
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    public BeanFactory(ApplicationContext applicationContext) {
        this.configBeansToInstantiate = applicationContext.getConfigBeansToInstantiate();
        this.classpathBeansToInstantiate = applicationContext.getClasspathBeansToInstantiate();
    }

    public BeanFactory initialize() {
        this.configBeansToInstantiate.values().stream().map(Method::getDeclaringClass)
                                                        .distinct()
                                                        .forEach(clazz -> {
                                                            try {
                                                                this.configBeanGenMethodContainers.put(
                                                                        clazz,
                                                                        clazz.getDeclaredConstructor().newInstance()
                                                                );
                                                            } catch (Exception e) {
                                                                logger.error(e.getMessage());
                                                            }
                                                        });
        Stream.of(
                this.classpathBeansToInstantiate,
                this.configBeansToInstantiate.keySet()
        ).flatMap(Collection::stream)
        .forEach(clazz -> {
            logger.debug("{}", clazz);
            storeAndRetrieveBean(clazz);
        });
        this.configBeanGenMethodContainers.clear();
        return this;
    }

    private Object storeAndRetrieveBean(Class<?> clazz) {
        return Optional.ofNullable(this.beans.get(clazz)).orElseGet(() ->
            this.beans.computeIfAbsent(clazz, key -> instantiateBean(clazz))
        );
    }

    private Object instantiateBean(Class<?> clazz) {
        return Optional.ofNullable(this.configBeansToInstantiate.get(clazz)).map(f -> {
            try {
                return f.invoke(
                        this.configBeanGenMethodContainers.get(f.getDeclaringClass()),
                        convertParamsToArgsWithBean(f)
                );
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }).orElseGet(() -> {
            final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(
                    clazz,
                    this.classpathBeansToInstantiate
            );
            return BeanFactoryUtils.getInjectedConstructor(concreteClass).map(this::runConstructor).orElseGet(() -> {
                try {
                    return concreteClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return null;
                }
            });
        });
    }

    private Object runConstructor(Constructor<?> cons) {
        try {
            return cons.newInstance(convertParamsToArgsWithBean(cons));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private Object[] convertParamsToArgsWithBean(Executable f) {
        return Stream.of(f.getParameterTypes()).map(this::storeAndRetrieveBean).toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) this.beans.get(requiredType);
    }

    public Map<Class<?>, Object> getAllBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return this.beans.entrySet().stream().filter(x -> x.getKey().isAnnotationPresent(annotation))
                                            .collect(
                                                    Collectors.toMap(
                                                            Map.Entry::getKey,
                                                            Map.Entry::getValue
                                                    )
                                            );
    }
}