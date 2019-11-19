package samples;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    public Car car() {
        return new Car("red", "suv");
    }
}
