package nextstep.di.factory.example;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MyJdbcConfig {
    @Bean
    public MyJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new MyJdbcTemplate(dataSource);
    }
}
