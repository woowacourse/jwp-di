package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private List<Class<? extends Annotation>> types = Arrays.asList(Controller.class, Service.class, Repository.class);

    private Map<Class<?>, Object> beans;

    private BeanInitializeOrderDeterminer determiner = new BeanInitializeOrderDeterminer();

    public void register(Class<?> configuration) {
        if (!configuration.isAnnotationPresent(Configuration.class)) {
            throw new BeanCreateException("Configuration 킅래스에 어노테이션이 존재하지 않습니다.");
        }
        try {
            Object configurationObject = configuration.getDeclaredConstructor().newInstance();
            Set<BeanCreationResource> resources = Arrays.stream(configuration.getDeclaredMethods())
                    .map(method -> new ConfigurationBeanCreationResource(method, configurationObject))
                    .collect(Collectors.toSet());
            determiner.determine(resources);
        } catch (Exception e) {
            throw new BeanCreateException(e);
        }
        if (configuration.isAnnotationPresent(ComponentScan.class)) {
            scanBasePackages(configuration);
        }
    }

    private void scanBasePackages(Class<?> configuration) {
        ComponentScan componentScan = configuration.getAnnotation(ComponentScan.class);
        if (componentScan.basePackages().length == 0) {
            scan(configuration.getPackageName());
            return;
        }
        Set<String> basePackages = Sets.newHashSet(componentScan.basePackages());
        for (String basePackage : basePackages) {
            scan(basePackage);
        }
    }

    public void scan(String targetPackage) {
        Reflections reflections = new Reflections(targetPackage);

        Set<Class<?>> classWithTypesAnnotation = getTypesWithAnnotation(reflections);

        Set<BeanCreationResource> resources = createClasspathBeanCreationResources(classWithTypesAnnotation);

        determiner.determine(resources);
    }

    private Set<Class<?>> getTypesWithAnnotation(Reflections reflections) {
        return types.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<BeanCreationResource> createClasspathBeanCreationResources(Set<Class<?>> classWithTypesAnnotation) {
        return classWithTypesAnnotation.stream()
                .map(clazz -> BeanFactoryUtils.findConcreteClass(clazz, classWithTypesAnnotation))
                .map(concrete -> BeanFactoryUtils.getInjectedConstructor(concrete).orElseThrow(BeanCreateException::new))
                .map(ClasspathBeanCreationResource::new)
                .collect(Collectors.toSet());
    }

    public void initialize() {
        beans = determiner.initialize();
        for (Map.Entry<Class<?>, Object> classObjectEntry : beans.entrySet()) {
            logger.info("{} bean made : {}", classObjectEntry.getKey(), classObjectEntry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(annotation))
                .collect(Collectors.toMap(key -> key, key -> beans.get(key)));
    }
}
