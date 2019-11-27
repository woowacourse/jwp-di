package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanBox> preInstanticateClazz = Maps.newHashMap();

    public void registerBean(Map<Class<?>, Constructor> beans) {
        Map<Class<?>, BeanBox> beanBoxes = Maps.newHashMap();
        beans.keySet().forEach(clazz -> beanBoxes.put(clazz, new ConstructorBeanBox(clazz)));
        preInstanticateClazz.putAll(beanBoxes);
    }

    public void registerConfigBean(Map<Class<?>, Method> configs) {
        Map<Class<?>, BeanBox> beanBoxes = Maps.newHashMap();
        configs.keySet().forEach(clazz -> beanBoxes.put(clazz, new MethodBeanBox(configs.get(clazz))));
        preInstanticateClazz.putAll(beanBoxes);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateClazz.keySet()) {
            scanBean(preInstanticateBean);
        }
    }

    public Map<Class<?>, Object> getAnnotatedWith(Class<? extends Annotation> annotation) {
        Map<Class<?>, Object> annotatedClass = Maps.newHashMap();
        for (Class<?> clazz : beans.keySet()) {
            if (clazz.isAnnotationPresent(annotation)) {
                annotatedClass.put(clazz, beans.get(clazz));
            }
        }
        return annotatedClass;
    }

    @SuppressWarnings("unchecked")
    private Object scanBean(Class<?> preInstanticateBean) {
        if (beans.containsKey(preInstanticateBean)) {
            return beans.get(preInstanticateBean);
        }

        BeanBox beanBox = preInstanticateClazz.get(preInstanticateBean);

        if (!beanBox.hasParams()) {
            Object instance = beanBox.instantiate();
            return putBean(preInstanticateBean, instance);
        }

        Object instance = beanBox.putParameterizedObject(preInstanticateBean, getParams(beanBox));
        return putBean(preInstanticateBean, instance);
    }

    private Object putBean(Class<?> preInstanticateBean, Object instance) {
        beans.put(preInstanticateBean, instance);
        log.debug("preInstanticateBean : {}, instance : {}", preInstanticateBean, instance);
        return beans.get(preInstanticateBean);
    }

    private Object[] getParams(BeanBox beanBox) {
        Object[] params = new Object[beanBox.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = getParameterType(beanBox, i);
            log.debug("parameterType : {}", parameterType);
            params[i] = scanBean(parameterType);
        }
        return params;
    }

    private Class<?> getParameterType(BeanBox beanBox, int i) {
        Object invoker = beanBox.getInvoker();
        if (invoker instanceof Constructor) {
            Class<?> parameterType = ((Constructor) invoker).getParameterTypes()[i];
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstanticateClazz);
            return BeanFactoryUtils.findConcreteClass(concreteClass, preInstanticateClazz);
        }
        if (invoker instanceof Method) {
            return ((Method) invoker).getParameterTypes()[i];
        }
        throw new NotSupportedInjectionTypeException(invoker.getClass());
    }
}
