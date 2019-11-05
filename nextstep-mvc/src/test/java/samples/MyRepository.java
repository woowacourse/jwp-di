package samples;

import nextstep.db.DataBase;
import nextstep.stereotype.Repository;

@Repository
public class MyRepository {

    public User findUserById(String userId) {
        return DataBase.findUserById(userId);
    }

    public void addUser(User user) {
        DataBase.addUser(user);
    }
}
