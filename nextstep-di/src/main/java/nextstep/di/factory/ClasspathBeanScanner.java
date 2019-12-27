package nextstep.di.factory;

import org.reflections.Reflections;

import java.util.*;

public class ClasspathBeanScanner implements BeanScanner {
    private Reflections reflections;
    private List<Class> annotations;

    public ClasspathBeanScanner(List<Class> annotations, Object... basePackage) {
        this.annotations = annotations;
        this.reflections = new Reflections(basePackage);
    }

    @Override
    public Map<Class<?>, BeanDefinition> scanBeans() {
        Map<Class<?>, BeanDefinition> maps = new HashMap<>();
        for (Class<?> preInitiatedBean : scanClassesByAnnotation()) {
            maps.put(preInitiatedBean, new ConstructorDefinition(preInitiatedBean));
        }
        return maps;
    }

    private Set<Class<?>> scanClassesByAnnotation() {
        Set<Class<?>> preInitiatedBeans = new HashSet<>();
        for (Class clazz : annotations) {
            preInitiatedBeans.addAll(reflections.getTypesAnnotatedWith(clazz));
        }
        return preInitiatedBeans;
    }

}
