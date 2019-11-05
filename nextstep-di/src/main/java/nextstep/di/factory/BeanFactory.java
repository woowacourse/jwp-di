package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiatedBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiatedBeans) {
        this.preInstantiatedBeans = preInstantiatedBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> clazz : preInstantiatedBeans) {
            beans.put(clazz, instantiate(clazz, new ArrayList<>(Collections.singletonList(clazz))));
        }
    }

    private Object instantiate(Class<?> clazz, List<Class<?>> history) {
        if (clazz.isInterface()) {
            throw new InterfaceCannotInstantiatedException();
        }

        Constructor[] ctors = clazz.getDeclaredConstructors();
        Optional<Constructor> injectCtor = Arrays.stream(ctors)
                .filter(ctor -> ctor.isAnnotationPresent(Inject.class))
                .findFirst();
        if (injectCtor.isEmpty()) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new NoDefaultConstructorException();
            }
        }
        Constructor ctor = injectCtor.get();
        List<Object> realParams = new ArrayList<>();
        for (Class<?> param : ctor.getParameterTypes()) {
            if (history.contains(param)) {
                throw new RecursiveFieldException();
            }

            if (param.isPrimitive()) {
                throw new PrimitiveTypeInjectionFailException();
            }

            if (beans.containsKey(param)) {
                realParams.add(beans.get(param));
                continue;
            }

            if (param.isInterface()) {
                param = findImplClass(param);
            }

            List<Class<?>> newHistory = new ArrayList<>(history);
            newHistory.add(param);
            realParams.add(instantiate(param, newHistory));
        }
        try {
            return ctor.newInstance(realParams.toArray());
        } catch (Exception e) {
            throw new ObjectInstantiationFailException(e);
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
