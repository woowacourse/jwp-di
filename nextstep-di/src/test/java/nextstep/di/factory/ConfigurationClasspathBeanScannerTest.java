package nextstep.di.factory;

import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigurationClasspathBeanScannerTest {

    @Test
    void scanBeans_성공() {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(Collections.singletonList(Configuration.class), "nextstep.di.factory.example");
        Map<Class<?>, BeanDefinition> classBeanDefinitionMap = cbs.scanBeans();

        assertThat(classBeanDefinitionMap.size()).isEqualTo(2);
    }

    @Test
    void scanBeans_리턴타입이_같은_메서드일때_예외() {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(Collections.singletonList(Configuration.class), "nextstep.di.factory.duplication");

        assertThrows(DuplicateBeanException.class, () -> cbs.scanBeans());
    }
}
