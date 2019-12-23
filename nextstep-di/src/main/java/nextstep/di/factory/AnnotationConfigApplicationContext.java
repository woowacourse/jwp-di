package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.ComponentScan;
import nextstep.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements MvcApplicationContext {
    private static final BeanFactory BEAN_FACTORY = BeanFactory.getInstance();

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public AnnotationConfigApplicationContext(Object... basePackages) {
        Map<Class<?>, BeanCreator> componentsCreators = ClassBeanScanner.scan(basePackages);

        BEAN_FACTORY.initialize(componentsCreators, beans);
    }

    public AnnotationConfigApplicationContext(Class<?>... configClass) {
        Map<Class<?>, BeanCreator> componentsCreators = ClassBeanScanner.scan(Arrays.stream(configClass)
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class).basePackages())
                .map(Set::of)
                .flatMap(Set::stream)
                .distinct()
                .toArray());

        Map<Class<?>, BeanCreator> beansCreators = MethodBeanScanner.scan(configClass);
        Map<Class<?>, BeanCreator> creators = Maps.newHashMap();
        creators.putAll(componentsCreators);
        creators.putAll(beansCreators);
        BEAN_FACTORY.initialize(creators, beans);
    }

    @Override
    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }

    @Override
    public Map<Class<?>, Object> getControllers() {
        return beans.entrySet()
                .parallelStream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}