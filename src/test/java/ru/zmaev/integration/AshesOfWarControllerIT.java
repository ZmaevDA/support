package ru.zmaev.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.zmaev.domain.dto.request.AshesOfWarCreateRequestDto;
import ru.zmaev.domain.dto.request.AshesOfWarUpdateRequestDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AshesOfWarControllerIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql"
    })
    void findAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/ashes-of-war")
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql"
    })
    void findById() throws Exception {
        long existingAshesOfWarId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/ashes-of-war/" + existingAshesOfWarId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_principal.sql",
    })
    void create() throws Exception {
        AshesOfWarCreateRequestDto ashesOfWar = new AshesOfWarCreateRequestDto();
        ashesOfWar.setName("Ash of War");
        ashesOfWar.setDescription("Description");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/ashes-of-war")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ashesOfWar)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql"
    })
    void update() throws Exception {
        AshesOfWarUpdateRequestDto ashesOfWar = new AshesOfWarUpdateRequestDto(
                1L,
                "Updated Ash of War",
                "Updated Description"
        );

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/ashes-of-war")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ashesOfWar)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql"
    })
    void delete() throws Exception {
        long existingAshesOfWarId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/ashes-of-war/" + existingAshesOfWarId))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/ashes-of-war/" + existingAshesOfWarId))
                .andExpect(status().isNotFound());
    }
}
