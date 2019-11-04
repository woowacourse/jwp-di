package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
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

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("nextstep.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();
    }

    @Test
    public void di() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
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

    @Test
    @DisplayName("Controller 가져오기 테스트")
    void getBeansAnnotatedWith1() {
        Map<Class<?>, Object> controllerBeans = beanFactory.getBeansAnnotatedWith(Controller.class);

        assertThat(controllerBeans.size()).isEqualTo(1);
        assertThat(controllerBeans.get(QnaController.class)).isNotNull();
    }

    @Test
    @DisplayName("Service 가져오기 테스트")
    void getBeansAnnotatedWith2() {
        Map<Class<?>, Object> serviceBeans = beanFactory.getBeansAnnotatedWith(Service.class);

        assertThat(serviceBeans.size()).isEqualTo(1);
        assertThat(serviceBeans.get(MyQnaService.class)).isNotNull();
    }

    @Test
    @DisplayName("Repository 가져오기 테스트")
    void getBeansAnnotatedWith3() {
        Map<Class<?>, Object> repositoryBeans = beanFactory.getBeansAnnotatedWith(Repository.class);

        assertThat(repositoryBeans.size()).isEqualTo(2);
        assertThat(repositoryBeans.get(JdbcQuestionRepository.class)).isNotNull();
        assertThat(repositoryBeans.get(JdbcUserRepository.class)).isNotNull();
    }
}
