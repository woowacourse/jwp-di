package nextstep.di.factory;

import nextstep.annotation.Configuration;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
//        BeanFactory beanFactory = new BeanFactory();
//        cbs.register(ExampleConfig.class);
//        beanFactory.initialize();
//
//        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    void scanBeans() {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(Collections.singletonList(Configuration.class));
        List<Method> methods = cbs.scanBeans();

        assertThat(methods.size()).isEqualTo(3);
    }
}
