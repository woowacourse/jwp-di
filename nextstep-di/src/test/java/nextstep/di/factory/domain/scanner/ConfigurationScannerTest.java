package nextstep.di.factory.domain.scanner;

import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.GenericBeanFactory;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationScannerTest {
    private BeanFactory beanFactory;
    private ConfigurationScanner configurationScanner;

    @BeforeEach
    public void setUp() {
        beanFactory = new GenericBeanFactory();
        configurationScanner = new ConfigurationScanner(beanFactory);
    }

    @Test
    public void getDataSourceTest() {
        configurationScanner.register(ExampleConfig.class);

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);
    }

    @Test
    public void getMyJdbcTemplateTest() {
        configurationScanner.register(IntegrationConfig.class);

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);

        assertThat(beanFactory.getBean(MyJdbcTemplate.class))
                .isInstanceOf(MyJdbcTemplate.class);
    }

    @Test
    public void singleInstanceTest() {
        configurationScanner.register(IntegrationConfig.class);

        assertThat(beanFactory.getBean(DataSource.class))
                .isEqualTo(beanFactory.getBean(DataSource.class));

        assertThat(beanFactory.getBean(MyJdbcTemplate.class))
                .isEqualTo(beanFactory.getBean(MyJdbcTemplate.class));
    }
}