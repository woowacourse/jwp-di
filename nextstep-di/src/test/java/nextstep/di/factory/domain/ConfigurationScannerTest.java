package nextstep.di.factory.domain;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationScannerTest {

    @Test
    public void test() {
        ConfigurationScanner configurationScanner = new ConfigurationScanner();
        configurationScanner.initialize(ExampleConfig.class);
        BeanFactory2 beanFactory = new BeanFactory2();
        configurationScanner.scanBeanFactory(beanFactory);

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);
    }

    @Test
    public void test2() {
        ConfigurationScanner configurationScanner = new ConfigurationScanner();
        configurationScanner.initialize(IntegrationConfig.class);
        BeanFactory2 beanFactory = new BeanFactory2();
        configurationScanner.scanBeanFactory(beanFactory);

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);

        assertThat(beanFactory.getBean(MyJdbcTemplate.class))
                .isInstanceOf(MyJdbcTemplate.class);
    }

    @Test
    public void singleInstanceTest() {
        ConfigurationScanner configurationScanner = new ConfigurationScanner();
        configurationScanner.initialize(IntegrationConfig.class);
        BeanFactory2 beanFactory = new BeanFactory2();
        configurationScanner.scanBeanFactory(beanFactory);

        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertThat(myJdbcTemplate.getDataSource())
                .isEqualTo(beanFactory.getBean(DataSource.class));
    }
}