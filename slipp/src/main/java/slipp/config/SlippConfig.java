package slipp.config;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.jdbc.ConnectionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "slipp")
public class SlippConfig {
    @Bean
    public DataSource dataSource() {
        return ConnectionManager.getDataSource();
    }
}
