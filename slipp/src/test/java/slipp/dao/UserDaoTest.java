package slipp.dao;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ConfigurationScanner;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    private BeanFactory beanFactory = BeanFactory.initializeWith(ConfigurationScanner.of(JdbcConfiguration.class).scan());
    private JdbcTemplate jdbcTemplate = beanFactory.getBean(JdbcTemplate.class);

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, beanFactory.getBean(DataSource.class));
    }

    @Test
    public void crud() throws Exception {
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
    public void findAll() throws Exception {
        UserDao userDao = new UserDao(jdbcTemplate);
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}