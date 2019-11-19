package nextstep.di.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcUserRepository implements UserRepository {
    private MyJdbcTemplate myJdbcTemplate;

    @Inject
    public JdbcUserRepository(MyJdbcTemplate myJdbcTemplate) {
        this.myJdbcTemplate = myJdbcTemplate;
    }

    public DataSource getDataSource() {
        return myJdbcTemplate.getDataSource();
    }
}
