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
    public void getDataSourceTest() {
        BeanFactory beanFactory = new BeanFactoryImpl();
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        configurationScanner.register(ExampleConfig.class);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);
    }

    @Test
    public void getMyJdbcTemplateTest() {
        BeanFactory beanFactory = new BeanFactoryImpl();
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        configurationScanner.register(IntegrationConfig.class);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class))
                .isInstanceOf(BasicDataSource.class);

        assertThat(beanFactory.getBean(MyJdbcTemplate.class))
                .isInstanceOf(MyJdbcTemplate.class);
    }
    //@TODO 주석 제거
//
//    @Test
//    public void singleInstanceTest() {
//        BeanFactory beanFactory = new BeanFactoryImpl();
//        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
//        configurationScanner.register(IntegrationConfig.class);
//        beanFactory.initialize();
//
//        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
//        assertThat(myJdbcTemplate.getDataSource())
//                .isEqualTo(beanFactory.getBean(DataSource.class));
//    }
}