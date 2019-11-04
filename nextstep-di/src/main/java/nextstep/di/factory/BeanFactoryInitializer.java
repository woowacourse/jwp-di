package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.util.List;

public class BeanFactoryInitializer {
    @SuppressWarnings("unchecked")
    public static void init(List<Class<? extends Annotation>> annotations, Object... basePackage) {
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory beanFactory = BeanFactory.getInstance();
        beanFactory.initialize(beanScanner.getTypesAnnotatedWith(annotations));
    }
}
