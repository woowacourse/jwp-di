package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.MethodBeanDefinition;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodBeanScanner implements BeanScanner {

    private final List<BeanDefinition> beanDefinitions;

    public MethodBeanScanner(List<BeanDefinition> beanDefinitions) {
        this.beanDefinitions = initBeanDefinition(beanDefinitions);
    }

    private List<BeanDefinition> initBeanDefinition(List<BeanDefinition> beanDefinitions) {
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanClass)
                .map(Class::getMethods)
                .map(this::registerMethodBeanDefinition)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<BeanDefinition> registerMethodBeanDefinition(Method[] methods) {
        return Stream.of(methods)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(MethodBeanDefinition::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return Collections.unmodifiableList(beanDefinitions);
    }
}
