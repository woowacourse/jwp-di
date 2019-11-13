package nextstep.di.factory;

import java.util.*;
import java.util.stream.Collectors;

public class BeanInitializer {
    private Stack<BeanCreationResource> circularReferenceDetector;
    private List<BeanCreationResource> beanInitializationsQueue;
    private Map<Class<?>, BeanCreationResource> preInitializedResources;

    BeanInitializer() {
        circularReferenceDetector = new Stack<>();
        beanInitializationsQueue = new ArrayList<>();
        preInitializedResources = new HashMap<>();
    }

    void addBeanCreationResources(Set<BeanCreationResource> resources) {
        preInitializedResources.putAll(resources.stream()
                .collect(Collectors.toMap(BeanCreationResource::getType, resource -> resource)));
        for (BeanCreationResource resource : resources) {
            checkResourceExist(resource);
        }
    }

    public Map<Class<?>, Object> initialize(Map<Class<?>, Object> beans) {
        for (BeanCreationResource resource : beanInitializationsQueue) {
            beans.put(resource.getType(), invokeBean(beans, resource));
        }
        return beans;
    }

    private Object invokeBean(Map<Class<?>, Object> beans, BeanCreationResource resource) {
        return resource.initialize(resource.getParameterTypes().stream()
                .map(type -> {
                    if (beans.containsKey(type)) {
                        return beans.get(type);
                    }
                    return beans.get(BeanFactoryUtils.findConcreteClass(type, beans.keySet()));
                })
                .toArray());
    }

    private void checkResourceExist(BeanCreationResource resource) {
        if (beanInitializationsQueue.contains(resource)) {
            return;
        }
        createResourcesOfParameters(resource);
    }

    private void createResourcesOfParameters(BeanCreationResource resource) {
        if (circularReferenceDetector.search(resource) >= 0) {
            throw new BeanCreateException("순환 참조되는 클래스가 존재합니다 : " + resource.toString());
        }
        circularReferenceDetector.push(resource);
        for (Class<?> parameterType : resource.getParameterTypes()) {
            checkResourceExist(getResource(parameterType));
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
        return createResource(concreteClass);
    }

    private BeanCreationResource createResource(Class<?> concreteClass) {
        BeanCreationResource resource = new ClasspathBeanCreationResource(
                BeanFactoryUtils.getInjectedConstructor(concreteClass).orElseThrow(BeanCreateException::new));
        preInitializedResources.put(concreteClass, resource);
        return resource;
    }
}
