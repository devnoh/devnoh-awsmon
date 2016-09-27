package devnoh.awsmon.service;

import devnoh.awsmon.entity.User;
import devnoh.awsmon.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by devnoh on 9/23/16.
 */
@RunWith(SpringRunner.class)
//@SpringBootTest
public class UserServiceImplTest {

    @MockBean
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        User found = (User) userService.loadUserByUsername("user1");
        System.out.println("found=" + user);
        assertNotNull(user);
        assertEquals("user1", found.getUsername());
        assertEquals("pass1", found.getPassword());
    }

    @Test
    public void testSaveDepartment() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(userService.saveUser(user), user);
    }
}
