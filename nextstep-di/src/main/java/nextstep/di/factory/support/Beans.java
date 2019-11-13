package nextstep.di.factory.support;

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

    public void instantiate(Class<?> clazz, Supplier instanceSupplier) {
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, instanceSupplier.get());
        }
    }
}
