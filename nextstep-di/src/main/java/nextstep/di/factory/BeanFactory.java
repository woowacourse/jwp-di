package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.supports.TopologySort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        TopologySort<Class<?>> topologySort = new TopologySort<>(
                preInstanticateBeans,
                clazz -> {
                    Constructor<?> constructor = (BeanFactoryUtils.getInjectedConstructor(clazz) != null) ? BeanFactoryUtils.getInjectedConstructor(clazz) : clazz.getConstructors()[0];
                    return getParameterTypes(constructor);
                },
                () -> {
                    throw new IllegalArgumentException("사이클..!!");
                });


        for (Class<?> clazz : topologySort.calculateReversedOrders()) {
            Constructor<?> constructor = (BeanFactoryUtils.getInjectedConstructor(clazz) != null) ? BeanFactoryUtils.getInjectedConstructor(clazz) : clazz.getConstructors()[0];
            beans.put(clazz, instantiate(constructor));
        }
    }

    private Object instantiate(Constructor<?> constructor) {
        return BeanUtils.instantiateClass(constructor, getBeans(getParameterTypes(constructor)));
    }

    private Object[] getBeans(List<Class<?>> parameterTypes) {
        return parameterTypes.stream()
                .map(node -> beans.get(node)).toArray();
    }

    private List<Class<?>> getParameterTypes(Constructor<?> constructor) {
        return findConcreteClass(Arrays.asList(constructor.getParameterTypes()));
    }

    private List<Class<?>> findConcreteClass(List<Class<?>> classes) {
        return classes.stream()
                .map(parameter -> BeanFactoryUtils.findConcreteClass(parameter, preInstanticateBeans))
                .collect(Collectors.toList());
    }
}
