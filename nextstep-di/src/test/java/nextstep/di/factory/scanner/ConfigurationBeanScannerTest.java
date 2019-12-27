package nextstep.di.factory.scanner;

import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.bean.BeanDefinition;
import nextstep.di.factory.bean.MethodBeanDefinition;
import nextstep.di.factory.example.ExampleConfig;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    private static final Class[] AVAILABLE_ANNOTATIONS = {Configuration.class};

    @Test
    public void 팩토리_등록() {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner("nextstep.di.factory.example");
        BeanFactory beanFactory = new BeanFactory(cbs);
        beanFactory.initialize();
        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    void bean_스캐닝() throws NoSuchMethodException {
        Scanner scanner = new ConfigurationBeanScanner("samples");
        Set<BeanDefinition> beanDefinitions = scanner.scan();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            MethodBeanDefinition methodBeanDefinition = (MethodBeanDefinition) beanDefinition;
            Annotation[] annotations = methodBeanDefinition.getMethodDeclaringObject().getClass().getAnnotations();
            assertThatCode(() -> checkBeans(annotations)).doesNotThrowAnyException();
        }
        assertThat(beanDefinitions.size()).isEqualTo(1);
    }

    @Test
    void 수동_설정_등록() {
        ConfigurationBeanScanner scanner = new ConfigurationBeanScanner();
        scanner.resister(ExampleConfig.class);
        assertThat(scanner.scan().size()).isEqualTo(1);
    }

    @Test
    void 스캔_되지_않은_클래스_확인() {
        Scanner scanner = new ClassPathBeanScanner("samples");
        Set<BeanDefinition> beanDefinitions = scanner.scan();
        beanDefinitions
                .forEach((beanDefinition) -> assertThat(beanDefinition.getClass()).isNotEqualTo(NotAnnotated.class));
    }

    private void checkBeans(Annotation[] annotations) {
        Arrays.stream(annotations)
                .filter(annotation -> Arrays.asList(AVAILABLE_ANNOTATIONS).contains(annotation.annotationType()))
                .findFirst()
                .orElseThrow(() -> new AnnotationNotFoundException("Error: Annotation not available"));
    }
}