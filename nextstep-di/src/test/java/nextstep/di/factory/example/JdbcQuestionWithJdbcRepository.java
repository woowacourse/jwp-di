package nextstep.di.factory.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Repository;

@Repository
public class JdbcQuestionWithJdbcRepository implements QuestionRepository {
    private MyJdbcTemplate myJdbcTemplate;

    @Inject
    public JdbcQuestionWithJdbcRepository(MyJdbcTemplate myJdbcTemplate) {
        this.myJdbcTemplate = myJdbcTemplate;
    }
}
