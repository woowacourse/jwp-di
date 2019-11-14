package nextstep.di.factory.domain;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.util.BeanFactoryUtils;
import nextstep.di.factory.util.ReflectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorBeanTest {

    public static final Set<Class<?>> PRE = new HashSet<>(Arrays.asList(
            JdbcQuestionRepository.class,
            JdbcUserRepository.class
    ));
//
//    @Test
//    public void BeanHasParameters() {
//        Class<?> clazz = MyQnaService.class;
//        ConstructorBean constructorBean = makeConstructorBean(clazz);
//        MyQnaService instance = (MyQnaService) makeInstance(constructorBean);
//        assertThat(instance).isInstanceOf(MyQnaService.class);
//        assertThat(instance.getUserRepository()).isInstanceOf(JdbcUserRepository.class);
//        assertThat(instance.getQuestionRepository()).isInstanceOf(JdbcQuestionRepository.class);
//    }
//
//    private Object makeInstance(ConstructorBean bean) {
//        if (bean.hasParameter()) {
//            Object[] object = bean.getParameters().stream()
//                    .map(beanDefinition -> beanDefinition.makeInstance())
//                    .toArray();
//            return bean.makeInstance(object);
//        }
//        return bean.makeInstance();
//    }
//
//    private ConstructorBean makeConstructorBean(Class<?> clazz) {
//        clazz = BeanFactoryUtils.findConcreteClass(clazz, PRE);
//        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
//        if (constructor == null) {
//            return new ConstructorBean(clazz, ReflectionUtils.getDefaultConstructor(clazz), Lists.emptyList());
//        }
//        Class<?>[] parameterTypes = constructor.getParameterTypes();
//        if (parameterTypes.length == 0) {
//            return new ConstructorBean(clazz, constructor, Lists.emptyList());
//        }
//
//        List<BeanDefinition> parameters = Stream.of(parameterTypes)
//                .map(classType -> makeConstructorBean(classType))
//                .collect(Collectors.toList());
//
//        return new ConstructorBean(clazz, constructor, parameters);
//    }
}