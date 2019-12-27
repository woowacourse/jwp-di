package nextstep.di.beans.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Inject;
import nextstep.di.beans.factory.exception.InstantiationFailedException;
import nextstep.di.beans.factory.exception.NoSuchDefaultConstructorException;
import nextstep.di.beans.specification.BeanSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

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
     * @param preInstantiateBeans
     * @return
     */
    public static BeanSpecification findConcreteClass(Class<?> injectedClazz, Set<BeanSpecification> preInstantiateBeans) {
        if (!injectedClazz.isInterface()) {
            return preInstantiateBeans.stream()
                    .filter(bean -> bean.getType().equals(injectedClazz))
                    .findAny()
                    .orElseThrow();
        }

        for (BeanSpecification clazz : preInstantiateBeans) {
            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getType().getInterfaces());
            if (interfaces.contains(injectedClazz) | clazz.getType().equals(injectedClazz)) {
                return clazz;
            }
        }

        throw new IllegalStateException(injectedClazz + "인터페이스를 구현하는 Bean이 존재하지 않는다.");
    }

    public static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error("NoSuchDefaultConstructorException : ", e);
            throw new NoSuchDefaultConstructorException("기본 생성자가 없습니다.", e);
        }
    }

    public static Object instantiate(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Instantiation Failed : {}", constructor, e);
            throw new InstantiationFailedException("Instantiation Failed", e);
        }
    }
}
