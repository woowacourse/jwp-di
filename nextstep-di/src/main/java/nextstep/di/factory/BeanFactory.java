package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(BeanCreateMatcher beanCreateMatcher) {
        beanCreateMatcher.forEach((key, value) -> beans.putIfAbsent(key, value.getInstance(beanCreateMatcher, beans)));
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
