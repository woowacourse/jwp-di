package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Configuration;
import nextstep.annotation.Inject;
import nextstep.di.factory.exception.InaccessibleConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.getAllConstructors;
import static org.reflections.ReflectionUtils.withAnnotation;

public class BeanFactoryUtils {
    /**
     * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환
     *
     * @param clazz
     * @return
     * @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = getAllConstructors(clazz, withAnnotation(Inject.class));
        if (injectedConstructors.isEmpty()) {
            return null;
        }
        return injectedConstructors.iterator().next();
    }

    /**
     * 인자로 전달되는 클래스의 구현 클래스. 만약 인자로 전달되는 Class가 인터페이스가 아니면 전달되는 인자가 구현 클래스,
     * 인터페이스인 경우 BeanFactory가 관리하는 모든 클래스 중에 인터페이스를 구현하는 클래스를 찾아 반환
     *
     * @param injectedClazz
     * @param preInstanticateBeans
     * @return
     */
    public static Class<?> findConcreteClass(Class<?> injectedClazz, Set<Class<?>> preInstanticateBeans) {
        if (!injectedClazz.isInterface()) {
            return injectedClazz;
        }

        for (Class<?> clazz : preInstanticateBeans) {
            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
            if (interfaces.contains(injectedClazz)) {
                return clazz;
            }
        }

        throw new IllegalStateException(injectedClazz + "인터페이스를 구현하는 Bean이 존재하지 않는다.");
    }

    public static Optional<Constructor<?>> findBeansConstructor(Constructor<?>[] constructors, Set<Class<?>> preInstantiateBeans) {
        return Arrays.stream(constructors)
                .filter(constructor -> isEligibleBeansConstructor(constructor, preInstantiateBeans))
                .findAny();

    }

    private static boolean isEligibleBeansConstructor(Constructor<?> constructor, Set<Class<?>> preInstantiateBeans) {
        return Arrays.stream(constructor.getParameterTypes())
                .allMatch(parameter -> preInstantiateBeans.contains(parameter));
    }

    public static Optional<Constructor<?>> findDefaultConstructor(Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findAny();
    }

    public static Constructor<?> getConstructor(Class<?> beanClass, Set<Class<?>> preConstructInstatiateBeans) {
        Set<Class<?>> allPreInstantiateBeans = new HashSet<>(preConstructInstatiateBeans);
        allPreInstantiateBeans.addAll(getMethodBeanClasses(preConstructInstatiateBeans));

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(beanClass);
        if (constructor != null) {
            return constructor;
        }

        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();

        constructor = BeanFactoryUtils
                .findBeansConstructor(constructors, allPreInstantiateBeans)
                .or(() -> BeanFactoryUtils.findDefaultConstructor(constructors))
                .orElseThrow(InaccessibleConstructorException::new);

        return constructor;
    }

    public static Set<Class<?>> getMethodBeanClasses(Set<Class<?>> preConstructorInstantiateBeans) {
        Set<Class<?>> configurationBeans = preConstructorInstantiateBeans.stream()
                .filter(key -> key.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());

        return configurationBeans.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .map(Method::getReturnType)
                .collect(Collectors.toSet());
    }
}
