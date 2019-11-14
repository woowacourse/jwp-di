package nextstep.di.factory.domain.scanner;

import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.GenericBeanFactory;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationScannerTest {

    @Test
    public void getDataSourceTest() {
        BeanFactory beanFactory = new GenericBeanFactory();
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        configurationScanner.register(ExampleConfig.class);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);
    }

    @Test
    public void getMyJdbcTemplateTest() {
        BeanFactory beanFactory = new GenericBeanFactory();
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        configurationScanner.register(IntegrationConfig.class);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);

        assertThat(beanFactory.getBean(MyJdbcTemplate.class))
                .isInstanceOf(MyJdbcTemplate.class);
    }
}