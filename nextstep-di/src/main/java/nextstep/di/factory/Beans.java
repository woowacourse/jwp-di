package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.BeanNotExistException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class Beans {
    private Map<Class<?>, Object> beans;

    public Beans() {
        this.beans = Maps.newHashMap();
    }

    public Object put(Class<?> clazz, Object instance) {
        return beans.put(clazz, instance);
    }

    public Object get(Class<?> clazz) {
        return Optional.ofNullable(beans.get(clazz))
                .orElseThrow(BeanNotExistException::new);
    }

    public Object instantiate(Class<?> clazz, Supplier supplier) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        beans.put(clazz, supplier.get());
        return beans.get(clazz);
    }
}
