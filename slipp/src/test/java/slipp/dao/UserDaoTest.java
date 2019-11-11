package slipp.dao;

import nextstep.di.factory.ApplicationContext;
import nextstep.jdbc.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.WebServerLauncher;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;
import slipp.support.MyConfiguration;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    private ApplicationContext applicationContext;
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        applicationContext = new ApplicationContext(WebServerLauncher.class, MyConfiguration.class);
        applicationContext.initialize();
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, applicationContext.getBean(DataSource.class));
        userDao = applicationContext.getBean(UserDao.class);
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}