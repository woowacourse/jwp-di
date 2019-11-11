package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Inject;
import nextstep.di.factory.example.*;
import nextstep.exception.BeanInstantiationException;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("nextstep.di.factory.example");
    }

    @DisplayName("Bean 초기화 확인")
    @Test
    void initialize() {
        Set<Class<?>> preInstantiateBeans = new HashSet<>(Arrays.asList(TestClass.class));
        BeanFactory beanFactory = new BeanFactory(preInstantiateBeans);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(TestClass.class)).isInstanceOf(TestClass.class);
    }

    @DisplayName("Bean 초기화 실패 확인")
    @Test
    void failInitialize() {
        Set<Class<?>> preInstantiateBeans = new HashSet<>(Arrays.asList(TestClass.class, TestClassWithoutInject.class));
        BeanFactory beanFactory = new BeanFactory(preInstantiateBeans);

        assertThatExceptionOfType(BeanInstantiationException.class)
                .isThrownBy(beanFactory::initialize);
    }

    @DisplayName("Controller 클래스 반환 확인")
    @Test
    void getController() {
        Set<Class<?>> preInstantiateBeans = new HashSet<>(Arrays.asList(TestClass.class, TestClassWithoutController.class));
        BeanFactory beanFactory = new BeanFactory(preInstantiateBeans);
        beanFactory.initialize();

        assertThat(beanFactory.getController()).contains(TestClass.class);
        assertThat(beanFactory.getController()).doesNotContain(TestClassWithoutController.class);
    }

    @DisplayName("Bean 생성과정에서 주입된 객체가 Bean으로 등록된 객체인지 확인")
    @Test
    public void di() {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        BeanFactory beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        assertNotNull(qnaController);
        assertThat(beanFactory.getBean(QnaController.class)).isInstanceOf(QnaController.class);

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertThat(qnaService).isEqualTo(beanFactory.getBean(MyQnaService.class));

        UserRepository userRepository = qnaService.getUserRepository();
        QuestionRepository questionRepository = qnaService.getQuestionRepository();

        assertNotNull(userRepository);
        assertNotNull(questionRepository);

        assertThat(userRepository).isEqualTo(beanFactory.getBean(JdbcUserRepository.class));
        assertThat(questionRepository).isEqualTo(beanFactory.getBean(JdbcQuestionRepository.class));
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }

    @Controller
    private class TestClass {
        @Inject
        public TestClass() {
        }
    }

    @Controller
    private class TestClassWithoutInject {
    }

    private class TestClassWithoutController {
        @Inject
        public TestClassWithoutController() {
        }
    }
}
