package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.*;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger( BeanFactoryTest.class );

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

    @Test
    public void recursiveReferenceException() {
        BeanFactory beanFactory = new BeanFactory(Set.of(RecursiveController.class));
        assertThrows(RecursiveFieldException.class, beanFactory::initialize);
    }

    @Test
    public void noDefaultConstructorException() {
        BeanFactory beanFactory = new BeanFactory(Set.of(NoDefaultCtorController.class));
        assertThrows(NoDefaultConstructorException.class, beanFactory::initialize);
    }

    @Test
    public void implClassNotFoundException() {
        BeanFactory beanFactory = new BeanFactory(Set.of(NoImplService.class));
        assertThrows(ImplClassNotFoundException.class, beanFactory::initialize);
    }

    @Test
    public void interfaceCannotInstantiatedException() {
        BeanFactory beanFactory = new BeanFactory(Set.of(NoImplRepository.class));
        assertThrows(InterfaceCannotInstantiatedException.class, beanFactory::initialize);
    }

    @Test
    public void primitiveTypeInjectionFailException() {
        BeanFactory beanFactory = new BeanFactory(Set.of(PrimitiveTypeInjectController.class));
        assertThrows(PrimitiveTypeInjectionFailException.class, beanFactory::initialize);
    }

    @Test
    public void interfaceExtendsInterfaceSuccess() {
        BeanFactory beanFactory = new BeanFactory(Set.of(NoImplService.class, ImplIntermediateRepository.class));
        beanFactory.initialize();
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
