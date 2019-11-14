package slipp.dao;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.config.ConfigurationBean;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcUserDaoTest {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ConfigurationBean configurationBean = new ConfigurationBean();
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
        jdbcTemplate = new JdbcTemplate(configurationBean.dataSource());
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        jdbcUserDao.insert(expected);
        User actual = jdbcUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        jdbcUserDao.update(expected);
        actual = jdbcUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        List<User> users = jdbcUserDao.findAll();
        assertThat(users).hasSize(1);
    }
}