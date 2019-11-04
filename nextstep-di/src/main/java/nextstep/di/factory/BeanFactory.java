package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.CircularReferenceException;
import nextstep.di.factory.exception.IllegalAnnotationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Deque<Class<?>> beanRegisterHistory = new ArrayDeque<>();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    public void initialize() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            registerBean(preInstantiateBean);
        }
    }

    private void registerBean(Class<?> preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean)) {
            return;
        }

        checkBeanType(preInstantiateBean);
        checkCircularReference(preInstantiateBean);

        beanRegisterHistory.push(preInstantiateBean);
        Constructor<?> constructor = getConstructorOf(preInstantiateBean);
        constructor.setAccessible(true);

        Object bean = instantiateBeanOf(constructor);
        log.info("created bean: {}", bean);

        beans.put(preInstantiateBean, bean);
        beanRegisterHistory.pop();
    }

    private void checkBeanType(final Class<?> preInstantiateBean) {
        if (preInstantiateBean.isInterface()) {
            log.error("invalid annotation on {}", preInstantiateBean);
            throw new IllegalAnnotationException("invalid annotation");
        }
    }

    private void checkCircularReference(final Class<?> preInstantiateBean) {
        if (beanRegisterHistory.contains(preInstantiateBean)) {
            log.error("circular reference on {}", preInstantiateBean);
            throw new CircularReferenceException("circular reference");
        }
    }

    private Constructor<?> getConstructorOf(final Class<?> preInstantiateBean) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);

        if (Objects.isNull(constructor)) {
            return BeanFactoryUtils.getDefaultConstructor(preInstantiateBean);
        }
        return constructor;
    }

    private Object instantiateBeanOf(final Constructor<?> constructor) {
        List<Object> arguments = extractArgumentsOf(constructor);
        return BeanFactoryUtils.instantiate(constructor, arguments.toArray());
    }

    private List<Object> extractArgumentsOf(final Constructor<?> constructor) {
        List<Object> arguments = new ArrayList<>();
        for (Class<?> parameter : constructor.getParameterTypes()) {
            Class<?> concreteParameter = BeanFactoryUtils.findConcreteClass(parameter, preInstantiateBeans);

            if (isUnregisteredBean(concreteParameter)) {
                registerBean(concreteParameter);
            }
            arguments.add(beans.get(concreteParameter));
        }
        return arguments;
    }

    private boolean isUnregisteredBean(final Class<?> concreteParameter) {
        return !beans.containsKey(concreteParameter);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        return beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
