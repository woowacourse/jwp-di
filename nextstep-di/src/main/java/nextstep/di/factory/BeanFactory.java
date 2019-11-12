package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.instantiation.InstantiationMethod;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private BeanCreateMatcher matcher;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Object... basePackages) {
        BeanScanner beanScanner = new BeanScanner(basePackages);
        matcher = beanScanner.scanBean(Controller.class, Service.class, Repository.class);
    }


    public void initialize() {
        for (Map.Entry<Class<?>, InstantiationMethod> entry : matcher.entrySet()) {
            beans.putIfAbsent(entry.getKey(), entry.getValue().getInstance(matcher, beans));
        }
//        matcher.entrySet().forEach(entry -> beans.putIfAbsent(entry.getKey(), entry.getValue().getInstance(matcher, beans)));
        logger.debug("Bean init : {}", beans);
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
