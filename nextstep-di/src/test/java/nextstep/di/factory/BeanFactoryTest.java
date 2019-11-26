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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @Test
    public void di() {
        AnnotationBeanScanner annotationBeanScanner = new AnnotationBeanScanner("nextstep.di.factory.example");
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        annotationBeanScanner.scanBean(beanCreateMatcher, Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(beanCreateMatcher);

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("싱글 인스턴스 확인")
    void sameInstance() {
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
        assertThat(dataSource).isEqualTo(myJdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("ComponentScan 에서 basePackage 지정한 경우 해당 경로 하위 스캔 테스트")
    void customPath() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        configurationBeanScanner.scanBean(beanCreateMatcher);
        beanFactory = new BeanFactory(beanCreateMatcher);

        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);

        assertNotNull(myJdbcTemplate);
        assertNotNull(myJdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("ComponentScan 에서 basePackage 지정한 후 싱글인스턴스 확인")
    void customPathSameInstance() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        configurationBeanScanner.scanBean(beanCreateMatcher);
        beanFactory = new BeanFactory(beanCreateMatcher);

        DataSource dataSource = beanFactory.getBean(DataSource.class);
        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertThat(dataSource).isEqualTo(beanFactory.getBean(DataSource.class));
        assertThat(myJdbcTemplate).isEqualTo(beanFactory.getBean(MyJdbcTemplate.class));
        assertThat(dataSource).isEqualTo(myJdbcTemplate.getDataSource());
    }
}
