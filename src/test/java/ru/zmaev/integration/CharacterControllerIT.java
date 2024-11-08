package ru.zmaev.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.container.PostgresTestContainer;
import ru.zmaev.domain.dto.request.AttributeCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CharacterControllerIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "userInfo")
    private UserInfo userInfo;

    @Test
    @WithMockUser
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_start_class.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_attribute.sql",
            "/sql/insert_build.sql",
            "/sql/insert_character.sql"})
    void findAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/characters")
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_start_class.sql",
            "/sql/insert_principal.sql"
    })
    void findAllStartClasses() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/characters/start-classes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_start_class.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_attribute.sql",
            "/sql/insert_build.sql",
            "/sql/insert_character.sql"
    })
    void findById() throws Exception {
        long existingCharacterId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/characters/" + existingCharacterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin", "editor"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_start_class.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"
    })
    void create() throws Exception {
        AttributeCreateRequestDto attribute = new AttributeCreateRequestDto(
                20,
                20,
                20,
                20,
                20,
                20,
                20,
                20
        );
        CharacterCreateRequestDto character = new CharacterCreateRequestDto(
                1L,
                1L,
                "Character",
                attribute
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(character)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_start_class.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_attribute.sql",
            "/sql/insert_build.sql",
            "/sql/insert_character.sql"
    })
    void update() throws Exception {
        AttributeCreateRequestDto attribute = new AttributeCreateRequestDto(
                20,
                20,
                20,
                20,
                20,
                20,
                20,
                20
        );
        CharacterUpdateRequestDto character = new CharacterUpdateRequestDto(
                2L,
                2L,
                "New Character",
                attribute
        );

        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_start_class.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_attribute.sql",
            "/sql/insert_build.sql",
            "/sql/insert_character.sql"
    })
    void delete() throws Exception {
        long existingCharacterId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/characters/" + existingCharacterId))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/characters/" + existingCharacterId))
                .andExpect(status().isNotFound());
    }
}
