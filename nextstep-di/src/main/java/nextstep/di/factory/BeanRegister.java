package nextstep.di.factory;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public class BeanRegister {
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private BeanRegister() {
    }

    protected static BeanRegister getInstance() {
        return BeanRegisterHolder.instance;
    }

    protected <T> T get(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    protected Set<Map.Entry<Class<?>, Object>> entrySet() {
        return beans.entrySet();
    }

    protected boolean containsKey(Object key) {
        return beans.containsKey(key);
    }

    protected void put(Class<?> key, Object value) {
        beans.put(key, value);
    }

    private static class BeanRegisterHolder {
        static final BeanRegister instance = new BeanRegister();
    }
}
