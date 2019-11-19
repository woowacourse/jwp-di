package nextstep.di.registry;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public class BeanRegistry {
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public <T> T get(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Set<Map.Entry<Class<?>, Object>> entrySet() {
        return beans.entrySet();
    }

    public boolean containsKey(Object key) {
        return beans.containsKey(key);
    }

    public void put(Class<?> key, Object value) {
        beans.put(key, value);
    }

    public void put(Class<?> key) {
        put(key, null);
    }

    public Set<Class<?>> keySet() {
        return beans.keySet();
    }

    public boolean isEnrolled(Class<?> requiredType) {
        return containsKey(requiredType) && beans.get(requiredType) != null;
    }
}
