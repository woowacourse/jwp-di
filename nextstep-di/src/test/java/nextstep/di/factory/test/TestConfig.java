package nextstep.di.factory.test;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"nextstep.di.factory.example", "nextstep.di.factory.test"})
public class TestConfig {
    @Bean
    public ComponentScanTest test() {
        return new ComponentScanTest("test");
    }
}

