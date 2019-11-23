package samples;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "samples")
public class MyConfiguration {

    @Bean
    public Car car() {
        return new Car("red", "suv");
    }
}
