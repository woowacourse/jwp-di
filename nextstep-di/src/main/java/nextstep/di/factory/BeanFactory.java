package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.exception.ImplClassNotFoundException;
import nextstep.di.factory.exception.PrimitiveTypeInjectionFailException;
import nextstep.di.factory.exception.RecursiveFieldException;
import nextstep.di.factory.exception.UninitializedBeanFactoryException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public enum BeanFactory {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private Map<Class<?>, BeanCreator> beanCreators;
    private Map<Class<?>, Object> beans;

    public static BeanFactory getInstance() {
        return INSTANCE;
    }

    public void initialize(Map<Class<?>, BeanCreator> beanCreators) {
        beans = Maps.newHashMap();
        this.beanCreators = beanCreators;
        for (Class<?> clazz : beanCreators.keySet()) {
            initializeBean(clazz);
        }
    }

    private void initializeBean(Class<?> clazz) {
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, instantiate(clazz, Sets.newHashSet(clazz)));
        }
    }

    public void initialize(Map<Class<?>, BeanCreator> beanCreators, Map<Class<?>, Object> initialBeans) {
        beans = initialBeans;
        this.beanCreators = beanCreators;
        for (Class<?> clazz : beanCreators.keySet()) {
            initializeBean(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        validateInitialization();
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getControllers() {
        validateInitialization();
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for (Class<?> clazz : beans.keySet()) {
            addControllerFromBeans(controllers, clazz);
        }
        return controllers;
    }

    private void addControllerFromBeans(Map<Class<?>, Object> controllers, Class<?> clazz) {
        if (clazz.isAnnotationPresent(Controller.class)) {
            controllers.put(clazz, beans.get(clazz));
        }
    }

    private Object instantiate(Class<?> clazz, Set<Class<?>> history) {
        BeanCreator bc = beanCreators.get(clazz);
        Object[] params = createParams(bc.getParams(), history);
        return bc.instantiate(params);
    }

    private Object[] createParams(List<Class<?>> params, Set<Class<?>> history) {
        List<Object> realParams = Lists.newArrayList();
        for (Class<?> param : params) {
            validateNoRecursiveField(history, param);
            validateNoPrimitiveInjection(param);
            if (beans.containsKey(param)) {
                realParams.add(beans.get(param));
                continue;
            }
            param = !beanCreators.containsKey(param) && param.isInterface() ? findImplClass(param) : param;
            Object instance = instantiate(param, createUpdatedHistory(history, param));
            if (beanCreators.containsKey(param)) {
                beans.put(param, instance);
            }
            realParams.add(instance);
        }
        return realParams.toArray();
    }

    private Set<Class<?>> createUpdatedHistory(Set<Class<?>> history, Class<?> param) {
        Set<Class<?>> newHistory = Sets.newHashSet(history);
        newHistory.add(param);
        return newHistory;
    }

    private void validateNoPrimitiveInjection(Class<?> param) {
        if (param.isPrimitive()) {
            throw new PrimitiveTypeInjectionFailException();
        }
    }

    private void validateNoRecursiveField(Set<Class<?>> history, Class<?> param) {
        if (history.contains(param)) {
            throw new RecursiveFieldException();
        }
    }

    private void validateInitialization() {
        if (Objects.isNull(beanCreators) || Objects.isNull(beans)) {
            throw new UninitializedBeanFactoryException();
        }
    }

    private Class<?> findImplClass(Class<?> interfaze) {
        return beanCreators.keySet().parallelStream()
                .filter(bean -> !interfaze.equals(bean))
                .filter(interfaze::isAssignableFrom)
                .findFirst()
                .orElseThrow(ImplClassNotFoundException::new);
    }
}