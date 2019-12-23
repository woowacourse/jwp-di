package nextstep.di.beandefinition;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class MethodBeanDefinition implements BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(MethodBeanDefinition.class);

    private final Method method;

    private MethodBeanDefinition(Method method) {
        this.method = method;
    }

    public static MethodBeanDefinition of(Method method) {
        return new MethodBeanDefinition(method);
    }

    @Override
    public Class<?> getBeanType() {
        return method.getReturnType();
    }

    @Override
    public Set<Class<?>> getDependantTypes() {
        Set<Class<?>> dependantTypes = Sets.newHashSet(method.getParameterTypes());
        dependantTypes.add(method.getDeclaringClass());

        return dependantTypes;
    }

    @Override
    public Object create(BeanFactory beanFactory) {
        Object declaringBean = beanFactory.getBean(method.getDeclaringClass());
        Object[] parameterBeans = Arrays.asList(method.getParameterTypes()).stream()
                .map(parameterType -> beanFactory.getBean(parameterType))
                .toArray(Object[]::new);

        try {
            return method.invoke(declaringBean, parameterBeans);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("error: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "MethodBeanDefinition{" +
                "method=" + method +
                '}';
    }
}
