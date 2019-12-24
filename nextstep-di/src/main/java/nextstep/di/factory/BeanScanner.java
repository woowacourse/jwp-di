package nextstep.di.factory;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;
    private List<Class> annotations;

    public BeanScanner(List<Class> annotations, Object... basePackage) {
        this.annotations = annotations;
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, BeanDefinition> scanBeans() {
        Map<Class<?>, BeanDefinition> maps = new HashMap<>();
        Set<Class<?>> preInitiatedBeans = new HashSet<>();
        for (Class clazz : annotations) {
            preInitiatedBeans.addAll(reflections.getTypesAnnotatedWith(clazz));
        }

        for (Class<?> preInitiatedBean : preInitiatedBeans) {
            maps.put(preInitiatedBean, new ConstructorDefinition(preInitiatedBean));
        }
        return maps;
    }
}
