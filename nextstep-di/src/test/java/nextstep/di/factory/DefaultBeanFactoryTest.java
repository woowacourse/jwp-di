package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.bean.BeanDefinitionRegistry;
import nextstep.di.bean.DefaultBeanDefinitionRegistry;
import nextstep.di.example.*;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ComponentScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DefaultBeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(DefaultBeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        final Object[] basePackages = new ComponentScanner(IntegrationConfig.class).getBasePackages();
        BeanDefinitionRegistry registry = new DefaultBeanDefinitionRegistry();
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(basePackages);
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(basePackages);

        registry.register(configurationBeanScanner.getBeanDefinitions());
        registry.register(classpathBeanScanner.getBeanDefinitions());

        beanFactory = new DefaultBeanFactory(registry);
    }

    @Test
    public void di() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void findMethodsByAnnotationTest() throws NoSuchMethodException {
        Method expected = QnaController.class.getMethod("test");
        Set<Method> methods = beanFactory.findMethodsByAnnotation(TestMethodAnnotation.class, Controller.class);

        assertThat(methods).hasSize(1);
        assertThat(methods).contains(expected);
    }

    @Test
    void 싱글인스턴스_확인() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);
        MyQnaService actual = beanFactory.getBean(MyQnaService.class);
        MyQnaService expected = qnaController.getQnaService();

        assertThat(expected == actual).isTrue();
    }

    @Test
    void QuestionRepository가_싱글_인스턴스가_맞는지_테스트() {
        final MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);

        final QuestionRepository actual = myQnaService.getQuestionRepository();
        final QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);

        assertThat(actual).isEqualTo(expected);
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
