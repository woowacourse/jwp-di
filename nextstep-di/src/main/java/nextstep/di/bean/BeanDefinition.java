package nextstep.di.bean;

public interface BeanDefinition {
    Object getInstance(Object[] params);

    Class[] getParameterTypes();
}
