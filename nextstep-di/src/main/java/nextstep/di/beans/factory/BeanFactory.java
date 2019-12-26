package nextstep.di.beans.factory;

import com.google.common.collect.Maps;
import nextstep.di.beans.specification.BeanSpecification;
import nextstep.di.validation.BeanValidationExecutor;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<BeanSpecification> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Deque<BeanSpecification> beanRegisterHistory = new ArrayDeque<>();
    private BeanValidationExecutor beanValidationExecutor = new BeanValidationExecutor();

    public BeanFactory(Set<BeanSpecification> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    public void initialize() {
        for (BeanSpecification preInstantiateBean : preInstantiateBeans) {
            registerBean(preInstantiateBean);
        }
    }

    private void registerBean(BeanSpecification preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean.getType())) {
            return;
        }

        beanValidationExecutor.execute(preInstantiateBean, beanRegisterHistory);

        beanRegisterHistory.push(preInstantiateBean);

        Object bean = instantiateBeanOf(preInstantiateBean);
        log.info("created bean: {}", bean);

        beans.put(preInstantiateBean.getType(), bean);
        beanRegisterHistory.pop();
    }

    private Object instantiateBeanOf(final BeanSpecification beanSpecification) {
        List<Object> arguments = extractArgumentsOf(beanSpecification);
        return beanSpecification.instantiate(arguments.toArray());
    }

    private List<Object> extractArgumentsOf(final BeanSpecification beanSpecification) {
        List<Object> arguments = new ArrayList<>();
        for (Class<?> parameter : beanSpecification.getParameterTypes()) {
            BeanSpecification concreteParameter = BeanFactoryUtils.findConcreteClass(parameter, this.preInstantiateBeans);

            if (isUnregisteredBean(concreteParameter.getType())) {
                registerBean(concreteParameter);
            }

            arguments.add(beans.get(concreteParameter.getType()));
        }
        return arguments;
    }

    private Set<Class<?>> getTypes(Set<BeanSpecification> beanSpecifications) {
        return beanSpecifications.stream()
                .map(BeanSpecification::getType)
                .collect(Collectors.toSet());
    }

    private boolean isUnregisteredBean(final Class<?> concreteParameter) {
        return !beans.containsKey(concreteParameter);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getControllers() {
        return getBeansAnnotatedWith(Controller.class);
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        return beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
