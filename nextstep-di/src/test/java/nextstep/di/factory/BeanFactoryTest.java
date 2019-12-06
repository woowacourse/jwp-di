package nextstep.di.factory;


import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    protected Reflections reflections;
    protected BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        Set<Class<?>> preInstantiateComponents = beanScanner.scan();
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        Set<Class<?>> preInstantiateConfigurationBeans = configurationBeanScanner.scan(IntegrationConfig.class);

        beanFactory = new BeanFactory(preInstantiateComponents, preInstantiateConfigurationBeans);
    }

    @Test
    void initialize() {
        Map<Class<?>, Object> beans = beanFactory.initialize(IntegrationConfig.class);
        assertNotNull(beans.get(MyJdbcTemplate.class));
        assertNotNull(beans.get(DataSource.class));
        assertNotNull(beans.get(MyQnaService.class));
        assertNotNull(beans.get(QnaController.class));
    }
}
