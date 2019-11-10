package nextstep.di.factory;

import nextstep.annotation.Inject;
import nextstep.di.exception.BeanFactoryInitializeException;
import nextstep.di.exception.NotFoundConstructorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private static final int ONE = 1;
    private static final int ZERO = 0;

    private BeanRegister beanRegister;

    public BeanFactory() {
        beanRegister = BeanRegister.getInstance();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return beanRegister.get(requiredType);
    }

    public Map<Class<?>, Object> getAnnotatedBeans(Class<? extends Annotation> annotation) {
        return Collections.unmodifiableMap(beanRegister.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    protected void initialize(Set<Class<?>> preInstantiateBeans) {
        logger.debug("Initialize BeanFactory!");
        preInstantiateBeans.forEach(bean -> {
            logger.debug(bean.getName());
            try {
                if (!beanRegister.containsKey(bean)) {
                    createInstance(bean, preInstantiateBeans);
                }
            } catch (Exception e) {
                logger.error("BeanFactory Initialize Error :  {}", e);
                throw new BeanFactoryInitializeException(e);
            }
        });
    }

    private void createInstance(Class<?> bean, Set<Class<?>> preInstantiateBeans) throws Exception {
        Constructor constructor = findConstructor(bean);

        Class[] parameterTypes = constructor.getParameterTypes();
        List<Object> params = new ArrayList<>();
        for (Class parameterType : parameterTypes) {
            logger.debug("ParameterType : {}", parameterType.getName());

            Class concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
            if (!beanRegister.containsKey(concreteClass)) {
                createInstance(concreteClass, preInstantiateBeans);
            }
            params.add(beanRegister.get(concreteClass));
        }

        logger.debug("Create : {}", bean.getName());
        beanRegister.put(bean, constructor.newInstance(params.toArray()));
    }

    private Constructor findConstructor(Class<?> bean) {
        logger.debug("Find Constructor : {}", bean.getName());

        List<Constructor<?>> constructors = Arrays.asList(bean.getConstructors());
        if (constructors.size() == ONE) {
            return constructors.get(ZERO);
        }

        List<Constructor> injectedConstructors = constructors.stream()
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        if (injectedConstructors.size() == ONE) {
            return injectedConstructors.get(ZERO);
        }

        throw new NotFoundConstructorException("올바른 생성자를 찾을 수 없습니다.");
    }
}
