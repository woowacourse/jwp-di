package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.AccessibleConstructorException;
import nextstep.di.factory.exception.CreateBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        preInstantiateBeans.forEach(this::createBeanWithTryCatch);
    }

    private Object createBean(Class<?> beanClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        beanClass = BeanFactoryUtils.findConcreteClass(beanClass, preInstantiateBeans);
        Constructor<?> constructor =  BeanFactoryUtils.getInjectedConstructor(beanClass);

        //@inject 없을때
        if(constructor == null) {
//            Constructor<?> defaultConstructor = beanClass.getDeclaredConstructor();
//            Object newInstance = defaultConstructor.newInstance();
//            beans.put(beanClass, newInstance);
//            return newInstance;
            Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
            Constructor<?> beansConstructor = BeanFactoryUtils.findBeansConstructor(constructors, preInstantiateBeans);


            if (beansConstructor == null) {
                Constructor<?> defaultConstructor = BeanFactoryUtils.findDefaultConstructor(constructors);

                if (defaultConstructor == null) {
                    throw new AccessibleConstructorException();
                }
                Object newInstance = defaultConstructor.newInstance();
                beans.put(beanClass, newInstance);
                return newInstance;
            }

            Class<?>[] parameters = beansConstructor.getParameterTypes();
            List<Object> objects = Arrays.
                    stream(parameters)
                    .map(this::createBeanWithTryCatch).collect(Collectors.toList());

            Object newInstance = beansConstructor.newInstance(objects.toArray());

            beans.put(beanClass, newInstance);
            return newInstance;



        }
        //inject 있을때
        Class<?>[] parameters = constructor.getParameterTypes();
        List<Object> objects = Arrays.
                stream(parameters)
                .map(this::createBeanWithTryCatch).collect(Collectors.toList());

        Object newInstance =  constructor.newInstance(objects.toArray());

        beans.put(beanClass, newInstance);
        return newInstance;
    }

    private Object createBeanWithTryCatch(Class<?> parameter) {
        try {
            return createBean(parameter);
        } catch (Exception e) {
            throw new CreateBeanException(e);
        }
    }
}
