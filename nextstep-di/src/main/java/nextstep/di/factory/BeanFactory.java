package nextstep.di.factory;

import nextstep.stereotype.Controller;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanFactory {
    private final Set<Class<?>> preInstanticateBeans;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(final Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
        initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    private void initialize() {
        preInstanticateBeans.forEach(aClass -> {
            try {
                instanticate(aClass);
            } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException("Bean 초기화 실패");
            }
        });
    }

    private Object instanticate(final Class<?> aClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        // Bean 저장소에 aClass에 해당하는 인스턴스가 이미 존재하면 해당 인스턴스 반환
        if (beans.containsKey(aClass)) {
            return beans.get(aClass);
        }

        // aClass에 @Inject가 설정되어 있는 생성자를 찾는다. BeanFactoryUtils 활용
        final Constructor constructor = BeanFactoryUtils.getInjectedConstructor(aClass);

        // @Inject로 설정한 생성자가 없으면 Default 생성자로 인스턴스 생성 후 Bean 저장소에 추가 후 반환
        if (Objects.isNull(constructor)) {
            beans.put(aClass, BeanUtils.instantiateClass(aClass));
            return beans.get(aClass);
        }

        // @Inject로 설정한 생성자가 있으면 찾은 생성자를 활용해 인스턴스 생성 후 Bean 저장소에 추가 후 반환
        final List<Object> arguments = new ArrayList<>();
        final Class[] parameterTypes = constructor.getParameterTypes();
        for (final Class clazz : parameterTypes) {
            final Class cls = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
            arguments.add(instanticate(cls));
        }
        beans.put(aClass, constructor.newInstance(arguments.toArray()));
        return beans.get(aClass);
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> clazz : preInstanticateBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }
}
