package nextstep.di.example;

import javax.sql.DataSource;

public interface UserRepository {

    DataSource getDataSource();
}
