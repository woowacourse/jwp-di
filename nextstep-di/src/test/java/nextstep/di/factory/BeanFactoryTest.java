package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @Test
    public void di() throws NoSuchFieldException, IllegalAccessException {
        AnnotationBeanScanner annotationBeanScanner = new AnnotationBeanScanner("nextstep.di.factory.example");
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        annotationBeanScanner.scanBean(beanCreateMatcher, Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(beanCreateMatcher);

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        Field qnaService = qnaController.getClass().getDeclaredField("qnaService");
        qnaService.setAccessible(true);
        assertNotNull(qnaController);
        assertNotNull(qnaService.get(qnaController));

        MyQnaService myQnaService = (MyQnaService) qnaService.get(qnaController);
        Field userRepository = myQnaService.getClass().getDeclaredField("userRepository");
        userRepository.setAccessible(true);
        Field questionRepository = myQnaService.getClass().getDeclaredField("questionRepository");
        questionRepository.setAccessible(true);

        assertNotNull(userRepository.get(myQnaService));
        assertNotNull(questionRepository.get(myQnaService));
    }

    @Test
    @DisplayName("싱글 인스턴스 확인")
    void sameInstance() throws NoSuchFieldException, IllegalAccessException {
        AnnotationBeanScanner annotationBeanScanner = new AnnotationBeanScanner("nextstep.di.factory.example");
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        annotationBeanScanner.scanBean(beanCreateMatcher, Controller.class, Service.class, Repository.class);
        configurationBeanScanner.scanBean(beanCreateMatcher);
        beanFactory = new BeanFactory(beanCreateMatcher);

        DataSource dataSource = beanFactory.getBean(DataSource.class);
        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertThat(dataSource).isEqualTo(beanFactory.getBean(DataSource.class));
        assertThat(myJdbcTemplate).isEqualTo(beanFactory.getBean(MyJdbcTemplate.class));
        Field expectedDataSource = myJdbcTemplate.getClass().getDeclaredField("dataSource");
        expectedDataSource.setAccessible(true);

        assertThat(dataSource).isEqualTo(expectedDataSource.get(myJdbcTemplate));
    }

    @Test
    @DisplayName("ComponentScan 에서 basePackage 지정한 경우 해당 경로 하위 스캔 테스트")
    void customPath() throws IllegalAccessException, NoSuchFieldException {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        configurationBeanScanner.scanBean(beanCreateMatcher);
        beanFactory = new BeanFactory(beanCreateMatcher);

        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        Field expectedDataSource = myJdbcTemplate.getClass().getDeclaredField("dataSource");
        expectedDataSource.setAccessible(true);

        assertNotNull(myJdbcTemplate);
        assertNotNull(expectedDataSource.get(myJdbcTemplate));
    }

    @Test
    @DisplayName("ComponentScan 에서 basePackage 지정한 후 싱글인스턴스 확인")
    void customPathSameInstance() throws NoSuchFieldException, IllegalAccessException {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        configurationBeanScanner.scanBean(beanCreateMatcher);
        beanFactory = new BeanFactory(beanCreateMatcher);

        DataSource dataSource = beanFactory.getBean(DataSource.class);
        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        Field expectedDataSource = myJdbcTemplate.getClass().getDeclaredField("dataSource");
        expectedDataSource.setAccessible(true);

        assertThat(dataSource).isEqualTo(beanFactory.getBean(DataSource.class));
        assertThat(myJdbcTemplate).isEqualTo(beanFactory.getBean(MyJdbcTemplate.class));
        assertThat(dataSource).isEqualTo(expectedDataSource.get(myJdbcTemplate));
    }
}
