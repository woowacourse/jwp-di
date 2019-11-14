package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements MvcApplicationContext {
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public AnnotationConfigApplicationContext(Class<?>... configClass) {
        Map<Class<?>, BeanCreator> componentsCreators = ClassBeanScanner.scan(Arrays.stream(configClass)
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class).basePackages())
                .map(Set::of)
                .flatMap(Set::stream)
                .distinct()
                .toArray());
        BeanFactory bf = BeanFactory.getInstance();
        bf.initialize(componentsCreators, beans);

        Map<Class<?>, BeanCreator> beansCreators = MethodBeanScanner.scan(configClass);
        bf.initialize(beansCreators, beans);
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
