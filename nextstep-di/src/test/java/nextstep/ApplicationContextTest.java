package nextstep;

import javax.sql.DataSource;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.scannerdi.ExampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationContextTest {
    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        beanFactory = new ApplicationContext().initializeBeans();
    }

    @Test
    public void register_classpathBeanScanner() {
        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }

    @Test
    void confirm_di_between_classpath_and_configuration_scanner() {
        assertNotNull(beanFactory.getBean(ExampleRepository.class));
        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(ExampleRepository.class).getDataSource());
    }
}