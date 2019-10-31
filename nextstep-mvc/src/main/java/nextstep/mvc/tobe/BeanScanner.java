package nextstep.mvc.tobe;

import nextstep.di.factory.BeanFactory;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BeanScanner {
    public static final Class<Controller> CONTROLLER = Controller.class;
    public static final Class<Service> SERVICE = Service.class;
    public static final Class<Repository> REPOSITORY = Repository.class;
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public BeanFactory getBeanFactory() {
        return new BeanFactory(scanBeans());
    }

    private Set<Class<?>> scanBeans() {
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(CONTROLLER);
        typesAnnotatedWith.addAll(reflections.getTypesAnnotatedWith(SERVICE));
        typesAnnotatedWith.addAll(reflections.getTypesAnnotatedWith(REPOSITORY));
        return typesAnnotatedWith;
    }
}
