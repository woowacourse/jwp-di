package nextstep.di.validation;

import com.google.common.collect.Maps;
import nextstep.di.factory.BeanFactoryUtils;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class BeanValidationExecutor {
    private Map<Class<?>, BeanValidator> beanValidators = Maps.newHashMap();

    public BeanValidationExecutor() {
        Package basePackage = this.getClass().getPackage();
        Reflections reflections = new Reflections(basePackage.getName());

        registerBeanValidators(reflections);
    }

    private void registerBeanValidators(final Reflections reflections) {
        Set<Class<? extends BeanValidator>> validators = reflections.getSubTypesOf(BeanValidator.class);

        for (Class<? extends BeanValidator> validator : validators) {
            BeanValidator instance = (BeanValidator) BeanFactoryUtils.instantiate(BeanFactoryUtils.getDefaultConstructor(validator));
            beanValidators.put(validator, instance);
        }
    }

    public void execute(final Class<?> preInstantiateBean, Object... validationHints) {
        beanValidators.values()
                .forEach(beanValidator -> beanValidator.validate(preInstantiateBean, validationHints));
    }
}
