package nextstep.di.factory;

import nextstep.di.factory.constructor.AnyService;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryTest {
    private static final String NEXTSTEP_DI_FACTORY_EXAMPLE = "nextstep.di.factory.example";
    private static final String NEXTSTEP_DI_FACTORY_CIRCULAR = "nextstep.di.factory.circular";
    private static final String NEXTSTEP_DI_FACTORY_CONSTRUCTOR_NOTDEFAULT = "nextstep.di.factory.constructor";
    private static final String NEXTSTEP_DI_FACTORY_CONSTRUCTOR_NOTBEAN = "nextstep.di.factory.notbean";

    @Test
    void di() {
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

    @Test
    @DisplayName("빈이 아닌걸 Inject 주입 테스트")
    void injectNotBean() {
        assertThrows(IllegalStateException.class, () -> getBeanFactory(NEXTSTEP_DI_FACTORY_CONSTRUCTOR_NOTBEAN));
    }

    @Test
    @DisplayName("다른 패키지 빈 생성 테스트")
    void getOtherPackageBeans() {
        BeanFactory beanFactory = getBeanFactory(NEXTSTEP_DI_FACTORY_EXAMPLE);
        beanFactory.initialize();
        assertThat(beanFactory.getBean(AnyService.class)).isNull();
    }

    private BeanFactory getBeanFactory(Object... basePackage) {
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory beanFactory = new BeanFactory(beanScanner.getPreInstanticateBeanClasses());
        beanFactory.initialize();

        return beanFactory;
    }
}
