package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.DefaultBeanDefinition;
import nextstep.di.utils.BeanUtils;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClasspathBeanScanner implements BeanScanner {
    private static final Class[] COMPONENT_ANNOTATIONS = {Controller.class, Repository.class, Service.class};

    private final List<BeanDefinition> beanDefinitions;

    public ClasspathBeanScanner(Object... basePackages) {
        Set<Class<?>> classTypes = BeanUtils.getTypesAnnotatedWith(basePackages, COMPONENT_ANNOTATIONS);
        this.beanDefinitions = initBeanDefinitions(classTypes);
    }

    private List<BeanDefinition> initBeanDefinitions(final Set<Class<?>> classTypes) {
        return classTypes.stream()
                .map(DefaultBeanDefinition::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }
}
