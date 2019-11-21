package slipp.config;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"slipp.controller", "slipp.dao"})
public class AppConfig {
}
