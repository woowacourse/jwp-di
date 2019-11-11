package nextstep.di.factory;

import nextstep.di.scanner.BeanScanner;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanFactory {
    private final Set<Class<?>> preInstantiateBeans = new HashSet<>();
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(final BeanScanner... beanScanners) {
        for (final BeanScanner scanner : beanScanners) {
            preInstantiateBeans.addAll(scanner.getBeans());
        }
        preInstantiateBeans.forEach(this::instantiate);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    @SuppressWarnings("unchecked")
    private Object instantiate(final Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        final Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        final Object bean = Objects.isNull(constructor)
                ? BeanUtils.instantiateClass(clazz)
                : BeanUtils.instantiateClass(constructor, getParameters(constructor));
        beans.put(clazz, bean);
        return bean;
    }

    private Object[] getParameters(final Constructor constructor) {
        final List<Object> parameters = new ArrayList<>();
        final Class[] parameterTypes = constructor.getParameterTypes();
        for (final Class clazz : parameterTypes) {
            final Class cls = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
            parameters.add(instantiate(cls));
        }
        return parameters.toArray();
    }

    @SuppressWarnings("unchecked")
    public Map<Class<?>, Object> getAnnotatedClasses(final Class aClass) {
        return preInstantiateBeans.stream()
                .filter(clazz -> clazz.isAnnotationPresent(aClass))
                .collect(Collectors.toUnmodifiableMap(Function.identity(), beans::get));
    }
}
