package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Inject;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
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
    private static final Logger log = LoggerFactory.getLogger( BeanFactoryTest.class );

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("nextstep.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
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

    @Test
    public void di() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
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
