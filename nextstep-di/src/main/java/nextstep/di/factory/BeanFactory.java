package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.validation.BeanValidationExecutor;
import nextstep.stereotype.Controller;
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
    private BeanValidationExecutor beanValidationExecutor = new BeanValidationExecutor();

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

        beanValidationExecutor.execute(preInstantiateBean, beanRegisterHistory);

        beanRegisterHistory.push(preInstantiateBean);
        Constructor<?> constructor = getConstructorOf(preInstantiateBean);
        constructor.setAccessible(true);

        Object bean = instantiateBeanOf(constructor);
        log.info("created bean: {}", bean);

        beans.put(preInstantiateBean, bean);
        beanRegisterHistory.pop();
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

    public Map<Class<?>, Object> getControllers() {
        return getBeansAnnotatedWith(Controller.class);
    }

    private Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        return beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
