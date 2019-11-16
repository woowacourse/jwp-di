package nextstep.di.factory.example.service;

import nextstep.di.factory.example.repository.QuestionRepository;
import nextstep.di.factory.example.repository.UserRepository;
import nextstep.stereotype.Service;

@Service
public class TestService {
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    public TestService(UserRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }
}
