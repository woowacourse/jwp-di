package nextstep.di.factory;

import nextstep.annotation.Configuration;
import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.controller.TestController;
import nextstep.di.factory.example.controller.TestController2;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.QuestionRepository;
import nextstep.di.factory.example.repository.TestJdbcTemplate;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example.service.TestService;
import nextstep.di.factory.factory.BasePackageFinder;
import nextstep.di.factory.factory.BeanFactory;
import nextstep.di.factory.factory.BeanScanner;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanScanner beanScanner;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        BasePackageFinder basePackageFinder = new BasePackageFinder("nextstep");
        beanScanner = new BeanScanner(basePackageFinder.findBasePackages());
        Set<Class<?>> preInstantiatedClazz = beanScanner.getTypesAnnotatedWith(Repository.class, Service.class, Controller.class, Configuration.class);

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

    @Test
    public void configuration_injection_test() {
        TestJdbcTemplate testJdbcTemplate = beanFactory.getBean(TestJdbcTemplate.class);
        DataSource testDataSource = beanFactory.getBean(DataSource.class);
        TestService testService = beanFactory.getBean(TestService.class);
        TestController2 testController2 = beanFactory.getBean(TestController2.class);

        assertNotNull(testService);
        assertNotNull(testJdbcTemplate);
        assertNotNull(testJdbcTemplate.getDataSource());
        assertNotNull(testDataSource);
        assertNotNull(testController2);

        assertThat(testJdbcTemplate.getDataSource() == testDataSource).isTrue();
        assertThat(testJdbcTemplate.getTestService() == testService).isTrue();
        assertThat(testController2.getTestJdbcTemplate() == testJdbcTemplate).isTrue();
    }
}
