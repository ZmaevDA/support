package ru.zmaev.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zmaev.container.PostgresTestContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql"
    })
    void findAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/")
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql"
    })
    void findById() throws Exception {
        String existingUserId = "uuid1";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/" + existingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
