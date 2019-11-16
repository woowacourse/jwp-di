package nextstep.di.factory;

import nextstep.di.factory.beans.ConstructorBeanRecipe;
import nextstep.di.factory.context.BeanFactory;
import nextstep.di.factory.example.*;
import nextstep.di.factory.scanner.BeanScanner;
import nextstep.di.factory.scanner.ComponentBeanScanner;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;
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
    public void 스캐너_통() {
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
        PlainBean manual = new PlainBean("POJO");
        beanFactory.addSingleton(PlainBean.class, manual);
        PlainBean plainBean = beanFactory.getBean(PlainBean.class);

        assertThat(plainBean).isNotNull();
        assertThat(plainBean).isEqualTo(manual);
        assertThat(plainBean.getName()).isEqualTo("POJO");
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
