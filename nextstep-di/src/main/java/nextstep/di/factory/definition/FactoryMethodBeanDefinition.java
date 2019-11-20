package nextstep.di.factory.definition;

import java.lang.reflect.Method;

public class FactoryMethodBeanDefinition implements BeanDefinition {

    private Object declaredClassInstance;
    private Method factoryMethod;

    public FactoryMethodBeanDefinition(Object declaredClassInstance, Method factoryMethod) {
        this.declaredClassInstance = declaredClassInstance;
        this.factoryMethod = factoryMethod;
    }

    @Override
    public Class<?> getName() {
        return factoryMethod.getReturnType();
    }

    @Override
    public Class<?>[] getParams() {
        return factoryMethod.getParameterTypes();
    }

    @Override
    public Object createBean(Object... initArgs) throws Exception {
        return factoryMethod.invoke(declaredClassInstance, initArgs);
    }

    @Override
    public boolean matchClass(Class<?> clazz) {
        return factoryMethod.getReturnType().equals(clazz);
    }

    @Override
    public String toString() {
        return "FactoryMethodBeanDefinition{" +
                "factoryMethod=" + factoryMethod +
                '}';
    }
}
