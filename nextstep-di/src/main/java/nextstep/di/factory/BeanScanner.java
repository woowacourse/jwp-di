package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;
    private List<Class> annotations;

    public BeanScanner(List<Class> annotations, Object... basePackage) {
        this.annotations = annotations;
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> scanBeans() {
        Set<Class<?>> preInitiatedBeans = new HashSet<>();
        for (Class clazz : annotations) {
            preInitiatedBeans.addAll(reflections.getTypesAnnotatedWith(clazz));
        }
        return preInitiatedBeans;
    }

    static Map<Class<?>, Object> instantiate(Set<Class<?>> preInitiatedBeans) {
        Map<Class<?>, Object> beans = Maps.newHashMap();
        try {
            for (Class<?> clazz : preInitiatedBeans) {
                beans.put(clazz, clazz.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }

        return beans;
    }
}
