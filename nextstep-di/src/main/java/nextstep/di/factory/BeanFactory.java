package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.beandefinition.BaseBeanDefinitionRegister;
import nextstep.di.beandefinition.BeanDefinition;
import nextstep.di.beandefinition.BeanDefinitionRegistry;
import nextstep.di.beandefinition.MethodBeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    // BeanFactory 는 하나만 존재해야하는 걸까?
    private Map<BeanDefinition, Object> beans = Maps.newHashMap();
    private BeanDefinitionRegistry registry = BeanDefinitionRegistry.create();

    private BeanFactory() {
    }

    public static BeanFactory initializeWith(Set<Class<?>> scannedTypes) {
        log.debug("scannedTypes: {}", scannedTypes);

        BeanFactory factory = new BeanFactory();
        factory.initialize(scannedTypes);

        return factory;
    }

    private void initialize(Set<Class<?>> scannedTypes) {
        initializeRegistry(scannedTypes);

        List<BeanDefinition> beanInstantiationOrder = calculateBeanInstantiationOrder();

        addBeansWithOrder(beanInstantiationOrder);
    }

    private void initializeRegistry(Set<Class<?>> scannedTypes) {
        registry = BaseBeanDefinitionRegister.from(BeanDefinitionRegistry.create(), this)
                .register(scannedTypes);
    }


    private List<BeanDefinition> calculateBeanInstantiationOrder() {
        return BeanInstantiationOrderDecider.of(registry)
                .decideOrder();
    }

    private void addBeansWithOrder(List<BeanDefinition> beanInstantiationOrder) {
        for (BeanDefinition definition : beanInstantiationOrder) {
            addBean(definition);
        }
    }

    private void addBean(BeanDefinition definition) {
        if (beans.containsKey(definition)) {
            return;
        }
        beans.put(definition, definition.create(this));
    }

    public <T> T getBean(Class<T> requiredType) {
        BeanDefinition definition = registry.findExactBeanDefinition(requiredType);

        return (T) beans.get(definition);
    }

    public Map<Class<?>, Object> getBeansSatisfiedWith(Predicate<Class<?>> predicate) {
        return beans.keySet().stream()
                .map(definition -> definition.getBeanType())
                .filter(predicate)
                .collect(Collectors.toMap(type -> type, this::getBean));
    }

    public Callback[] callbacks = new Callback[]{
            new BeanMethodInterceptor()
    };

    public class BeanMethodInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            log.debug("method: {} begin", method);

            // 만약에 리턴값으로 등록되어 있으면 해당 빈을 리턴하기
            BeanDefinition definition = MethodBeanDefinition.of(method);
            if (beans.containsKey(definition)) {
                return beans.get(definition);
            }

            log.debug("first time");

            Object ret = methodProxy.invokeSuper(obj, args);

            beans.put(definition, ret);

            log.debug("method: {} end", method);
            return ret;
        }
    }
}
