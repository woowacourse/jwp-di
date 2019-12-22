package nextstep.di.factory;

import java.util.List;

public class ComponentDefinition extends AbstractBeanDefinition {
    ComponentDefinition(Class<?> type, BeanCreator beanCreator, List<Class<?>> parameters) {
        super(type, beanCreator, parameters);
    }
}
