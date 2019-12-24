package nextstep.di.factory;

import nextstep.di.factory.beandefinition.BeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest extends AbstractBeanScannerTest {

    @BeforeEach
    void setUp() {
        initConfigurationpathBeanScanner();
    }

    @Test
    public void register_simple() {
        Set<BeanDefinition> beanDefinitions = cbds.scan();
        BeanFactory beanFactory = new BeanFactory(beanDefinitions);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void scanTest() {
        Set<BeanDefinition> beanDefinitions = cbds.scan();

        assertThat(beanDefinitions).isNotNull();
    }
}