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
        initialize(beanCreateMatcher);
    }

    private void initialize(BeanCreateMatcher matcher) {
        matcher.entrySet().forEach(entry -> beans.put(entry.getKey(), entry.getValue().getInstance(matcher, beans)));
        logger.debug("Beans: {}", beans);
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
