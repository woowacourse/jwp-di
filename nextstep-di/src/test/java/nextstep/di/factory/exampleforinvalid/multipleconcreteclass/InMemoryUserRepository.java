package nextstep.di.factory.exampleforinvalid.multipleconcreteclass;

import nextstep.di.factory.example.UserRepository;
import nextstep.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
}
