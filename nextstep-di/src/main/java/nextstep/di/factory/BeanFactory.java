package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.bean.BeanDefinition;
import nextstep.di.factory.bean.MethodBeanDefinition;
import nextstep.di.factory.exception.BeanInvokeException;
import nextstep.di.factory.exception.InvalidBeanClassTypeException;
import nextstep.di.factory.exception.InvalidBeanTargetConstructorException;
import nextstep.di.factory.exception.InvalidBeanTargetException;
import nextstep.di.factory.scanner.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, BeanDefinition> targetBeanDefinitions = Maps.newHashMap();
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Scanner... scanners) {
        setTargetBeanDefinitions(scanners);
    }

    private void setTargetBeanDefinitions(Scanner[] scanners) {
        Arrays.stream(scanners)
                .map(Scanner::scan)
                .flatMap(Collection::stream)
                .forEach(beanDefinition -> targetBeanDefinitions.put(beanDefinition.getBeanClass(), beanDefinition));
    }

    public Map<Class<?>, Object> getBeansWithType(Class<? extends Annotation> type) {
        return this.beans.entrySet().stream()
                .filter(bean -> bean.getKey().isAnnotationPresent(type))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (BeanDefinition beanDefinition : targetBeanDefinitions.values()) {
            enrollBean(beanDefinition);
        }
    }

    private Object enrollBean(BeanDefinition beanDefinition) {
        if (isBeanExists(beanDefinition.getBeanClass())) {
            return getBean(beanDefinition.getBeanClass());
        }

        Object object = createBeanInstance(beanDefinition);
        beans.put(beanDefinition.getBeanClass(), object);
        return object;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        if (isNotBeanTarget(beanDefinition.getBeanClass())) {
            throw new InvalidBeanTargetException();
        }
        try {
            List<Object> paramInstances = initParameters(beanDefinition);

            if (beanDefinition instanceof MethodBeanDefinition) {
                MethodBeanDefinition methodBeanDefinition = (MethodBeanDefinition) beanDefinition;
                Method method = methodBeanDefinition.getMethod();
                return method.invoke(methodBeanDefinition.getMethodDeclaringObject(), paramInstances.toArray());
            }
            Constructor<?> constructor = beanDefinition.getInjectedConstructor();

            return BeanUtils.instantiateClass(constructor, paramInstances.toArray());
        } catch (NoSuchMethodException e) {
            throw new InvalidBeanTargetConstructorException(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanInvokeException(e);
        }
    }

    private boolean isNotBeanTarget(Class<?> clazz) {
        return !targetBeanDefinitions.containsKey(clazz);
    }

    private boolean isBeanExists(Class<?> bean) {
        return beans.containsKey(bean);
    }

    private List<Object> initParameters(BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class[] parameterClasses = beanDefinition.getBeanParameterClasses();
        return Arrays.stream(parameterClasses)
                .map((Class parameterClass) -> createBean(parameterClass, beanDefinition))
                .collect(Collectors.toList());
    }

    private Object createBean(Class<?> parameterClass, BeanDefinition beanDefinition) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterClass, targetBeanDefinitions.keySet());
        Object bean = beans.get(concreteClass);
        if (Objects.isNull(bean)) {
            bean = enrollBean(targetBeanDefinitions.get(concreteClass));
            beans.put(concreteClass, bean);
        }
        return bean;
    }
}
