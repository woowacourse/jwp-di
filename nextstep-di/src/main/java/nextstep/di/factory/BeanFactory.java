package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Set<Class<?>> visited;

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        visited = Sets.newHashSet();

        for (Class<?> root : preInstanticateBeans) {
            if (beans.containsKey(root)) {
                continue;
            }
            instantiateClass(root);
        }
    }

    private void instantiateClass(Class<?> clazz) {
        if (visited.contains(clazz)) {
            if (!beans.containsKey(clazz)) {
                throw new IllegalArgumentException("사이클..!!");
            }

            return;
        }
        visited.add(clazz);

        // concrete
        Constructor<?> constructor = (BeanFactoryUtils.getInjectedConstructor(clazz) != null) ? BeanFactoryUtils.getInjectedConstructor(clazz) : clazz.getConstructors()[0];

        for (Class<?> parameterType : getParameterTypes(constructor)) {
            instantiateClass(parameterType);
        }

        // 생성자 채우기
        beans.put(clazz, instantiate(constructor));
    }

    private List<Class<?>> getParameterTypes(Constructor<?> constructor) {
        return findConcreteClass(Arrays.asList(constructor.getParameterTypes()));
    }

    private List<Class<?>> findConcreteClass(List<Class<?>> classes) {
        return classes.stream()
                .map(parameter -> BeanFactoryUtils.findConcreteClass(parameter, preInstanticateBeans))
                .collect(Collectors.toList());
    }

    private Object instantiate(Constructor<?> constructor) {
        return BeanUtils.instantiateClass(constructor, getBeans(getParameterTypes(constructor)));
    }

    private Object[] getBeans(List<Class<?>> parameterTypes) {
        return parameterTypes.stream()
                .map(node -> beans.get(node)).toArray();
    }
}
