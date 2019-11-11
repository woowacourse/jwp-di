package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.circular.CircularDependenceA;
import nextstep.di.factory.circular.CircularDependenceB;
import nextstep.di.factory.constructor.notbean.AnyClass;
import nextstep.di.factory.constructor.notdefault.AnyService;
import nextstep.di.factory.constructor.notdefault.NoDefaultConstructor;
import nextstep.di.factory.example.*;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final List<Class<?>> NEXTSTEP_DI_FACTORY_EXAMPLE = Arrays.asList(JdbcQuestionRepository.class, JdbcUserRepository.class, MyQnaService.class, QnaController.class, QuestionRepository.class, UserRepository.class);
    private static final List<Class<?>> NEXTSTEP_DI_FACTORY_CIRCULAR = Arrays.asList(CircularDependenceA.class, CircularDependenceB.class);
    private static final List<Class<?>> NEXTSTEP_DI_FACTORY_CONSTRUCTOR_NOTDEFAULT = Arrays.asList(AnyService.class, NoDefaultConstructor.class);
    private static final List<Class<?>> NEXTSTEP_DI_FACTORY_CONSTRUCTOR_NOTBEAN = Arrays.asList(AnyClass.class, NoDefaultConstructor.class);

    @Test
    public void di() {
        QnaController qnaController = getBeanFactory(NEXTSTEP_DI_FACTORY_EXAMPLE).getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("Controller 가져오기 테스트")
    void getBeansAnnotatedWith1() {
        Map<Class<?>, Object> controllerBeans = getBeanFactory(NEXTSTEP_DI_FACTORY_EXAMPLE).getBeansAnnotatedWith(Controller.class);

        assertThat(controllerBeans.size()).isEqualTo(1);
        assertThat(controllerBeans.get(QnaController.class)).isNotNull();
    }

    @Test
    @DisplayName("Service 가져오기 테스트")
    void getBeansAnnotatedWith2() {
        Map<Class<?>, Object> serviceBeans = getBeanFactory(NEXTSTEP_DI_FACTORY_EXAMPLE).getBeansAnnotatedWith(Service.class);

        assertThat(serviceBeans.size()).isEqualTo(1);
        assertThat(serviceBeans.get(MyQnaService.class)).isNotNull();
    }

    @Test
    @DisplayName("Repository 가져오기 테스트")
    void getBeansAnnotatedWith3() {
        Map<Class<?>, Object> repositoryBeans = getBeanFactory(NEXTSTEP_DI_FACTORY_EXAMPLE).getBeansAnnotatedWith(Repository.class);

        assertThat(repositoryBeans.size()).isEqualTo(2);
        assertThat(repositoryBeans.get(JdbcQuestionRepository.class)).isNotNull();
        assertThat(repositoryBeans.get(JdbcUserRepository.class)).isNotNull();
    }

    @Test
    @DisplayName("생성자 파라미터에 Bean이 아닌 클래스형이 포함됐을 때 테스트")
    void constructorNotBean() {
        assertThrows(IllegalStateException.class, () -> getBeanFactory(NEXTSTEP_DI_FACTORY_CONSTRUCTOR_NOTBEAN));
    }

    @Test
    @DisplayName("순환참조 테스트")
    void circularReference() {
        assertThrows(IllegalStateException.class, () -> getBeanFactory(NEXTSTEP_DI_FACTORY_CIRCULAR));

    }

    @Test
    @DisplayName("빈 싱글톤 테스트")
    void singletonBean() {
        BeanFactory beanFactory = getBeanFactory(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Object bean1 = beanFactory.getBean(JdbcUserRepository.class);
        Object bean2 = beanFactory.getBean(JdbcQuestionRepository.class);
        Object bean3 = beanFactory.getBean(MyQnaService.class).getUserRepository();
        Object bean4 = beanFactory.getBean(MyQnaService.class).getQuestionRepository();

        assertThat(bean1 == bean3).isTrue();
        assertThat(bean2 == bean4).isTrue();
    }

    @Test
    @DisplayName("기본 생성자가 존재하지 않고 Inject 어노테이션도 없을 경우 예외 테스트")
    void noDefaultConstructor() {
        assertThrows(IllegalStateException.class, () -> getBeanFactory(NEXTSTEP_DI_FACTORY_CONSTRUCTOR_NOTDEFAULT));
    }

    private BeanFactory getBeanFactory(List<Class<?>> prefix) {
        Set<Class<?>> preInstanticateClazz = Sets.newHashSet(prefix);
        BeanFactory beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();

        return beanFactory;
    }
}
