package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanRecipe> beanRecipes;

    private CircularReferenceDetector circularReferenceDetector;

    public BeanFactory() {
        this.beanRecipes = Maps.newHashMap();
        this.circularReferenceDetector = new CircularReferenceDetector();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> type : beanRecipes.keySet()) {
            beans.put(type, getOrBakeBean(type));
        }
    }

    private Object getOrBakeBean(Class<?> type) {
        if (beans.containsKey(type)) {
            return beans.get(type);
        }

        beans.put(type, bakeBean(type));
        return beans.get(type);
    }

    private Object bakeBean(Class<?> type) {
        BeanRecipe beanRecipe = beanRecipes.get(type);
        Object[] params = resolveParams(beanRecipe);
        return beanRecipe.bakeBean(params);
    }


    private Object[] resolveParams(BeanRecipe recipe) {
        return Arrays.stream(recipe.getBeanParamTypes())
                .peek(addCircularReferenceDetector(circularReferenceDetector))
                .map(param -> BeanFactoryUtils.findConcreteClass2(param, beanRecipes.keySet()).orElse(param))
                .map(this::getOrBakeBean)
                .peek(removeDetector(circularReferenceDetector))
                .toArray();

    }

    private Consumer<? super Object> removeDetector(CircularReferenceDetector circularReferenceDetector) {
        return clazz -> circularReferenceDetector.remove();
    }

    private Consumer<? super Class<?>> addCircularReferenceDetector(CircularReferenceDetector circularReferenceDetector) {
        return clazz -> circularReferenceDetector.add(clazz);

    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(annotation))
                .collect(Collectors.toMap(key -> key, key -> beans.get(key)));
    }

    public void addScanner(BeanScanner beanScanner) {
        beanRecipes.putAll(beanScanner.scan().stream()
                .collect(Collectors.toMap(recipe -> recipe.getBeanType(), recipe -> recipe)));
    }

    public void addBeanType(BeanRecipe beanRecipe) {
        beanRecipes.put(beanRecipe.getBeanType(), beanRecipe);

    }

    public void addAllBeanType(Set<BeanRecipe> beanRecipes) {
        beanRecipes.forEach(this::addBeanType);
    }

    public void addSingleton(Class<?> beanType, Object bean) {
        beans.put(beanType, bean);
    }
}
