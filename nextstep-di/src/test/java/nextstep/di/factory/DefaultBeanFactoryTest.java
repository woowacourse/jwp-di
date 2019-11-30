package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DefaultBeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(DefaultBeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory defaultBeanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("nextstep.di.factory.example");
        Set<Class<?>> preInstantiateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        defaultBeanFactory = new DefaultBeanFactory(preInstantiateClazz);
        defaultBeanFactory.initialize();
    }

    @Test
    public void di() {
        QnaController qnaController = defaultBeanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void di2() {
        JdbcUserRepository repository = defaultBeanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(repository);
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
    public void equals() {
        MyQnaService qnaService = defaultBeanFactory.getBean(QnaController.class).getQnaService();
        MyQnaService bean = defaultBeanFactory.getBean(MyQnaService.class);
        assertSame(qnaService, bean);
    }
}
