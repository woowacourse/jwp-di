package nextstep.di.factory;

import nextstep.di.factory.beans.integration.IntegrationExampleBean2;
import nextstep.di.factory.beans.integration.JdbcTestRepository;
import nextstep.di.factory.beans.noerror.OneConstructorBean;
import nextstep.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private ApplicationContext applicationContext;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        applicationContext = new ApplicationContext();
        applicationContext.scan("nextstep.di.factory.example");
        applicationContext.initialize();
    }

    @Test
    public void di() throws Exception {
        QnaController qnaController = applicationContext.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }


    @Test
    void UserRepository가_싱글_인스턴스가_맞는지_테스트() {
        final UserRepository beanFactoryUserRepository = applicationContext.getBean(JdbcUserRepository.class);

        final MyQnaService qnaService = applicationContext.getBean(MyQnaService.class);
        final UserRepository qnaServiceUserRepository = qnaService.getUserRepository();

        final NewQnaService newQnaService = applicationContext.getBean(NewQnaService.class);
        final UserRepository newQnaServiceUserRepository = newQnaService.getUserRepository();

        assertThat(beanFactoryUserRepository).isEqualTo(qnaServiceUserRepository);
        assertThat(qnaServiceUserRepository).isEqualTo(newQnaServiceUserRepository);
    }

    @Test
    public void register_simple() {
        applicationContext = new ApplicationContext();
        applicationContext.register(ExampleConfig.class);
        applicationContext.initialize();

        assertNotNull(applicationContext.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        applicationContext = new ApplicationContext();
        applicationContext.register(IntegrationConfig.class);
        applicationContext.scan("nextstep.di.factory.beans.integration");
        applicationContext.initialize();

        DataSource dataSource = applicationContext.getBean(DataSource.class);
        assertNotNull(dataSource);

        JdbcTestRepository testRepository = applicationContext.getBean(JdbcTestRepository.class);
        assertNotNull(testRepository);
        assertNotNull(testRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());

        assertEquals(jdbcTemplate.getDataSource(), testRepository.getDataSource());
        assertEquals(jdbcTemplate.getDataSource(), dataSource);
    }

    @Test
    void 기본_ComponentScan_어노테이션_동작_확인() {
        applicationContext = new ApplicationContext();
        applicationContext.register(DefaultComponentScanConfig.class);
        applicationContext.initialize();

        NewQnaService newQnaService = applicationContext.getBean(NewQnaService.class);
        assertNotNull(newQnaService);
        assertNotNull(newQnaService.getQuestionRepository());
        assertNotNull(newQnaService.getUserRepository());

        MyJdbcTemplate myJdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(myJdbcTemplate);
        assertNotNull(myJdbcTemplate.getDataSource());
    }

    @Test
    void ComponentScan_어노테이션_basePackage가_하나일_경우_동작_확인() {
        applicationContext = new ApplicationContext();
        applicationContext.register(OnePackageComponentScanConfig.class);
        applicationContext.initialize();

        IntegrationExampleBean2 exampleBean = applicationContext.getBean(IntegrationExampleBean2.class);
        assertNotNull(exampleBean);
        assertNotNull(exampleBean.getRepository());
        assertNotNull(exampleBean.getRepository().getDataSource());

        DataSource dataSource = applicationContext.getBean(DataSource.class);
        assertNotNull(dataSource);

        assertEquals(dataSource, exampleBean.getRepository().getDataSource());
    }

    @Test
    void ComponentScan_어노테이션_basePackage가_두개이상일_경우_동작_확인() {
        applicationContext = new ApplicationContext();
        applicationContext.register(OverTwoComponentScanConfig.class);
        applicationContext.initialize();

        IntegrationExampleBean2 exampleBean2 = applicationContext.getBean(IntegrationExampleBean2.class);
        assertNotNull(exampleBean2);
        assertNotNull(exampleBean2.getRepository());
        assertNotNull(exampleBean2.getRepository().getDataSource());

        MyJdbcTemplate myJdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(myJdbcTemplate);

        OneConstructorBean oneConstructorBean = applicationContext.getBean(OneConstructorBean.class);
        assertNotNull(oneConstructorBean);
    }
}
