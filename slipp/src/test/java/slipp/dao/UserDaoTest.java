package slipp.dao;

import nextstep.di.ApplicationContext;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.ApplicationContextRoot;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    private final ApplicationContext context = new ApplicationContext(ApplicationContextRoot.class);
    private final JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

    @BeforeEach
    void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, context.getBean(DataSource.class));
    }

    @Test
    void crud() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao(jdbcTemplate);
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAll() {
        UserDao userDao = new UserDao(jdbcTemplate);
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}