package nextstep.di.factory.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final DataSource dataSource;

    @Inject
    public JdbcUserRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
