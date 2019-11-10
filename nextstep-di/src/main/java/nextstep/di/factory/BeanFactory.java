package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private CircularReferenceDetector circularReferenceDetector;

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        circularReferenceDetector = new CircularReferenceDetector();
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            getConcreteBean(preInstanticateBean);
        }
    }

    private Object getConcreteBean(Class<?> preInstanticateBean) {
        Class<?> concrete = BeanFactoryUtils.findConcreteClass(preInstanticateBean, preInstanticateBeans);
        if (beans.containsKey(concrete)) {
            return beans.get(concrete);
        }
        return createConcreteBean(concrete);
    }

    private Object createConcreteBean(Class<?> concrete) {
        circularReferenceDetector.add(concrete);
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concrete).orElseThrow(BeanCreateException::new);
        List<Object> params = getParams(constructor);
        try {
            Object result = constructor.newInstance(params.toArray());
            logger.debug("New Bean {}, Class : {}", result, concrete);
            beans.put(result.getClass(), result);
            circularReferenceDetector.remove();
            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new BeanCreateException(e);
        }
    }

    private List<Object> getParams(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(this::getConcreteBean)
                .collect(Collectors.toList());
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(annotation))
                .collect(Collectors.toMap(key -> key, key -> beans.get(key)));
    }
}
