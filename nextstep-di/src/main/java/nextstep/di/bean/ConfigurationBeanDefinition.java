package nextstep.di.bean;

import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private Class<?> clazz;
    private Method method;

    public ConfigurationBeanDefinition(Class<?> clazz, Method method) {
        // ConfigurationBean 에서의 @Bean 의 타입은 메서드의 반환 타입이다
        this.clazz = method.getReturnType();
        this.method = method;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }


}
