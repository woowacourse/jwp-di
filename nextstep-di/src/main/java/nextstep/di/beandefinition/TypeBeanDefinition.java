package nextstep.di.beandefinition;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.BeanFactoryUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;

public class TypeBeanDefinition<T> implements BeanDefinition {
    private final Class<T> type;
    private final Constructor<T> constructor;

    private TypeBeanDefinition(Class<T> type) {
        this.type = type;
        this.constructor = BeanFactoryUtils.getBeanConstructor(type);
    }

    public static TypeBeanDefinition of(Class<?> type) {
        return new TypeBeanDefinition(type);
    }

    @Override
    public Class<?> getBeanType() {
        return type;
    }

    @Override
    public Set<Class<?>> getDependantTypes() {
        return Sets.newHashSet(constructor.getParameterTypes());
    }

    @Override
    public T create(BeanFactory beanFactory) {
        return BeanUtils.instantiateClass(constructor, collectParameterBeans(beanFactory));
    }

    private Object[] collectParameterBeans(BeanFactory factory) {
        return Arrays.asList(constructor.getParameterTypes()).stream()
                .map(type -> factory.getBean(type))
                .toArray(Object[]::new);
    }

    @Override
    public String toString() {
        return "TypeBeanDefinition{" +
                "type=" + type +
                ", constructor=" + constructor +
                '}';
    }
}
