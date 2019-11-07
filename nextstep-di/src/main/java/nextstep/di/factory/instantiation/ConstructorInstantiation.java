package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.BeanFactoryUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorInstantiation implements InstantiationMethod {
    private final Class<?> clazz;

    public ConstructorInstantiation(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object getInstance(BeanCreateMatcher beanCreateMatcher) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            return BeanUtils.instantiateClass(clazz);
        }
        Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();
        Object[] parameterInstance = Arrays.stream(parameterTypes)
                .map(parameterType -> BeanFactoryUtils.findConcreteClass(parameterType, beanCreateMatcher.keySet()))
                .map(parameterType -> beanCreateMatcher.get(parameterType).getInstance(beanCreateMatcher))
                .toArray();
        return BeanUtils.instantiateClass(injectedConstructor, parameterInstance);
    }
}
