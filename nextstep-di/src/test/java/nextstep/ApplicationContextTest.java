package nextstep;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.scanner.di.Config;
import nextstep.di.scanner.di.ExampleRepository;
import nextstep.exception.EmptyBasePackagesException;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationContextTest {
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    @Test
    void initializeBeans() {
        applicationContext = new ApplicationContext(Config.class, ExampleConfig.class,
                IntegrationConfig.class);
        beanFactory = applicationContext.initializeBeans();

        assertNotNull(beanFactory.getBean(ExampleRepository.class));
        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(ExampleRepository.class).getDataSource());
    }

    @Test
    void emptyBasePackages() {
        applicationContext = new ApplicationContext();
        assertThrows(EmptyBasePackagesException.class, () -> applicationContext.initializeBeans());
    }
}