package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Inject;
import nextstep.exception.NotFoundConstructorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private static class BeanFactoryHolder {
        static final BeanFactory instance = new BeanFactory();
    }

    private BeanFactory() {
    }

    public static BeanFactory getInstance() {
        return BeanFactoryHolder.instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getAnnotatedBeans(Class<? extends Annotation> annotation) {
        return Collections.unmodifiableMap(beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }


    protected void initialize(Set<Class<?>> preInstantiateBeans) {
        logger.debug("Initialize BeanFactory!");
        preInstantiateBeans.forEach(bean -> {
            logger.debug(bean.getName());
            try {
                if (!beans.containsKey(bean)) {
                    createInstance(bean, preInstantiateBeans);
                }
            } catch (Exception e) {
                logger.error("BeanFactory Initialize Error :  {}", e);
            }
        });
    }

    private void createInstance(Class<?> bean, Set<Class<?>> preInstantiateBeans) throws Exception {
        Constructor constructor = findConstructor(bean);

        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameterType = parameterTypes[i];
            logger.debug("param type : {}", parameterType.getName());

            Class concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
            if (!beans.containsKey(concreteClass)) {
                createInstance(concreteClass, preInstantiateBeans);
            }
            params[i] = beans.get(concreteClass);
        }

        beans.put(bean, constructor.newInstance(params));
        logger.debug("create : {}", bean.getName());
    }

    private Constructor findConstructor(Class<?> bean) {
        logger.debug("Find Constructor : {}", bean.getName());

        Constructor<?>[] constructors = bean.getConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }

        List<Constructor> list = Arrays.stream(bean.getConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        if (list.size() == 1) {
            return list.get(0);
        }

        throw new NotFoundConstructorException("올바른 생성자를 찾을 수 없습니다.");
    }
}
