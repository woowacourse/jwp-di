package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.ComponentScan;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private BeanCreateMatcher matcher = new BeanCreateMatcher();
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Object... basePackages) {
        Object[] customPath = findCustomPath(basePackages);
        AnnotationBeanScanner annotationBeanScanner = new AnnotationBeanScanner(customPath);
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(customPath);
        annotationBeanScanner.scanBean(matcher, Controller.class, Service.class, Repository.class);
        configurationBeanScanner.scanBean(matcher);
    }

    private Object[] findCustomPath(Object[] basePackages) {
        Object[] customPackages = Arrays.stream(basePackages)
                .filter(object -> object instanceof Class)
                .map(clazz -> (((Class) clazz).getAnnotation(ComponentScan.class)))
                .map(annotation -> ((ComponentScan) annotation).basePackages())
                .toArray();

        return customPackages.length == 0 ? basePackages : customPackages;
    }


    public void initialize() {
        matcher.entrySet().forEach(entry -> beans.put(entry.getKey(), entry.getValue().getInstance(matcher, beans)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    public Set<Class<?>> getControllers() {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }
}
