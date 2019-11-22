package nextstep.di.factory.definition;

import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class BeanDefinitionMethod implements BeanDefinition {
    private final Method method;

    public BeanDefinitionMethod(final Method method) {
        this.method = method;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        List<Class<?>> classes = Lists.newArrayList(method.getDeclaringClass());
        classes.addAll(Lists.newArrayList(method.getParameterTypes()));

        return classes.toArray(new Class[0]);
    }

    // TODO Object를 어떻게 전달하지
    @Override
    public Object createBean(Object... objects) {
        Object object = objects[0];
        List<Object> parameters = Arrays.asList(objects).subList(1, objects.length);

        try {
            return method.invoke(object, parameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
