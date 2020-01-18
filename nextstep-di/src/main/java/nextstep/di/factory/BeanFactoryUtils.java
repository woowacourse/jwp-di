package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Inject;
import nextstep.di.exception.BeanWithoutConstructorException;
import nextstep.di.exception.MultipleBeanImplementationException;
import nextstep.di.exception.NotExistBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.getAllConstructors;
import static org.reflections.ReflectionUtils.withAnnotation;

public class BeanFactoryUtils {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryUtils.class);

    /**
     * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환
     *
     * @param clazz
     * @return
     * @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Constructor<T> getInjectedConstructor(Class<T> clazz) {
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
     * @param preInstantiatedBeans
     * @return
     */
    public static Class<?> findConcreteClass(Class<?> injectedClazz, Set<Class<?>> preInstantiatedBeans) {
        if (!injectedClazz.isInterface()) {
            return injectedClazz;
        }

        List<Class<?>> concreteclasses = preInstantiatedBeans.stream()
                .filter(clazz -> isConcrete(clazz, injectedClazz))
                .collect(Collectors.toList());

        if (concreteclasses.isEmpty()) {
            throw NotExistBeanException.from(injectedClazz);
        }
        if (2 <= concreteclasses.size()) {
            throw MultipleBeanImplementationException.from(injectedClazz, concreteclasses);
        }

        return concreteclasses.get(0);
    }

    private static boolean isConcrete(Class<?> clazz, Class<?> injectedInterface) {
        return Sets.newHashSet(clazz.getInterfaces())
                .contains(injectedInterface);
    }

    public static List<Class<?>> findConcreteClasses(List<Class<?>> classes, Set<Class<?>> preInstantiatedBeans) {
        return classes.stream()
                .map(parameter -> findConcreteClass(parameter, preInstantiatedBeans))
                .collect(Collectors.toList());
    }

    public static <T> Constructor<T> getBeanConstructor(Class<T> type) {
        log.debug("type: {}", type.toString());

        Constructor<T> constructor = getInjectedConstructor(type);

        if (constructor != null) {
            return constructor;
        }

        Constructor<T>[] candidatesConstructor = (Constructor<T>[]) type.getConstructors();
        if (candidatesConstructor.length == 0) {
            throw BeanWithoutConstructorException.from(type);
        }
        return candidatesConstructor[0];
    }
}
