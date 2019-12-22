package nextstep.di.factory;

import nextstep.di.definition.BeanDefinition;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanScannerTest {

    private static final String FACTORY_EXAMPLE = "nextstep.di.factory.example";

    private ConfigurationBeanScanner configurationBeanScanner;
    private ClasspathBeanScanner classpathBeanScanner;

    @BeforeEach
    void setUp() {
        configurationBeanScanner = new ConfigurationBeanScanner(FACTORY_EXAMPLE);
        classpathBeanScanner = new ClasspathBeanScanner(FACTORY_EXAMPLE);
    }

    @Test
    void register_simple() {
        final Set<BeanDefinition> beanDefinitions = configurationBeanScanner.scan();
        final BeanFactory beanFactory = new BeanFactory(beanDefinitions);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNotNull();
    }

    @Test
    void register_classpathBeanScanner_integrate() {
        final Set<BeanDefinition> beanDefinitions = configurationBeanScanner.scan();
        beanDefinitions.addAll(classpathBeanScanner.scan());
        final BeanFactory beanFactory = new BeanFactory(beanDefinitions);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNotNull();

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertThat(userRepository).isNotNull();

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertThat(jdbcTemplate).isNotNull();
        assertThat(jdbcTemplate.getDataSource()).isNotNull();
    }

}