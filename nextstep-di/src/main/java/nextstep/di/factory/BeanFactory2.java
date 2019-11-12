package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.exception.DefaultConstructorInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory2 {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory2.class);


    // Refactoring 하는 부분
    private List<BeanDefinition> preBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory2(Set<BeanDefinition> beanDefinitions) {
//        this.preBeans = beanDefinitions.stream()
//                .collect(Collectors.toMap(BeanDefinition::getClazz, Function.identity()));
        this.preBeans = new ArrayList<>(beanDefinitions);
        // TODO
        //initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

//    public void initialize() {
//        for (BeanDefinition preBean : preBeans) {
//
//            Method[] methods = preBean.getClass().getMethods();
//            for (Method targetMethod : methods) {
//                beans.put(preBean.getClass(), createInstance(targetMethod));
//            }
//        }
//    }

//    private Object createInstance(Method method) {
//        Parameter[] parameters = method.getParameters();
//        Constructor<?> constructor = getConstructor(method.getClass());
//        Object classInstance = BeanUtils.instantiateClass(constructor);
//        List<Object> arguments = Arrays.stream(parameters)
//                .map(parameter -> getOrCreateInstance(parameter.getType()))
//                .collect(Collectors.toList());
//        try {
//            return method.invoke(classInstance, arguments);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//    }

// TODO
//    private Object getOrCreateInstance(Class<?> clazz) {
//        if (beans.containsKey(clazz)) {
//            return getBean(clazz);
//        }
//
//        BeanDefinition beanDefinition = preBeans.stream()
//                .filter(prebean -> prebean.getClass() == clazz)
//                .findFirst()
//                .orElseThrow(RuntimeException::new);
//        Object instance = createInstance(beanDefinition);
//        beans.put(beanDefinition.getClass(), instance);
//        return instance;
//    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            return getDefaultConstructor(clazz);
        }
        return injectedConstructor;
    }

    private Constructor getDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error("Error : {0}", e);
            throw new DefaultConstructorInitException(e);
        }
    }
}
