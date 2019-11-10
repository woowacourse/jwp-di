package nextstep.di.factory.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class NewQnaService {
    private JdbcUserRepository userRepository;
    private QuestionRepository questionRepository;

    @Inject
    public NewQnaService(JdbcUserRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    public JdbcUserRepository getUserRepository() {
        return userRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }
}
