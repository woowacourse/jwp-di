package nextstep.di;

import nextstep.di.factory.BeanFactoryUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class ClasspathBeanInitiator implements BeanInitiator {
    private final Class<?> preInstantiatedType;

    public ClasspathBeanInitiator(Class<?> preInstantiatedType) {
        this.preInstantiatedType = preInstantiatedType;
    }

    @Override
    public Object instantiate(Object... beans) {
        return BeanUtils.instantiateClass(getBeanConstructor(preInstantiatedType), beans);
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        return Arrays.asList(getBeanConstructor(preInstantiatedType).getParameterTypes());
    }

    private Constructor<?> getBeanConstructor(Class<?> type) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(type);
        if (BeanFactoryUtils.getInjectedConstructor(type) == null) {
            return getDefaultConstructor(type);
        }
        return constructor;
    }

    private Constructor getDefaultConstructor(Class<?> type) {
        try {
            return type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("기본 생성자를 가져올 수 없습니다.");
        }
    }
}
