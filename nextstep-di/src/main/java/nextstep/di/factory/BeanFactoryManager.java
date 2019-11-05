package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.util.List;

public class BeanFactoryManager {
    @SuppressWarnings("unchecked")
    public static void addBeans(List<Class<? extends Annotation>> annotations, Object... basePackage) {
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory beanFactory = BeanFactory.getInstance();
        beanFactory.addBeans(beanScanner.getTypesAnnotatedWith(annotations));
    }
}
