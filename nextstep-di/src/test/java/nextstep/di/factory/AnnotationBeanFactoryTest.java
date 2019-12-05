package nextstep.di.factory;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.scanner.BeanDefinitionScanner;
import nextstep.di.scanner.ClassPathScanner;
import nextstep.di.scanner.ConfigurationBeanDefinitionScanner;
import nextstep.di.scanner.ConstructorBeanDefinitionScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class AnnotationBeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        ClassPathScanner scanner = new ClassPathScanner(IntegrationConfig.class);
        Set<String> samplePackages = scanner.getPackages();

        Set<BeanDefinition> beanDefinitions = new HashSet<>();

        BeanDefinitionScanner configurationScanner = new ConfigurationBeanDefinitionScanner(samplePackages);
        beanDefinitions.addAll(configurationScanner.scan());
        BeanDefinitionScanner constructorScanner = new ConstructorBeanDefinitionScanner(samplePackages);
        beanDefinitions.addAll(constructorScanner.scan());

        this.beanFactory = new AnnotationBeanFactory(beanDefinitions);
        beanFactory.initialize();
    }

    @Test
    void di() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);
        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void checkSingleInstance() {
        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        DataSource datasource = beanFactory.getBean(DataSource.class);
        assertSame(jdbcTemplate.getDataSource(), datasource);
    }
}