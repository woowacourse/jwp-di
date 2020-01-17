package nextstep.di.scanner.di;

import javax.sql.DataSource;

import nextstep.annotation.Inject;
import nextstep.di.factory.example.UserRepository;
import nextstep.stereotype.Repository;

@Repository
public class ExampleRepository implements UserRepository {
    private DataSource dataSource;

    @Inject
    public ExampleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
