package nextstep.di.context;

import nextstep.context.ApplicationContext;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.ExampleConfig;
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
        applicationContext = new ApplicationContext(ExampleConfig.class);
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