package nextstep.di.factory.beans.integration;

import nextstep.stereotype.Controller;

@Controller
public class IntegrationExampleBean2 {
    private JdbcTestRepository repository;

    public IntegrationExampleBean2(JdbcTestRepository repository) {
        this.repository = repository;
    }

    public JdbcTestRepository getRepository() {
        return repository;
    }
}
