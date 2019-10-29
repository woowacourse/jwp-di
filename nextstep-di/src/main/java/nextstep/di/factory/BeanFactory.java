package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        preInstanticateBeans.forEach(aClass -> {
            try {
                beans.put(aClass, instanticate(aClass));
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException("빈 초기화 실패");
            }
        });
    }

    private Object instanticate(Class<?> aClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        // Bean 저장소에 clazz에 해당하는 인스턴스가 이미 존재하면 해당 인스턴스 반환
        if (beans.containsKey(aClass)) {
            return beans.get(aClass);
        }
        // clazz에 @Inject가 설정되어 있는 생성자를 찾는다. BeanFactoryUtils 활용
        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(aClass);

        // @Inject로 설정한 생성자가 없으면 Default 생성자로 인스턴스 생성 후 Bean 저장소에 추가 후 반환
        if (Objects.isNull(constructor)) {
            beans.put(aClass, BeanUtils.instantiateClass(aClass));
            return beans.get(aClass);
        }

        // @Inject로 설정한 생성자가 있으면 찾은 생성자를 활용해 인스턴스 생성 후 Bean 저장소에 추가 후 반환
        List<Object> arguments = new ArrayList<>();
        Class[] parameterTypes = constructor.getParameterTypes();

        for (Class clazz : parameterTypes) {
            Class cls = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
            arguments.add(instanticate(cls));
        }

        beans.put(aClass, constructor.newInstance(arguments.toArray()));
        return beans.get(aClass);
    }
}
