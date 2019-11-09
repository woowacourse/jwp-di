package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.supports.TopologySort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiatedTypes;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiatedTypes) {
        this.preInstantiatedTypes = preInstantiatedTypes;
    }

    public void initialize() {
        addBeans(createTopologySort().calculateReversedOrders());
    }

    private TopologySort<Class<?>> createTopologySort() {
        return new TopologySort<>(
                preInstantiatedTypes,
                type -> getParameterTypes(BeanFactoryUtils.getBeanConstructor(type)),
                () -> {
                    throw new IllegalArgumentException("사이클..!!");
                });
    }

    private void addBeans(List<Class<?>> types) {
        for (Class<?> type : types) {
            addBean(type);
        }
    }

    private void addBean(Class<?> type) {
        beans.put(type, instantiate(BeanFactoryUtils.getBeanConstructor(type)));
    }

    private Object instantiate(Constructor<?> constructor) {
        return BeanUtils.instantiateClass(constructor, getBeans(getParameterTypes(constructor)));
    }

    private List<Class<?>> getParameterTypes(Constructor<?> constructor) {
        return BeanFactoryUtils.findConcreteClasses(Arrays.asList(constructor.getParameterTypes()), preInstantiatedTypes);
    }

    private Object[] getBeans(List<Class<?>> parameterTypes) {
        return parameterTypes.stream()
                .map(type -> getBean(type))
                .toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getBeans(Predicate<Class<?>> predicate) {
        return beans.keySet().stream()
                .filter(predicate)
                .collect(Collectors.toMap(type -> type, this::getBean));
    }
}
