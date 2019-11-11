package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.circular.CircularDependenceA;
import nextstep.di.factory.circular.CircularDependenceB;
import nextstep.di.factory.constructor.AnyService;
import nextstep.di.factory.constructor.NoDefaultConstructor;
import nextstep.di.factory.example.*;
import nextstep.di.factory.notbean.InjectNotBean;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryTest {

    private static final List<Class<?>> NOT_BEAN_PACKAGE_BEANS = Arrays.asList(InjectNotBean.class);
    private static final List<Class<?>> CIRCULARDEPENDENCY_PACKAGE_BEANS = Arrays.asList(CircularDependenceA.class, CircularDependenceB.class);
    private static final List<Class<?>> CONSTRUCTOR_PACKAGE_BEANS = Arrays.asList(AnyService.class, NoDefaultConstructor.class);
    private static final List<Class<?>> EXAMPLE_PACKAGE_BEANS = Arrays.asList(JdbcQuestionRepository.class, JdbcUserRepository.class, MyQnaService.class, QnaController.class, QuestionRepository.class
            , UserRepository.class);

    @Test
    void di() {
        QnaController qnaController = getBeanFactory(EXAMPLE_PACKAGE_BEANS).getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("Controller 가져오기 테스트")
    void getBeansAnnotatedWith1() {
        Map<Class<?>, Object> controllerBeans = getBeanFactory(EXAMPLE_PACKAGE_BEANS).getBeansAnnotatedWith(Controller.class);

        assertThat(controllerBeans.size()).isEqualTo(1);
        assertThat(controllerBeans.get(QnaController.class)).isNotNull();
    }

    @Test
    @DisplayName("Service 가져오기 테스트")
    void getBeansAnnotatedWith2() {
        Map<Class<?>, Object> serviceBeans = getBeanFactory(EXAMPLE_PACKAGE_BEANS).getBeansAnnotatedWith(Service.class);

        assertThat(serviceBeans.size()).isEqualTo(1);
        assertThat(serviceBeans.get(MyQnaService.class)).isNotNull();
    }

    @Test
    @DisplayName("Repository 가져오기 테스트")
    void getBeansAnnotatedWith3() {
        Map<Class<?>, Object> repositoryBeans = getBeanFactory(EXAMPLE_PACKAGE_BEANS).getBeansAnnotatedWith(Repository.class);

        assertThat(repositoryBeans.size()).isEqualTo(2);
        assertThat(repositoryBeans.get(JdbcQuestionRepository.class)).isNotNull();
        assertThat(repositoryBeans.get(JdbcUserRepository.class)).isNotNull();
    }

    @Test
    @DisplayName("순환참조 테스트")
    void circularReference() {
        assertThrows(IllegalStateException.class, () -> getBeanFactory(CIRCULARDEPENDENCY_PACKAGE_BEANS));
    }

    @Test
    @DisplayName("빈 싱글톤 테스트")
    void singletonBean() {
        BeanFactory beanFactory = getBeanFactory(EXAMPLE_PACKAGE_BEANS);
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
        assertThrows(IllegalStateException.class, () -> getBeanFactory(CONSTRUCTOR_PACKAGE_BEANS));
    }

    @Test
    @DisplayName("빈이 아닌걸 Inject 주입 테스트")
    void injectNotBean() {
        assertThrows(IllegalStateException.class, () -> getBeanFactory(NOT_BEAN_PACKAGE_BEANS));
    }

    @Test
    @DisplayName("다른 패키지 빈 생성 테스트")
    void getOtherPackageBeans() {
        BeanFactory beanFactory = getBeanFactory(EXAMPLE_PACKAGE_BEANS);
        beanFactory.initialize();
        assertThat(beanFactory.getBean(AnyService.class)).isNull();
    }

    private BeanFactory getBeanFactory(List<Class<?>> classes) {
        BeanFactory beanFactory = new BeanFactory(Sets.newHashSet(classes));
        beanFactory.initialize();

        return beanFactory;
    }
}
