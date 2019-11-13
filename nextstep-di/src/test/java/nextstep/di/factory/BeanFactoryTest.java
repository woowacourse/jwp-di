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

    private ApplicationContext beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new ApplicationContext();
        beanFactory.scan("nextstep.di.factory.example");
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
    void UserRepository가_싱글_인스턴스가_맞는지_테스트() {
        final UserRepository beanFactoryUserRepository = beanFactory.getBean(JdbcUserRepository.class);

        final MyQnaService qnaService = beanFactory.getBean(MyQnaService.class);
        final UserRepository qnaServiceUserRepository = qnaService.getUserRepository();

        final NewQnaService newQnaService = beanFactory.getBean(NewQnaService.class);
        final UserRepository newQnaServiceUserRepository = newQnaService.getUserRepository();

        assertThat(beanFactoryUserRepository).isEqualTo(qnaServiceUserRepository);
        assertThat(qnaServiceUserRepository).isEqualTo(newQnaServiceUserRepository);
    }

    @Test
    public void register_simple() {
        beanFactory = new ApplicationContext();
        beanFactory.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        beanFactory = new ApplicationContext();
        beanFactory.register(IntegrationConfig.class);
        beanFactory.scan("nextstep.di.factory.beans.integration");
        beanFactory.initialize();

        DataSource dataSource = beanFactory.getBean(DataSource.class);
        assertNotNull(dataSource);

        JdbcTestRepository testRepository = beanFactory.getBean(JdbcTestRepository.class);
        assertNotNull(testRepository);
        assertNotNull(testRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());

        assertEquals(jdbcTemplate.getDataSource(), testRepository.getDataSource());
        assertEquals(jdbcTemplate.getDataSource(), dataSource);
    }

    @Test
    void 기본_ComponentScan_어노테이션_동작_확인() {
        beanFactory = new ApplicationContext();
        beanFactory.register(DefaultComponentScanConfig.class);
        beanFactory.initialize();

        NewQnaService newQnaService = beanFactory.getBean(NewQnaService.class);
        assertNotNull(newQnaService);
        assertNotNull(newQnaService.getQuestionRepository());
        assertNotNull(newQnaService.getUserRepository());

        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(myJdbcTemplate);
        assertNotNull(myJdbcTemplate.getDataSource());
    }

    @Test
    void ComponentScan_어노테이션_basePackage가_하나일_경우_동작_확인() {
        beanFactory = new ApplicationContext();
        beanFactory.register(OnePackageComponentScanConfig.class);
        beanFactory.initialize();

        IntegrationExampleBean2 exampleBean = beanFactory.getBean(IntegrationExampleBean2.class);
        assertNotNull(exampleBean);
        assertNotNull(exampleBean.getRepository());
        assertNotNull(exampleBean.getRepository().getDataSource());

        DataSource dataSource = beanFactory.getBean(DataSource.class);
        assertNotNull(dataSource);

        assertEquals(dataSource, exampleBean.getRepository().getDataSource());
    }

    @Test
    void ComponentScan_어노테이션_basePackage가_두개이상일_경우_동작_확인() {
        beanFactory = new ApplicationContext();
        beanFactory.register(OverTwoComponentScanConfig.class);
        beanFactory.initialize();

        IntegrationExampleBean2 exampleBean2 = beanFactory.getBean(IntegrationExampleBean2.class);
        assertNotNull(exampleBean2);
        assertNotNull(exampleBean2.getRepository());
        assertNotNull(exampleBean2.getRepository().getDataSource());

        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(myJdbcTemplate);

        OneConstructorBean oneConstructorBean = beanFactory.getBean(OneConstructorBean.class);
        assertNotNull(oneConstructorBean);
    }
}
