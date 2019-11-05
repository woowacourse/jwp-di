package samples;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class MyService {

    private final MyRepository myRepository;

    @Inject
    public MyService(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    public User findUserById(String userId) {
        return myRepository.findUserById(userId);
    }

    public void addUser(User user) {
        myRepository.addUser(user);
    }
}
