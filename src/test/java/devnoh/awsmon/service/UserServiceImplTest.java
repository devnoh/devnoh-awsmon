package devnoh.awsmon.service;

import devnoh.awsmon.domain.Role;
import devnoh.awsmon.domain.User;
import devnoh.awsmon.repository.RoleRepository;
import devnoh.awsmon.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    RoleRepository roleRepository;

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
    public void testSaveUser() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(userService.saveUser(user), user);
    }

    @Test
    public void testSaveRole() {
        Role role = new Role();
        role.setName("role1");
        role.setDescription("desc1");
        when(roleRepository.save(role)).thenReturn(role);
        assertEquals(userService.saveRole(role), role);
    }
}
