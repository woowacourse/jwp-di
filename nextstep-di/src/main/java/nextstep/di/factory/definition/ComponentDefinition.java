package nextstep.di.factory.definition;

import nextstep.annotation.Inject;
import nextstep.exception.BeanCreateException;
import nextstep.exception.InjectMethodNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.reflections.ReflectionUtils.withAnnotation;

public class ComponentDefinition implements BeanDefinition {
    private final Class<?> component;
    private final Constructor<?> injectedConstructor;

    public ComponentDefinition(Class<?> component) {
        this.component = component;

        injectedConstructor = resolveConstructor(component);
    }

    private Constructor<?> resolveConstructor(Class<?> component) {
        List<Constructor<?>> cons = Arrays.asList(component.getConstructors());
        if (cons.size() == 1) {
            return cons.get(0);
        }

        return cons.stream()
            .filter(withAnnotation(Inject.class))
            .findAny()
            .orElseThrow(InjectMethodNotFoundException::new);
    }

    @Override
    public Object generateBean(Object... params) {
        try {
            return injectedConstructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new BeanCreateException("빈 생성에 실패하였습니다.", e);
        }
    }

    @Override
    public Class<?> getType() {
        return component;
    }

    @Override
    public Class<?>[] getParams() {
        return injectedConstructor.getParameterTypes();
    }
}
