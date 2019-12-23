package nextstep.di.factory.example;

import nextstep.stereotype.Service;

@Service
public class NoInjectClass {


    public NoInjectClass() {

    }

    public NoInjectClass(JdbcUserRepository jdbcUserRepository) {

    }
}
