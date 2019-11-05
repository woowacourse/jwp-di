package nextstep.di.factory;

import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanScannerTest {

    @Test
    @DisplayName("scan메서드에 패키지명을 넣어주면 해당 패키지의 @Controller, @Service, @Repository 구현 클래스를 스캔한다.")
    void scanAll() {
        Set<Class<?>> preInstantiateClazz = BeanScanner.scan("nextstep.di.factory.example");

        assertThat(preInstantiateClazz).contains(QnaController.class, MyQnaService.class, JdbcQuestionRepository.class, JdbcUserRepository.class);
        assertThat(preInstantiateClazz.size()).isEqualTo(4);
    }


}
