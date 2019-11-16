package slipp;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.jdbc.JdbcTemplate;
import slipp.dao.UserDao;

@Configuration
@ComponentScan(basePackages = "slipp.controller")
public class SlippConfig {
    @Bean
    public UserDao userDao(JdbcTemplate jdbcTemplate) {
        return new UserDao(jdbcTemplate);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate();
    }
}
