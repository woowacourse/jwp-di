package nextstep.di.factory.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

import javax.sql.DataSource;

@Controller
public class BeanInjectedComponent {
    private DataSource dataSource;

    @Inject
    public BeanInjectedComponent(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }
}