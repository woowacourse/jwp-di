package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.ComponentScan;
import nextstep.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements MvcApplicationContext {
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private List<BeanScanner> beanScanners = Arrays.asList(new ClassBeanScanner(), new MethodBeanScanner());

    public AnnotationConfigApplicationContext(Object... basePackages) {
        Map<Class<?>, BeanCreator> creators = Maps.newHashMap();
        for (BeanScanner beanScanner : beanScanners) {
            creators.putAll(beanScanner.scan(basePackages));
        }
        beans = new BeanFactory(creators, beans).initializeBeans();
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
