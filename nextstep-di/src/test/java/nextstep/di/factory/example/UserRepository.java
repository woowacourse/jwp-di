package nextstep.di.factory.example;

import javax.sql.DataSource;

public interface UserRepository {

    DataSource getDataSource();
}
