package nextstep.di.factory.exampleforinvalid.multipleconcreteclass;

import nextstep.di.factory.example.UserRepository;
import nextstep.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
