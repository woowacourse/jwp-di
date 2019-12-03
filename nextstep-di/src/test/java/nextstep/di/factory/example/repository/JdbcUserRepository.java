package nextstep.di.factory.example.repository;

import nextstep.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {

    private JdbcUserRepository() {
    }
}
