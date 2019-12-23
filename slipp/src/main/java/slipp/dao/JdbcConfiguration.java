package slipp.dao;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/jwp-framework;MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    @Bean
    public static DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DB_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PW);

        return ds;
    }

    @Bean
    public static JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return JdbcTemplate.of(dataSource);

        // [TODO] inter-bean-reference 적용하기
        // return new JdbcTemplate(dataSource());
    }
}
