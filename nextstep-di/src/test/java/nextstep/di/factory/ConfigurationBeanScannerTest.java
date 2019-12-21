package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.example.api.MyJdbcTemplate;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.scanner.ClasspathBeanScanner;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {

    private ConfigurationBeanScanner cbs;
    private ClasspathBeanScanner cbds;

    @BeforeEach
    void setUp() {
        cbs = new ConfigurationBeanScanner("nextstep.di.factory.example.");
        cbds = new ClasspathBeanScanner("nextstep.di.factory.example.");
    }

    @Test
    public void register_simple() {
        Set<BeanDefinition> beanDefinitions = cbs.scan();
        BeanFactory beanFactory = new BeanFactory(beanDefinitions);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void scanTest() {
        Set<BeanDefinition> beanDefinitions = cbs.scan();

        assertThat(beanDefinitions).isNotNull();
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        Sets.SetView<BeanDefinition> beanDefinitions = Sets.union(cbs.scan(), cbds.scan());
        BeanFactory beanFactory = new BeanFactory(beanDefinitions);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }

}