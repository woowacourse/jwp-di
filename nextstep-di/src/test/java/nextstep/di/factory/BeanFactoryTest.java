package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.QuestionRepository;
import nextstep.di.factory.example2.TestService;
import nextstep.di.factory.example2.TestServiceObject;
import nextstep.di.factory.exception.ScannerException;
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
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;
    private Reflections reflections;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        reflections = new Reflections("nextstep.di.factory.example.");
        Set<Class<?>> preInstantiateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstantiateClazz);
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
    @DisplayName("QuestionRepository가_싱글_인스턴스가_맞는지_테스트")
    void singleInstanceTest() {
        final MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);

        final QuestionRepository actual = myQnaService.getQuestionRepository();
        final QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void beanScopeTest() {
        assertThat(beanFactory.getBean(TestService.class)).isNull();
        assertThat(beanFactory.getBean(TestServiceObject.class)).isNull();
    }

    @SafeVarargs
    private final Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }

}
