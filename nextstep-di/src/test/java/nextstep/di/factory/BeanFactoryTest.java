package nextstep.di.factory;

import nextstep.di.factory.example.*;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    //    private Reflections reflections;
    private BeanScanner beanScanner;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanScanner = new BeanScanner("nextstep.di.factory.example");
        Set<Class<?>> preInstantiatedClazz = beanScanner.getTypesAnnotatedWith(Repository.class, Service.class, Controller.class );

        beanFactory = new BeanFactory(preInstantiatedClazz);
        beanFactory.initialize();
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

    @Test
    void single_instance_test() {
        final MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);

        final QuestionRepository actual = myQnaService.getQuestionRepository();
        final QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("싱글인스턴 확인 테스트")
    public void single_instance_test2() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);
        MyQnaService qnaService = beanFactory.getBean(MyQnaService.class);
        TestController testController = beanFactory.getBean(TestController.class);
        TestService testService = beanFactory.getBean(TestService.class);

        assertNotNull(testController);
        assertNotNull(testService);
        assertNotNull(qnaController.getQnaService());
        assertNotNull(qnaService);

        assertThat(qnaService == qnaController.getQnaService()).isTrue();
        assertThat(testService == testController.getTestService()).isTrue();
    }

    @Test
    public void di_without_inject_annotation() {
        TestController testController = beanFactory.getBean(TestController.class);

        assertNotNull(testController);
        assertNotNull(testController.getTestService());

        TestService testService = testController.getTestService();
        assertNotNull(testService.getUserRepository());
        assertNotNull(testService.getQuestionRepository());
    }
}
