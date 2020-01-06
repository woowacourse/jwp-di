package nextstep.di.definition;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanDefinitionTest {

    @DisplayName("인스턴스화 확인")
    @Test
    void instantiate() throws Exception {
        IntegrationConfig config = new IntegrationConfig();
        BeanDefinition dsbd = new ConfigurationBeanDefinition(
                config,
                IntegrationConfig.class.getMethod("dataSource"));
        BeanDefinition jtbd = new ConfigurationBeanDefinition(
                config,
                IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class));

        assertThat(dsbd.instantiate()).isInstanceOf(DataSource.class);
        assertThat(jtbd.instantiate(new BasicDataSource())).isInstanceOf(MyJdbcTemplate.class);
    }
}
