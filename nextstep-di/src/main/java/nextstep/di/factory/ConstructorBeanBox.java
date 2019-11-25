package nextstep.di.factory;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class ConstructorBeanBox implements BeanBox<Constructor<?>> {

    private Class<?> preInstanticateBean;
    private Constructor<?> constructor;

    public ConstructorBeanBox(Class<?> preInstanticateBean) {
        this.preInstanticateBean = preInstanticateBean;
        this.constructor = BeanFactoryUtils.getInjectedConstructor(preInstanticateBean);
    }

    @Override
    public boolean hasParams() {
        return Objects.nonNull(constructor);
    }

    @Override
    public Constructor<?> getInvoker() {
        return constructor;
    }

    @Override
    public Object instantiate() {
        return BeanUtils.instantiateClass(preInstanticateBean);
    }

    @Override
    public Object putParameterizedObject(Class<?> preInstanticateBean, Object[] params) {
        Constructor<?> constructor = getInvoker();
        return BeanUtils.instantiateClass(constructor, params);
    }

    @Override
    public int getParameterCount() {
        return constructor.getParameterCount();
    }
}
