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
    private final Map<Class<?>, Object> configBeanGenMethodsContainers = new HashMap<>();
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    public BeanFactory(ApplicationContext applicationContext) {
        this.configBeansToInstantiate = applicationContext.getConfigBeansToInstantiate();
        this.classpathBeansToInstantiate = applicationContext.getClasspathBeansToInstantiate();
    }

    public BeanFactory initialize() {
        prepareConfigBeanGenMethodsContainersDependency();
        Stream.of(
                this.classpathBeansToInstantiate,
                this.configBeansToInstantiate.keySet()
        ).flatMap(Collection::stream)
        .forEach(type -> {
            logger.debug("{}", type);
            storeAndRetrieveBean(type);
        });
        this.configBeanGenMethodsContainers.clear();
        return this;
    }

    private void prepareConfigBeanGenMethodsContainersDependency() {
        this.configBeansToInstantiate.values().stream().map(Method::getDeclaringClass)
                                                        .distinct()
                                                        .forEach(type -> {
                                                            try {
                                                                this.configBeanGenMethodsContainers.put(
                                                                        type,
                                                                        type.getDeclaredConstructor().newInstance()
                                                                );
                                                            } catch (Exception e) {
                                                                logger.error(e.getMessage());
                                                                throw new BeanFactoryInitFailedException(e);
                                                            }
                                                        });
    }

    private Object storeAndRetrieveBean(Class<?> clazz) {
        return Optional.ofNullable(this.beans.get(clazz)).orElseGet(() ->
            this.beans.computeIfAbsent(clazz, this::instantiateBean)
        );
    }

    private Object instantiateBean(Class<?> type) {
        return Optional.ofNullable(this.configBeansToInstantiate.get(type))
                        .map(this::instantiateConfigBean)
                        .orElseGet(() -> instantiateClasspathBean(type));
    }

    private Object instantiateConfigBean(Method f) {
        try {
            return f.invoke(
                    this.configBeanGenMethodsContainers.get(f.getDeclaringClass()),
                    convertParamsToArgsWithBean(f)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BeanFactoryInitFailedException(e);
        }
    }

    private Object instantiateClasspathBean(Class<?> type) {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(
                type,
                this.classpathBeansToInstantiate
        );
        return BeanFactoryUtils.getInjectedConstructor(concreteClass).map(this::runConstructor).orElseGet(() -> {
            try {
                return concreteClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new BeanFactoryInitFailedException(e);
            }
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