package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.validation.BeanValidationExecutor;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Method> preInstantiateBeansFromMethods;
    private Map<Class<?>, Object> beans;
    private Deque<Class<?>> beanRegisterHistory = new ArrayDeque<>();
    private BeanValidationExecutor beanValidationExecutor = new BeanValidationExecutor();

    public BeanFactory() {
        this.preInstantiateBeans = Sets.newHashSet();
        this.preInstantiateBeansFromMethods = Maps.newHashMap();
        this.beans = Maps.newHashMap();
    }

    public void appendPreInstantiateBeans(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans.addAll(preInstantiateBeans);
    }

    public void appendPreInstantiateBeanMethods(List<Method> preInstantiateBeanMethods) {
        preInstantiateBeanMethods.stream()
                .map(Method::getReturnType)
                .forEach(preInstantiateBeans::add);

        preInstantiateBeanMethods
                .forEach(method -> preInstantiateBeansFromMethods.put(method.getReturnType(), method));
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
        Executable executableMethod = getExecutableMethod(preInstantiateBean);

        Object bean = instantiateBeanOf(executableMethod);
        log.info("created bean: {}", bean);

        beans.put(preInstantiateBean, bean);
        beanRegisterHistory.pop();
    }

    private Executable getExecutableMethod(final Class<?> preInstantiateBean) {
        if (preInstantiateBeansFromMethods.containsKey(preInstantiateBean)) {
            return preInstantiateBeansFromMethods.get(preInstantiateBean);
        }

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);

        if (Objects.isNull(constructor)) {
            constructor = BeanFactoryUtils.getDefaultConstructor(preInstantiateBean);
        }

        constructor.setAccessible(true);
        return constructor;
    }

    private Object instantiateBeanOf(final Executable executableMethod) {
        List<Object> arguments = extractArgumentsOf(executableMethod);
        return BeanFactoryUtils.instantiate(executableMethod, arguments.toArray());
    }

    private List<Object> extractArgumentsOf(final Executable executableMethod) {
        List<Object> arguments = new ArrayList<>();

        for (Class<?> parameter : executableMethod.getParameterTypes()) {
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
