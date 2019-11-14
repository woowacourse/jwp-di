package nextstep.di.factory.example.service;

import nextstep.annotation.Inject;
import nextstep.di.factory.example.repository.UserRepository;
import nextstep.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
