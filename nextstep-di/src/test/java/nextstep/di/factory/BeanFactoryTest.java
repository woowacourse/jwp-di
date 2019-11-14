package nextstep.di.factory;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.SingletonTest1;
import nextstep.di.factory.example.SingletonTest2;
import nextstep.di.factory.exception.InvalidBeanClassTypeException;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.Scanner;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryTest {
    private List<Object> exampleBasePackages = Collections.singletonList("nextstep.di.factory.example");
    private List<Object> failBasePackages = Collections.singletonList("nextstep.di.factory.fail");

    @Test
    void di() {
        Scanner classPathBeanScanner = new ClassPathBeanScanner(exampleBasePackages.toArray());
        Set<BeanDefinition> beanDefinitions = classPathBeanScanner.scan();
        BeanFactory beanFactory = new BeanFactory(beanDefinitions);

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void 애노테이션이_있는_인터페이스() {
        Scanner classPathBeanScanner = new ClassPathBeanScanner(failBasePackages.toArray());
        assertThrows(InvalidBeanClassTypeException.class, classPathBeanScanner::scan);
    }

    @Test
    void 빈_싱글턴_보장_여부() {
        Scanner classPathBeanScanner = new ClassPathBeanScanner(exampleBasePackages.toArray());
        Set<BeanDefinition> beanDefinitions = classPathBeanScanner.scan();
        BeanFactory beanFactory = new BeanFactory(beanDefinitions);

        SingletonTest1 singletonTest1 = beanFactory.getBean(SingletonTest1.class);
        SingletonTest2 singletonTest2 = beanFactory.getBean(SingletonTest2.class);
        assertThat(singletonTest1.getQnaService()).isEqualTo(singletonTest2.getQnaService());
    }
}
