package nextstep.di.factory;

import java.util.Map;
import java.util.Set;

public interface BeanDefinitionCreator {

    Map<Class<?>, BeanDefinition> create(Set<Class<?>> preInstantiateClasses);
}
