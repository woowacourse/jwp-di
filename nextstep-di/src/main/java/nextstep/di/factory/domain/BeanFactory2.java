package nextstep.di.factory.domain;

import nextstep.di.factory.support.Beans;

import java.lang.annotation.Annotation;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class BeanFactory2 implements BeanFactory {
    private Beans beans;

    public BeanFactory2() {
        this.beans = new Beans();
    }

    public void addBean(Class<?> clazz, Object instance) {
        beans.put(clazz, () -> instance);
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    @Override
    public Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toSet());
    }
}
