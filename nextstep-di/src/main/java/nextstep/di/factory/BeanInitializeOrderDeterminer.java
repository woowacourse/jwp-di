package nextstep.di.factory;

import java.util.*;
import java.util.stream.Collectors;

public class BeanInitializeOrderDeterminer {
    private Stack<BeanCreationResource> circularReferenceDetector;
    private List<BeanCreationResource> beanInitializationsQueue;
    private Map<Class<?>, BeanCreationResource> preInitializedResources;

    public BeanInitializeOrderDeterminer() {
        circularReferenceDetector = new Stack<>();
        beanInitializationsQueue = new ArrayList<>();
        preInitializedResources = new HashMap<>();
    }

    public void determine(Set<BeanCreationResource> resources) {
        preInitializedResources.putAll(resources.stream()
                .collect(Collectors.toMap(BeanCreationResource::getType, resource -> resource)));
        for (BeanCreationResource resource : resources) {
            check(resource);
        }
    }

    public Map<Class<?>, Object> initialize() {
        Map<Class<?>, Object> beans = new HashMap<>();
        for (BeanCreationResource resource : beanInitializationsQueue) {
            beans.put(resource.getType(), invoke(beans, resource));
        }
        return beans;
    }

    private Object invoke(Map<Class<?>, Object> beans, BeanCreationResource resource) {
        return resource.initialize(resource.getParameterTypes().stream()
                .map(type -> {
                    if (beans.containsKey(type)) {
                        return beans.get(type);
                    }
                    return beans.get(BeanFactoryUtils.findConcreteClass(type, beans.keySet()));
                })
                .toArray());
    }

    private void check(BeanCreationResource resource) {
        if (beanInitializationsQueue.contains(resource)) {
            return;
        }
        create(resource);
    }

    private void create(BeanCreationResource resource) {
        if (circularReferenceDetector.search(resource) >= 0) {
            throw new BeanCreateException("순환 참조되는 클래스가 존재합니다 : " + resource.toString());
        }
        circularReferenceDetector.push(resource);
        for (Class<?> parameterType : resource.getParameterTypes()) {
            check(getResource(parameterType));
        }
        beanInitializationsQueue.add(circularReferenceDetector.pop());
    }

    private BeanCreationResource getResource(Class<?> parameterType) {
        if (preInitializedResources.containsKey(parameterType)) {
            return preInitializedResources.get(parameterType);
        }
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInitializedResources.keySet());
        if (preInitializedResources.containsKey(concreteClass)) {
            return preInitializedResources.get(concreteClass);
        }
        BeanCreationResource resource = new ClasspathBeanCreationResource(
                BeanFactoryUtils.getInjectedConstructor(concreteClass).orElseThrow(BeanCreateException::new));
        preInitializedResources.put(concreteClass, resource);
        return resource;
    }
}
