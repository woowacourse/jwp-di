package nextstep.di.factory.example;

import nextstep.di.BeanScanner;
import nextstep.di.ConfigurationScanner;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.config.MyJdbcTemplate;
import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.controller.TestQnaController;
import nextstep.di.factory.example.service.MyQnaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        Set<Class<?>> preInstantiateClazz = beanScanner.scan();

        beanFactory = new BeanFactory();
        beanFactory.appendPreInstantiateBeans(preInstantiateClazz);

        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        configurationScanner.scan();
        configurationScanner.registerBeans();

        beanFactory.initialize();
    }

    @Test
    void getBean() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void getBean_BeanAnnotationInConfig() {
        DataSource dataSource = beanFactory.getBean(DataSource.class);
        assertNotNull(dataSource);

        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(myJdbcTemplate);
        assertNotNull(myJdbcTemplate.getDataSource());
    }

    @Test
    void getControllers() {
        Map<Class<?>, Object> controllerBeans = beanFactory.getControllers();
        assertNotNull(controllerBeans.get(QnaController.class));
        assertNotNull(controllerBeans.get(TestQnaController.class));
    }

    @Test
    @DisplayName("하나의 Bean에 대해 하나의 인스턴스만 생성되는지 테스트")
    void singleton() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);
        TestQnaController testQnaController = beanFactory.getBean(TestQnaController.class);

        assertEquals(qnaController.getQnaService(), testQnaController.getQnaService());
    }
}
