package nextstep.di.factory.conf;

import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.ConfigurationBeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationTest {
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        String path = "nextstep.di.factory.conf";
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(Arrays.asList(Configuration.class), path);
        beanFactory = new BeanFactory(configurationBeanScanner.scanBeans());
        beanFactory.initialize();
    }

    @Test
    public void test() {
        assertNotNull(beanFactory.getBean(DataSource.class));
    }


}
