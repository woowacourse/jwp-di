package nextstep.di.scanner;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanTest {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanTest.class);

    @Test
    @DisplayName("파라미터가 있는 메소드의 파라미터")
    void getParameterTypes() {
        try {
            ConfigurationBean configurationBean = setIntegrationConfig();
            Class[] parameterType = configurationBean.getParameterTypes();
            log.debug("parameterTypes:{}", (Object) parameterType);
            assertThat(parameterType.length).isEqualTo(1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("파라미터가 없는 메소드의 파라미터")
    void getParameterTypes_notParams() {
        ConfigurationBean configurationBean = setExampleConfig();

        Class[] parameterType = configurationBean.getParameterTypes();
        log.debug("parameterTypes:{}", (Object) parameterType);
        assertThat(parameterType.length).isEqualTo(0);
    }

    @Test
    @DisplayName("파라미터가 없는 메소드 invoke")
    void getInstance_noParams() {
        ConfigurationBean configurationBean = setExampleConfig();

        Object[] objects = {};
        Object instance = configurationBean.getInstance(objects);
        log.debug("invokeClazz:{}", instance);
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("파라미터가 있는 메소드 invoke")
    void getInstance() {
        try {
            ConfigurationBean configurationBean = setIntegrationConfig();
            Object[] objects = {new BasicDataSource()};
            Object instance = Objects.requireNonNull(configurationBean).getInstance(objects);
            log.debug("invokeClazz:{}", instance);
            assertThat(instance).isNotNull();
        } catch (NoSuchMethodException e) {
            e.getMessage();
        }
    }

    @SuppressWarnings("unchecked")
    private ConfigurationBean setIntegrationConfig() throws NoSuchMethodException {
        Class clazz = IntegrationConfig.class;
        Method method = clazz.getMethod("jdbcTemplate", DataSource.class);
        log.debug("methods:{}", method);
        return new ConfigurationBean(new IntegrationConfig(), method);
    }

    private ConfigurationBean setExampleConfig() {
        Class clazz = ExampleConfig.class;
        Method[] methods = clazz.getMethods();
        log.debug("methods:{}", (Object) methods);
        return new ConfigurationBean(new ExampleConfig(), methods[0]);
    }
}