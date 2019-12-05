package nextstep.di.factory.example;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class VoidConfig {
    private static final Logger logger = LoggerFactory.getLogger(VoidConfig.class);

    @Bean
    public void test() {
        logger.debug("Can not be bean!");
    }
}
