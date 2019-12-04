package nextstep.di.factory.scannerdi;

import javax.sql.DataSource;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;

@Configuration
@ComponentScan({"nextstep.di.factory.scannerdi"})
public class Config {
    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:~/jwp-framework;MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }
}
