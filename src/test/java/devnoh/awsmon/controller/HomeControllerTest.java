package devnoh.awsmon.controller;

import devnoh.awsmon.config.SecurityConfig;
import devnoh.awsmon.config.WebMvcConfig;
import devnoh.awsmon.controller.HomeController;
import devnoh.awsmon.entity.User;
import devnoh.awsmon.service.UserService;
import devnoh.awsmon.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by devnoh on 9/22/16.
 */
//@RunWith(SpringRunner.class)
//@WebMvcTest(HomeController.class)
//@AutoConfigureMockMvc(secure = true)
@RunWith(SpringRunner.class)
@SpringBootTest
public class HomeControllerTest {

//    @Autowired
//    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webContext;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/"))
                //.andExpect(status().is4xxClientError());
                .andExpect(status().is3xxRedirection());
    }

    @Test
    //@WithMockUser(username = "user", password = "password", roles = "USER")
    @WithUserDetails(value="admin")
    //@WithMockCustomUser(username = "user", password = "pass", roles = "USER")
    public void testHomeAuhthorized() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

}
