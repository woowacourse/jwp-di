package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public enum BeanFactory {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private Set<Class<?>> preInstantiatedBeans;
    private Map<Class<?>, Object> beans;

    public static BeanFactory getInstance() {
        return INSTANCE;
    }

    public void initialize(Set<Class<?>> preInstantiatedBeans) {
        beans = Maps.newHashMap();
        this.preInstantiatedBeans = preInstantiatedBeans;
        for (Class<?> clazz : preInstantiatedBeans) {
            beans.put(clazz, instantiate(clazz, new ArrayList<>(Collections.singletonList(clazz))));
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
        for (Class<?> clazz : preInstantiatedBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }

    private Object instantiate(Class<?> clazz, List<Class<?>> history) {
        if (clazz.isInterface()) {
            throw new InterfaceCannotInstantiatedException();
        }
        Optional<Constructor> injectCtor = getInjectCtor(clazz.getDeclaredConstructors());
        if (injectCtor.isEmpty()) {
            return instantiateUsingDefaultCtor(clazz);
        }
        Constructor ctor = injectCtor.get();
        List<Object> realParams = createParamsOfInjectCtor(history, ctor);
        return instantiateUsingInjectCtor(ctor, realParams);
    }

    private Object instantiateUsingInjectCtor(Constructor ctor, List<Object> realParams) {
        try {
            return ctor.newInstance(realParams.toArray());
        } catch (Exception e) {
            throw new ObjectInstantiationFailException(e);
        }
    }

    private List<Object> createParamsOfInjectCtor(List<Class<?>> history, Constructor ctor) {
        List<Object> realParams = Lists.newArrayList();
        for (Class<?> param : ctor.getParameterTypes()) {
            validateNoRecursiveField(history, param);
            validateNoPrimitiveInjection(param);
            if (beans.containsKey(param)) {
                realParams.add(beans.get(param));
                continue;
            }
            param = param.isInterface() ? findImplClass(param) : param;
            realParams.add(instantiate(param, createUpdatedHistory(history, param)));
        }
        return realParams;
    }

    private List<Class<?>> createUpdatedHistory(List<Class<?>> history, Class<?> param) {
        List<Class<?>> newHistory = Lists.newArrayList(history);
        newHistory.add(param);
        return newHistory;
    }

    private Object instantiateUsingDefaultCtor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new NoDefaultConstructorException();
        }
    }

    private Optional<Constructor> getInjectCtor(Constructor[] ctors) {
        return Arrays.stream(ctors)
                .filter(ctor -> ctor.isAnnotationPresent(Inject.class))
                .findFirst();
    }

    private void validateNoPrimitiveInjection(Class<?> param) {
        if (param.isPrimitive()) {
            throw new PrimitiveTypeInjectionFailException();
        }
    }

    private void validateNoRecursiveField(List<Class<?>> history, Class<?> param) {
        if (history.contains(param)) {
            throw new RecursiveFieldException();
        }
    }

    private void validateInitialization() {
        if (Objects.isNull(preInstantiatedBeans) || Objects.isNull(beans)) {
            throw new UninitializedBeanFactoryException();
        }
    }

    private Class<?> findImplClass(Class<?> interfaze) {
        return preInstantiatedBeans.parallelStream()
                .filter(bean -> !interfaze.equals(bean))
                .filter(interfaze::isAssignableFrom)
                .findFirst()
                .orElseThrow(ImplClassNotFoundException::new);
    }
}
