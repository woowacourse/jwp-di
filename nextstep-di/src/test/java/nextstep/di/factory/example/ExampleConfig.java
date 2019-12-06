package nextstep.di.factory.example;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "nextstep.di.factory.example")
public class ExampleConfig {
    @Bean
    public DataSource dataSource(MyQnaService myQnaService) {
        return new BasicDataSource();
    }
}
