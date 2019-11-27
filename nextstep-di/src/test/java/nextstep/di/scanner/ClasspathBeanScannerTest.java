package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.JdbcQuestionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClasspathBeanScannerTest {

    private static final String TEST_BASE_PACKAGE = "nextstep.di.factory.example";

    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        BeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(IntegrationConfig.class);

        BeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.doScan(TEST_BASE_PACKAGE);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(JdbcQuestionRepository.class));
    }
}