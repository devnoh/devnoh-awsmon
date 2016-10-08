package devnoh.awsmon.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by devnoh on 9/22/16.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(Ec2Controller.class)
//@AutoConfigureMockMvc(secure = true)
public class Ec2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get("/ec2"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser(username = "user", password = "user", roles = {"USER"})
    public void testAuthorized() throws Exception {
        try {
            mockMvc.perform(get("/ec2"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("ec2"));
        } catch (Exception e) {
            // if no aws credentials file is provided
            System.out.println(e.getMessage());
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertTrue(e.getCause().getMessage().equals("profile file cannot be null"));
        }
    }
}
