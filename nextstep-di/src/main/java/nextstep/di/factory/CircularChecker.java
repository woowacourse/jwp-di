package nextstep.di.factory;

import com.google.common.collect.Lists;
import nextstep.annotation.Inject;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.InjectMethodNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class CircularChecker {
    private static final int NO_PARAMETER = 0;
    private final Set<Class<?>> preInstantiateBeans;

    public CircularChecker(final Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    public void check(final Class<?> clazz) {
        BeanFactoryUtils.getInjectedConstructor(clazz)
            .ifPresent(constructor -> checkComponentCircularReference(constructor, Lists.newArrayList()));
    }

    private void checkComponentCircularReference(final Constructor<?> injectedConstructor, List<Class<?>> ownerClasses) {
        Parameter[] parameters = injectedConstructor.getParameters();

        if(parameters.length == NO_PARAMETER) {
            return;
        }

        if(ownerClasses.isEmpty()) {
            Class<?> ownerClass = injectedConstructor.getDeclaringClass();
            ownerClasses.add(ownerClass);
            checkComponentCircularReference(parameters, ownerClasses);
            return;
        }

        Parameter[] injectedParameters = injectedConstructor.getParameters();
        checkCircularReference(injectedParameters, ownerClasses);

        Class<?> slaveClass = injectedConstructor.getDeclaringClass();
        ownerClasses.add(slaveClass);
        checkComponentCircularReference(injectedParameters, ownerClasses);
    }

    private void checkComponentCircularReference(final Parameter[] parameters, List<Class<?>> ownerClasses) {
        for (Parameter parameter : parameters) {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameter.getType(), preInstantiateBeans);
            Optional<Constructor<?>> maybeInjectedParameterConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
            maybeInjectedParameterConstructor.ifPresent(inject -> checkComponentCircularReference(inject, ownerClasses));
        }
    }

    private void checkCircularReference(final Parameter[] injectedParameters, List<Class<?>> ownerClasses) {
        for (Parameter injectedParameter : injectedParameters) {
            Class<?> type = injectedParameter.getType();
            if(ownerClasses.contains(type)) {
                throw new CircularReferenceException();
            }
        }

//        Arrays.stream(injectedParameters)
//            .map(Parameter::getType)
//            .filter(ownerClasses::contains)
//            .findAny()
//            .orElseThrow(CircularReferenceException::new);
    }
}
