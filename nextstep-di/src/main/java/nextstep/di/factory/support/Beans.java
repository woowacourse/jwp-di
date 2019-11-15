package nextstep.di.factory.support;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.BeanNotExistException;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toSet;

public class Beans {
    private Map<Class<?>, Object> beans;

    public Beans() {
        this.beans = Maps.newHashMap();
    }

    public <T> T get(Class<T> clazz) {
        return (T) Optional.ofNullable(beans.get(clazz))
                .orElseThrow(BeanNotExistException::new);
    }

    public void put(Class<?> clazz, Supplier instanceSupplier) {
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, instanceSupplier.get());
        }
    }

    public boolean contains(Class<?> clazz) {
        return beans.containsKey(clazz);
    }

    public Set<Class<?>> getSupportedClass(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toSet());
    }
}
