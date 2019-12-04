package nextstep.di.factory;

import java.util.List;
import javax.sql.DataSource;

import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.scannerdi.ExampleRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.scanConfigurationBeans();
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.scanConfigurationBeans();

        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.scanBeans("nextstep.di.factory.example");

        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }

    @Test
    void confirm_di_between_classpath_and_configuration_scanner() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.scanConfigurationBeans();

        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.scanBeans("nextstep.di.factory.scannerdi");

        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(ExampleRepository.class));
        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(ExampleRepository.class).getDataSource());
    }

    @Test
    void appendBasePackages() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        List<String> basePackages = cbs.appendBasePackage();

        assertTrue(basePackages.contains("nextstep.di.factory.example"));
        assertTrue(basePackages.contains("nextstep.di.factory.scannerdi"));
    }
}