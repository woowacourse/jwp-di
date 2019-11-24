package slipp.dao;

import nextstep.di.context.ApplicationContext;
import nextstep.jdbc.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;
import slipp.service.NotFoundUserException;
import slipp.support.db.MyConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    @BeforeEach
    public void setup() {
        ApplicationContext ac = new ApplicationContext();
        ac.configurations(MyConfiguration.class);
        ac.initialize("slipp");

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = UserDao.getInstance();
        userDao.insert(expected);
        User actual = userDao.findById(expected.getUserId())
                .orElseThrow(NotFoundUserException::new);
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findById(expected.getUserId())
                .orElseThrow(NotFoundUserException::new);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        UserDao userDao = UserDao.getInstance();
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}