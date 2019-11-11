package nextstep.mvc.tobe;

import nextstep.di.factory.BeanFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);

        cbs.doConfiguration();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }
}
