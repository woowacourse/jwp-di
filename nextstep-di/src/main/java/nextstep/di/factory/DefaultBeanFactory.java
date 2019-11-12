package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.DefaultBeanDefinition;
import nextstep.di.bean.MethodBeanDefinition;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();

    public DefaultBeanFactory(Set<Class<?>> preInstantiatedBeans) {
        initBeanDefinition(preInstantiatedBeans);
        initBeans();
    }

    private void initBeanDefinition(final Set<Class<?>> preInstantiatedBeans) {
        preInstantiatedBeans.forEach(clazz -> {
            DefaultBeanDefinition dbd = initDefaultBeanDefinition(preInstantiatedBeans, clazz);
            initMethodBeanDefinition(clazz, dbd);
        });
    }

    private DefaultBeanDefinition initDefaultBeanDefinition(final Set<Class<?>> preInstantiatedBeans, final Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiatedBeans);
        DefaultBeanDefinition dbd = new DefaultBeanDefinition(concreteClass);
        beanDefinitions.put(dbd.getBeanClass(), dbd);
        return dbd;
    }

    private void initMethodBeanDefinition(final Class<?> clazz, final DefaultBeanDefinition dbd) {
        Stream.of(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> {
                    MethodBeanDefinition mbd = new MethodBeanDefinition(getOrCreateBean(dbd), method.getReturnType(), method);
                    beanDefinitions.put(mbd.getBeanClass(), mbd);
                });
    }

    private void initBeans() {
        beanDefinitions.values().forEach(this::createBean);

    }

    private Object createBean(BeanDefinition beanDefinition) {
        if (beans.containsKey(beanDefinition.getBeanClass())) {
            return beans.get(beanDefinition.getBeanClass());
        }
        logger.debug("create Class = {}", beanDefinition.getBeanClass());

        Object[] parameters = createParameters(beanDefinition);
        Object bean = beanDefinition.createBean(parameters);
        beans.put(beanDefinition.getBeanClass(), bean);
        return bean;
    }

    private Object[] createParameters(final BeanDefinition beanDefinition) {
        return Stream.of(beanDefinition.getParameterTypes())
                .map(parameterType -> {
                    Class<?> concreteClass = BeanFactoryUtils.findConcreteClassByBeanDefinition(parameterType, beanDefinitions.values());
                    return getOrCreateBean(beanDefinitions.get(concreteClass));
                })
                .toArray();
    }

    private Object getOrCreateBean(BeanDefinition beanDefinition) {
        return beans.getOrDefault(beanDefinition.getBeanClass(), createBean(beanDefinition));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClassByBeanDefinition(requiredType, beanDefinitions.values());
        return (T) beans.get(concreteClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Method> findMethodsByAnnotation(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(classAnnotation))
                .map(clazz -> ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(methodAnnotation)))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
