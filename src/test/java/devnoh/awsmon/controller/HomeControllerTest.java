package devnoh.awsmon.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by devnoh on 9/22/16.
 */
@RunWith(SpringRunner.class)
//@WebMvcTest(HomeController.class)
//@AutoConfigureMockMvc(secure = true)
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
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get("/"))
                //.andExpect(status().is4xxClientError());
                .andExpect(status().is3xxRedirection());
    }

    @Test
    //@WithMockUser(username = "user", password = "password", roles = "USER")
    //@WithMockCustomUser(username = "user", password = "pass", roles = "USER")
    @WithUserDetails(value = "user")
    public void testAuthorized() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Log Out")));
    }
}
