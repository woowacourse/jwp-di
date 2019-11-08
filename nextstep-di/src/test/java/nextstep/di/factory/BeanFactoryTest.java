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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("nextstep.di.factory.example");
        Set<Class<?>> preInstantiateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstantiateClazz);
        beanFactory.initialize();
    }

    @Test
    public void di() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void diRepository() {
        JdbcUserRepository repository = beanFactory.getBean(JdbcUserRepository.class);
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
    public void getBeansTest() {
        assertThat(beanFactory.getBeans(Controller.class)).containsKey(QnaController.class);
    }

    @Test
    public void equals() {
        MyQnaService qnaService = beanFactory.getBean(QnaController.class).getQnaService();
        MyQnaService bean = beanFactory.getBean(MyQnaService.class);
        assertSame(qnaService, bean);
    }
}
