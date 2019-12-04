package slipp.dao;

import java.util.List;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.jdbc.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    private static final String BASE_PACKAGE = "slipp.dao";
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        BeanFactory beanFactory = new BeanFactory();
        new ClasspathBeanScanner(beanFactory).scanBeans(BASE_PACKAGE);
        beanFactory.initialize();
        userDao = beanFactory.getBean(UserDao.class);
    }

    @Test
    public void crud() {
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