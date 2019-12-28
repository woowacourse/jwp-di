package nextstep.di.factory;

import nextstep.di.factory.exception.DuplicateBeanException;
import nextstep.di.factory.scanner.ClasspathBeanScanner;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClassPathBeanScannerTest {
    private ClasspathBeanScanner classpathBeanScanner;

    @Test
    void scanBeans() {
        classpathBeanScanner = new ClasspathBeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), "nextstep.di.factory.example");

        assertDoesNotThrow(() -> classpathBeanScanner.scanBeans());
    }

    @Test
    void scanBeans_중복_빈_생성_예외() {
        classpathBeanScanner = new ClasspathBeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), "nextstep.di.factory.duplication");

        assertThrows(DuplicateBeanException.class, () -> classpathBeanScanner.scanBeans());
    }
}