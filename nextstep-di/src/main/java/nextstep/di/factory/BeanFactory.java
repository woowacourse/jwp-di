package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Inject;
import nextstep.exception.NotFoundConstructorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        logger.debug("Initialize BeanFactory!");
        preInstanticateBeans.forEach(bean -> {
            logger.debug(bean.getName());
            logger.debug(findConstructor(bean).getName());
        });
    }

    private Constructor findConstructor(Class<?> bean) {
        Constructor<?>[] constructors = bean.getConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }

        List<Constructor> list = Arrays.stream(bean.getConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        if (list.size() == 1) {
            return list.get(0);
        }

        throw new NotFoundConstructorException("올바른 생성자를 찾을 수 없습니다.");
    }
}
