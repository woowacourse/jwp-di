package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Configuration;
import nextstep.exception.BeanNotFoundException;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.DefaultConstructorFindFailException;
import nextstep.exception.ParameterIsNotBeanException;
import nextstep.stereotype.Controller;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanFactory {
    private Set<Class<?>> preInstantiateBeans;
    private ComponentFactory componentFactory;
    private ConfigurationBeanFactory configurationBeanFactory;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        if (beans.containsKey(requiredType)) {
            return (T) beans.get(requiredType);
        }
        throw new BeanNotFoundException();
    }

    public void initialize() {
        //configurationBeanFactory = new ConfigurationBeanFactory(preInstantiateBeans);
        componentFactory = new ComponentFactory(preInstantiateBeans);

        initializeConfigurationBean();
        initializeComponent();

        Map<Class<?>, Object> components = this.componentFactory.getComponents();
        //Map<Class<?>, Object> configurationBeans = this.configurationBeanFactory.getConfigurationBeans();

        // addBean(components, configurationBeans);
    }

    private void initializeComponent() {
        preInstantiateBeans.stream()
            .filter(this::isConfiguration)
            .forEach(this.configurationBeanFactory::initialize);
    }

    private void initializeConfigurationBean() {
        preInstantiateBeans.stream()
            .filter(Predicate.not(this::isConfiguration)) // isComponent
            .forEach(this.componentFactory::instantiateComponent);
    }

    private void addBean(Map<Class<?>, Object> components, Map<Class<?>, Object> configurationBeans) {
        beans.putAll(components);
        beans.putAll(configurationBeans);
    }

    private boolean isConfiguration(Class<?> preInstantiateBean) {
        return preInstantiateBean.isAnnotationPresent(Configuration.class);
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.keySet()
            .stream()
            .filter(clazz -> clazz.isAnnotationPresent(Controller.class))
            .collect(Collectors.toMap(clazz -> clazz, clazz -> beans.get(clazz)));
    }
}
