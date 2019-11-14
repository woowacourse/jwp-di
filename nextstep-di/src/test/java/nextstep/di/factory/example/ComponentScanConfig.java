package nextstep.di.factory.example;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.example.component.MyQnaService;
import nextstep.di.factory.example.component.QnaController;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "nextstep.di.factory.example")
public class ComponentScanConfig {
    @Bean
    public QnaController qnaController(MyQnaService myQnaService) {
        return new QnaController(myQnaService);
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("\"jdbc:h2:~/jwp-framework;MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE\"");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }
}
