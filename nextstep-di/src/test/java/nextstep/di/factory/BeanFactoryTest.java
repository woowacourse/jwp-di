package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.definition.BeanDefinition;
import nextstep.di.definition.ClassPathBeanDefinition;
import nextstep.di.definition.ConfigurationBeanDefinition;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private Set<BeanDefinition> beanDefinitions;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() throws NoSuchMethodException {
        reflections = new Reflections("nextstep.di.factory.example");
        IntegrationConfig config = new IntegrationConfig();
        BeanDefinition dsbd = new ConfigurationBeanDefinition(
                config,
                IntegrationConfig.class.getMethod("dataSource"));
        BeanDefinition jtbd = new ConfigurationBeanDefinition(
                config,
                IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class));

        beanDefinitions = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class).stream()
                .map(ClassPathBeanDefinition::new)
                .collect(toSet());
        beanDefinitions.add(dsbd);
        beanDefinitions.add(jtbd);
    }

    @DisplayName("Bean 초기화 확인")
    @Test
    void initialize() {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.register(beanDefinitions);

        beanFactory.initialize();

        assertThat(beanFactory.getBean(QnaController.class)).isInstanceOf(QnaController.class);
        assertThat(beanFactory.getBean(MyJdbcTemplate.class)).isInstanceOf(MyJdbcTemplate.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
