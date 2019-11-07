package slipp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    void create() {
        UserCreatedDto userCreatedDto = new UserCreatedDto("test", "1234", "zino", "zino@zino.com");
        User user = new User("test", "1234", "zino", "zino@zino.com");

        doNothing().when(userDao).insert(user);

        assertThat(userService.create(userCreatedDto)).isEqualTo(user);
    }

    @Test
    void findById() {
        User user = new User("test", "1234", "zino", "zino@zino.com");
        when(userDao.findByUserId("test")).thenReturn(user);

        assertThat(userService.findById("test")).isEqualTo(user);
    }

    @Test
    void update() {
        String userId = "test";
        User user = new User("test", "1234", "zino", "zino@zino.com");

        UserUpdatedDto userUpdatedDto = new UserUpdatedDto("2345", "test", "test@test.test");
        when(userDao.findByUserId("test")).thenReturn(user);
        User updatedUser = userService.update(userId, userUpdatedDto);

        assertThat(updatedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(updatedUser.getPassword()).isEqualTo(userUpdatedDto.getPassword());
        assertThat(updatedUser.getName()).isEqualTo(userUpdatedDto.getName());
        assertThat(updatedUser.getEmail()).isEqualTo(userUpdatedDto.getEmail());
    }
}