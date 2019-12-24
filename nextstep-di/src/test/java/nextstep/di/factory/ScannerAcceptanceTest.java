package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.example.api.MyJdbcTemplate;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScannerAcceptanceTest extends AbstractBeanScannerTest {

    @BeforeEach
    void setUp() {
        initAllBeanScanner();
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
