package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.VoidConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConfigurationBeanFactoryTest {

    private ConfigurationBeanFactory configurationBeanFactory;

    @BeforeEach
    void setUp() {
        configurationBeanFactory = new ConfigurationBeanFactory(ExampleConfig.class, IntegrationConfig.class);
        configurationBeanFactory.initialize();
    }

    @Test
    void instantiateBeans_oneBean() {
        assertThat(configurationBeanFactory.getBean(DataSource.class)).isInstanceOf(DataSource.class);
    }

    @Test
    void instantiateBeans() {
        assertThat(configurationBeanFactory.getBean(MyJdbcTemplate.class)).isInstanceOf(MyJdbcTemplate.class);
    }

    @Test
    void voidConfig_throwException() {
        configurationBeanFactory = new ConfigurationBeanFactory(VoidConfig.class);
        assertThatThrownBy(() -> configurationBeanFactory.initialize()).isInstanceOf(RuntimeException.class);
    }
}
