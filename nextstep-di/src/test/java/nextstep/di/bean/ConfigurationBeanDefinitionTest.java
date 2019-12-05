package nextstep.di.bean;

import nextstep.di.factory.ReflectionUtils;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanDefinitionTest {
    private BeanDefinition beanDefinition;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        Method beanMethod = IntegrationConfig.class.getDeclaredMethod("jdbcTemplate", DataSource.class);
        IntegrationConfig configInstance = ReflectionUtils.newInstance(IntegrationConfig.class);
        beanDefinition = new ConfigurationBeanDefinition(configInstance, beanMethod);
    }

    @Test
    void getClazz() {
        assertThat(beanDefinition.getClazz()).isEqualTo(MyJdbcTemplate.class);
    }

    @Test
    void getParameterTypes() {
        assertThat(beanDefinition.getParameterTypes()).contains(DataSource.class);
    }

    @Test
    void instantiate() {
        assertThat(beanDefinition.instantiate(new BasicDataSource())).isNotNull();

    }
}