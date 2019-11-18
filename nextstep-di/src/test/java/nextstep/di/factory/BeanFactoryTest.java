package nextstep.di.factory;

import nextstep.di.factory.beans.BeanScanner;
import nextstep.di.factory.beans.ComponentBeanScanner;
import nextstep.di.factory.beans.ConfigurationBeanScanner;
import nextstep.di.factory.beans.ConstructorBeanRecipe;
import nextstep.di.factory.context.BeanFactory;
import nextstep.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new BeanFactory();
    }

    @Test
    void 빈_어노테이션_di() {
        ConfigurationBeanScanner scanner = new ConfigurationBeanScanner(ExampleConfig.class);
        beanFactory.addScanner(scanner);
        beanFactory.initialize();

        DataSource dataSource = beanFactory.getBean(DataSource.class);
        assertThat(dataSource).isNotNull();
    }

    @Test
    public void 스캐너_통합() {
        BeanScanner configScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        beanFactory.addScanner(configScanner);

        BeanScanner componentScanner = new ComponentBeanScanner("nextstep.di.factory.example");
        beanFactory.addScanner(componentScanner);

        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }

    @Test
    void 빈_인스턴스_수동_등록() {
        PlainBeanExample manual = new PlainBeanExample("POJO");
        beanFactory.addSingleton(PlainBeanExample.class, manual);
        PlainBeanExample plainBeanExample = beanFactory.getBean(PlainBeanExample.class);

        assertThat(plainBeanExample).isNotNull();
        assertThat(plainBeanExample).isEqualTo(manual);
        assertThat(plainBeanExample.getName()).isEqualTo("POJO");
    }

    @Test
    void 빈_타입_수동_등록() {
        BeanScanner configScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        beanFactory.addScanner(configScanner);
        beanFactory.initialize();

        beanFactory.addBeanType(new ConstructorBeanRecipe(CustomJdbcTemplate.class));
        beanFactory.initialize();

        CustomJdbcTemplate bean = beanFactory.getBean(CustomJdbcTemplate.class);

        assertThat(bean).isNotNull();
        assertThat(bean.getDataSource()).isEqualTo(beanFactory.getBean(DataSource.class));
    }
}
