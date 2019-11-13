package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.instantiation.InstantiationMethod;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class BeanCreateMatcher {
    private Map<Class<?>, InstantiationMethod> beanGenerationMethod = Maps.newHashMap();

    public void put(Class<?> clazz, InstantiationMethod instantiationMethod) {
        beanGenerationMethod.put(clazz, instantiationMethod);
    }

    public InstantiationMethod get(Class<?> clazz) {
        return beanGenerationMethod.get(clazz);
    }

    public boolean containsKey(Class<?> key) {
        return beanGenerationMethod.containsKey(key);
    }

    public Set<Class<?>> keySet() {
        return beanGenerationMethod.keySet();
    }

    public Set<Map.Entry<Class<?>, InstantiationMethod>> entrySet() {
        return beanGenerationMethod.entrySet();
    }

    public void forEach(BiConsumer<? super Class<?>, ? super InstantiationMethod> action) {
        beanGenerationMethod.forEach(action);
    }

    public Collection<InstantiationMethod> values() {
        return beanGenerationMethod.values();
    }
}
