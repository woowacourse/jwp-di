package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanScannerTest {

    @DisplayName("BeanDefinition을 BeanFactory에 등록")
    @Test
    public void register() {
        BeanFactory beanFactory = new BeanFactory();

        Set<String> samplePackages = Sets.newHashSet("nextstep.di.factory.example");
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(samplePackages);
        cbs.register(beanFactory);

        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isInstanceOf(DataSource.class);
        assertThat(beanFactory.getBean(MyJdbcTemplate.class)).isInstanceOf(MyJdbcTemplate.class);
    }

    @DisplayName("@Configuration이 없을 경우에 BeanFactory에 등록")
    @Test
    void cannotRegister() {
        BeanFactory beanFactory = new BeanFactory();

        Set<String> samplePackages = Sets.newHashSet("nextstep.di.scanner");
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(samplePackages);
        cbs.register(beanFactory);
        beanFactory.initialize();

        assertThat(beanFactory.getBean(DataSource.class)).isNull();
    }
}
