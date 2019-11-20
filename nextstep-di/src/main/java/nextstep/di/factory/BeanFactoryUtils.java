package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Inject;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.exception.NotFoundConstructorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactoryUtils {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryUtils.class);

    private static final int ONE_CONSTRUCTOR = 1;
    private static final int FIRST_CONSTRUCTOR = 0;

    /**
     * 생성자가 하나이면 그 생성자 반환, 여러개이면 그 중 @Inject가 붙은 생성자가 하나이면 그 생성자 반환
     * 이외의 경우에는 예외 발생
     *
     * @param clazz
     * @return
     */
    public static Constructor findConstructor(Class<?> clazz) {
        log.debug("Find Constructor : {}", clazz.getName());

        checkInterface(clazz);

        List<Constructor> constructors = Arrays.asList(clazz.getConstructors());

        if (hasOneConstructor(constructors)) {
            return constructors.get(FIRST_CONSTRUCTOR);
        }
        return findInjectedConstructor(constructors);
    }

    private static void checkInterface(Class<?> clazz) {
        if (clazz.isInterface()) {
            throw new NotFoundConstructorException("올바른 생성자를 찾을 수 없습니다.");
        }
    }

    private static Constructor findInjectedConstructor(List<Constructor> constructors) {
        List<Constructor> injectedConstructors = constructors.stream()
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        if (hasOneConstructor(injectedConstructors)) {
            return injectedConstructors.get(FIRST_CONSTRUCTOR);
        }
        throw new NotFoundConstructorException("올바른 생성자를 찾을 수 없습니다.");
    }

    private static boolean hasOneConstructor(List<Constructor> constructors) {
        return constructors.size() == ONE_CONSTRUCTOR;
    }

    /**
     * 해당하는 클래스로 Bean이 등록되어 있으면 해당 클래스로 리턴한다. 인터페이스인 경우 인터페이스로 등록되어있으면 그 클래스를 반환하고 아닌 경우
     * 실제 구현되어있는 클래스를 찾아 반환한다. 전부 해당되지 않는 경우 예외를 발생시킨다.
     *
     *
     * @param injectedClazz
     * @param BeanDefinitions
     * @return
     */
    public static Class<?> findCollectClass(Class<?> injectedClazz, Set<BeanDefinition> BeanDefinitions) {
        if (!injectedClazz.isInterface()) {
            return injectedClazz;
        }

        for (BeanDefinition beanDefinition : BeanDefinitions) {
            Class<?> clazz = beanDefinition.getName();

            if (clazz.equals(injectedClazz)) {
                return injectedClazz;
            }

            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
            if (interfaces.contains(injectedClazz)) {
                return clazz;
            }
        }

        throw new IllegalStateException(injectedClazz + " 인터페이스를 구현하는 Bean이 존재하지 않는다.");
    }
}
