package nextstep.di.factory.beans.integration;

import nextstep.di.factory.example.UserRepository;
import nextstep.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcTestRepository implements UserRepository {
    private DataSource dataSource;

    public JdbcTestRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
