package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.beancreator.BeanCreator;
import nextstep.di.factory.exception.ImplClassNotFoundException;
import nextstep.di.factory.exception.PrimitiveTypeInjectionFailException;
import nextstep.di.factory.exception.RecursiveFieldException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private Map<Class<?>, BeanCreator> beanCreators;
    private Map<Class<?>, Object> beans;

    public BeanFactory(Map<Class<?>, BeanCreator> beanCreators) {
        this(beanCreators, Maps.newHashMap());
    }

    public BeanFactory(Map<Class<?>, BeanCreator> beanCreators, Map<Class<?>, Object> initialBeans) {
        this.beans = initialBeans;
        this.beanCreators = beanCreators;
    }

    public Map<Class<?>, Object> initializeBeans() {
        for (Class<?> clazz : beanCreators.keySet()) {
            if (!beans.containsKey(clazz)) {
                beans.put(clazz, instantiate(clazz, Sets.newHashSet(clazz)));
            }
        }
        return Collections.unmodifiableMap(beans);
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

    private Class<?> findImplClass(Class<?> interfaze) {
        return beanCreators.keySet().parallelStream()
                .filter(bean -> !interfaze.equals(bean))
                .filter(interfaze::isAssignableFrom)
                .findFirst()
                .orElseThrow(ImplClassNotFoundException::new);
    }
}