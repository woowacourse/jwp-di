package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.beancreator.BeanCreator;
import nextstep.di.factory.beanscanner.BeanScanner;
import nextstep.di.factory.beanscanner.ClassBeanScanner;
import nextstep.di.factory.beanscanner.MethodBeanScanner;
import nextstep.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements MvcApplicationContext {
    private final Map<Class<?>, Object> beans;
    private List<BeanScanner> beanScanners = Arrays.asList(new ClassBeanScanner(), new MethodBeanScanner());

    public AnnotationConfigApplicationContext(Object... basePackages) {
        Map<Class<?>, BeanCreator> creators = Maps.newHashMap();
        for (BeanScanner beanScanner : beanScanners) {
            creators.putAll(beanScanner.scan(basePackages));
        }
        beans = new BeanFactory(creators).getBeans();
    }

    @Override
    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }

    @Override
    public Map<Class<?>, Object> getControllers() {
        return beans.entrySet()
                .parallelStream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
