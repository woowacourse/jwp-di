package nextstep.di.factory;

import nextstep.annotation.Configuration;
import nextstep.di.factory.example.*;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        String path = "nextstep.di.factory.example";
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), path);
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(Arrays.asList(Configuration.class), path);

        Map<Class<?>, BeanDefinition> classBeanDefinitionMap = classpathBeanScanner.scanBeans();
        classBeanDefinitionMap.putAll(configurationBeanScanner.scanBeans());

        beanFactory = new BeanFactory(classBeanDefinitionMap);
        beanFactory.initialize();
    }

    @Test
    @DisplayName("DI Test")
    public void di() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("Controller 가져오기 테스트")
    public void getControllers() {
        Map<Class<?>, Object> beanByAnnotation = beanFactory.getBeanByAnnotation(Controller.class);

        assertNotNull(beanByAnnotation.get(QnaController.class));
    }

    @Test
    @DisplayName("Service 가져오기 테스트")
    public void getServices() {
        Map<Class<?>, Object> beanByAnnotation = beanFactory.getBeanByAnnotation(Service.class);

        assertNotNull(beanByAnnotation.get(MyQnaService.class));
    }

    @Test
    @DisplayName("Repository 가져오기 테스트")
    public void getRepositories() {
        Map<Class<?>, Object> beanByAnnotation = beanFactory.getBeanByAnnotation(Repository.class);

        assertNotNull(beanByAnnotation.get(JdbcUserRepository.class));
        assertNotNull(beanByAnnotation.get(JdbcQuestionRepository.class));
    }

    @Test
    @DisplayName("설정파일에 있는 메소드 빈 가져오기 테스트")
    public void getConfigurationBean() {
        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(MyJdbcTemplate.class));
    }

    @Test
    @DisplayName("특정 빈 가져오기 테스트")
    public void getBean() {
        Object bean1 = beanFactory.getBean(MyQnaService.class).getUserRepository();
        Object bean2 = beanFactory.getBean(JdbcUserRepository.class);

        Object bean3 = beanFactory.getBean(MyQnaService.class).getQuestionRepository();
        Object bean4 = beanFactory.getBean(JdbcQuestionRepository.class);

        assertThat(bean1).isEqualTo(bean2);
        assertThat(bean3).isEqualTo(bean4);
    }

    @Test
    @DisplayName("싱글 인스턴스 테스트")
    public void getSingleInstance() {
        final MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);
        final QuestionRepository actual = myQnaService.getQuestionRepository();
        final QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);
        assertThat(actual).isEqualTo(expected);
    }

}
