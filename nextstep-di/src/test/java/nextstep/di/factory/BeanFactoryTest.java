package nextstep.di.factory;

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
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("nextstep.di.factory.example");
        beanFactory = new BeanFactory("nextstep.di.factory.example");
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

    @Test
    @DisplayName("di된 객체와 BeanFactory가 가진 객체가 실제로 같은지 확인")
    public void beanTest() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);
        MyQnaService qnaService = beanFactory.getBean(MyQnaService.class);

        assertThat(qnaController.getQnaService()).isEqualTo(qnaService);
    }

    @Test
    public void getBeansAnnotatedWithTest() {
        Map<Class<?>, Object> controllers = beanFactory.getBeansAnnotatedWith(Controller.class);
        assertThat(controllers.get(QnaController.class)).isInstanceOf(QnaController.class);

        Map<Class<?>, Object> services = beanFactory.getBeansAnnotatedWith(Service.class);
        assertThat(services.get(MyQnaService.class)).isInstanceOf(MyQnaService.class);

        Map<Class<?>, Object> repositories = beanFactory.getBeansAnnotatedWith(Repository.class);
        assertThat(repositories.get(JdbcQuestionRepository.class)).isInstanceOf(JdbcQuestionRepository.class);
        assertThat(repositories.get(JdbcUserRepository.class)).isInstanceOf(JdbcUserRepository.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
