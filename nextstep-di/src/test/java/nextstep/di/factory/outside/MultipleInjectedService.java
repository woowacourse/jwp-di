package nextstep.di.factory.outside;

import nextstep.annotation.Inject;
import nextstep.di.factory.example.repository.QuestionRepository;
import nextstep.di.factory.example.repository.UserRepository;
import nextstep.stereotype.Service;

@Service
public class MultipleInjectedService {
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    @Inject
    public MultipleInjectedService(UserRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Inject
    public MultipleInjectedService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }
}
