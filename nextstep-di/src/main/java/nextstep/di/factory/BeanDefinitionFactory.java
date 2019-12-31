package nextstep.di.factory;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanDefinitionFactory {

    public static Map<Class<?>, BeanDefinition> createBeanDefinitions(Set<Class<?>> preInstantiateClasses) {
        Map<Class<?>, BeanDefinition> definitions = Maps.newHashMap();

        List<BeanDefinitionCreator> beanDefinitionCreators = Arrays.asList(new ClassBeanDefinitionCreator(),
                new MethodBeanDefinitionCreator());

        for (BeanDefinitionCreator beanDefinitionCreator : beanDefinitionCreators) {
            definitions.putAll(beanDefinitionCreator.create(preInstantiateClasses));
        }

        return definitions;
    }
}
