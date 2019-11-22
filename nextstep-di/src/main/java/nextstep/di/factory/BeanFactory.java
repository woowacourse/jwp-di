package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.scanner.BeanDefinition;
import nextstep.stereotype.Controller;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanFactory {
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void register(Map<Class<?>, BeanDefinition> maps) {
        this.beanDefinitions.putAll(maps);
    }

    public void initialize() {
        for (Class<?> clazz : beanDefinitions.keySet()) {
            beans.put(clazz, instantiateBeans(clazz));
        }
    }

    public Map<Class<?>, Object> getController() {
        return getAnnotatedWith(Controller.class);
    }

    private Object instantiateBeans(Class<?> definitionClazz) {
        if (beans.containsKey(definitionClazz)) {
            return beans.get(definitionClazz);
        }

        Class[] clazz = beanDefinitions.get(definitionClazz).getParameterTypes();
        Object[] params = new Object[clazz.length];

        instantiateParams(clazz, params);

        beans.put(definitionClazz, beanDefinitions.get(definitionClazz).getInstance(params));
        return beans.get(definitionClazz);
    }

    private void instantiateParams(Class[] clazz, Object[] params) {
        for (int i = 0; i < params.length; i++) {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass2(clazz[i], beanDefinitions);
            params[i] = instantiateBeans(concreteClass);
        }
    }

    private Map<Class<?>, Object> getAnnotatedWith(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toMap(clazz -> clazz, beans::get));
    }
}

