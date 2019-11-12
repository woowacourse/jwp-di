package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.example.MyQnaService;
import nextstep.stereotype.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {
    @Test
    void 앱_컨텍스트_초기화(){
        ApplicationContext applicationContext = new ApplicationContext(MyConfiguration.class);
        assertNotNull(applicationContext.getBean(MyQnaService.class));
    }

    @Component
    @ComponentScan(basePackages = "nextstep.di.factory.example")
    private static class MyConfiguration {
    }
}