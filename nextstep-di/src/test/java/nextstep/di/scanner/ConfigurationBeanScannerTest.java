package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanScannerTest {

    @DisplayName("BeanDefinition을 BeanFactory에 등록")
    @Test
    public void register() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner("nextstep.di.factory.example");
        cbs.register(beanFactory);

        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isInstanceOf(DataSource.class);
        assertThat(beanFactory.getBean(MyJdbcTemplate.class)).isInstanceOf(MyJdbcTemplate.class);
    }

    @DisplayName("@Configuration이 없을 경우에 BeanFactory에 등록")
    @Test
    void cannotRegister() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner("nextstep.di.factory.scanner");
        cbs.register(beanFactory);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNull();
    }

    private class TestClassWithoutConfiguration {
        @Bean
        public DataSource dataSource() {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setUrl("jdbc:h2:~/jwp-framework;MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE");
            ds.setUsername("sa");
            ds.setPassword("");
            return ds;
        }
    }
}
