package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.exception.BeanNotFoundException;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.ParameterIsNotBeanException;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @Test
    public void di() {
        reflections = new Reflections("nextstep.di.factory.example");
        Set<Class<?>> preInstantiateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstantiateClazz);
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("다른 패키지의 클래스를 찾으려고 할 때")
    void another_package_class() {
        reflections = new Reflections("nextstep.di.factory.example");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();

        assertThrows(BeanNotFoundException.class, () -> beanFactory.getBean(BeanScanner.class));
    }

    @Test
    @DisplayName("빈이 아닌 클래스가 빈인 클래스의 파라미터로 있을 때")
    void parameter_is_no_bean() {
        reflections = new Reflections("nextstep.di.factory.test");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Service.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        assertThrows(ParameterIsNotBeanException.class, () -> beanFactory.initialize());
    }

    @Test
    @DisplayName("Inject가 붙은 두개의 빈이 순환참조일 경우")
    void beans_is_circular_reference() {
        reflections = new Reflections("nextstep.di.factory.circularreference");
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Service.class);
        beanFactory = new BeanFactory(preInstanticateClazz);
        assertThrows(CircularReferenceException.class, () -> beanFactory.initialize());
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
