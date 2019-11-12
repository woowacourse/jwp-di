package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.BeanFactoryUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

public class ConstructorInstantiation implements InstantiationMethod {
    private final Class<?> clazz;

    public ConstructorInstantiation(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object getInstance(BeanCreateMatcher beanCreateMatcher, Map<Class<?>, Object> beans) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            return beans.getOrDefault(clazz, BeanUtils.instantiateClass(clazz));
        }
        Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();
        Object[] parameterInstance = Arrays.stream(parameterTypes)
                .map(parameterType -> BeanFactoryUtils.findConcreteClass(parameterType, beanCreateMatcher.keySet()))
                .map(parameterType -> getBean(beanCreateMatcher, beans, parameterType))
                .toArray();
        return beans.getOrDefault(clazz, BeanUtils.instantiateClass(injectedConstructor, parameterInstance));
    }
}
