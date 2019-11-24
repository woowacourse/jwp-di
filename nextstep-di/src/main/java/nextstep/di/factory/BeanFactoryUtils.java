package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Inject;
import nextstep.di.MethodBeanDefinition;
import nextstep.di.exception.NotFoundConstructorException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactoryUtils {
    private static final int ONE = 1;
    private static final int ZERO = 0;

    /**
     * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환
     *
     * @param clazz
     * @return
     * @Inject 애노테이션이 설정되지 않았을 경우, 생성자는 하나로 가정한다.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        List<Constructor<?>> constructors = Arrays.asList(clazz.getConstructors());
        if (constructors.size() == ONE) {
            return constructors.get(ZERO);
        }

        List<Constructor> injectedConstructors = constructors.stream()
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        if (injectedConstructors.size() == ONE) {
            return injectedConstructors.get(ZERO);
        }

        throw new NotFoundConstructorException("올바른 생성자를 찾을 수 없습니다.");
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

    public static Set<MethodBeanDefinition> getMethodBeanDefinitions(Class<?> clazz, Object declaredObject) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new MethodBeanDefinition(declaredObject, method))
                .collect(Collectors.toSet());
    }
}
