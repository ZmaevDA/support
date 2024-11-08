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
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.WeaponUpdateRequestDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WeaponControllerIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_attribute.sql",
            "/sql/insert_weapon.sql"
    })
    void findAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/weapons")
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
            "/sql/insert_attribute.sql",
            "/sql/insert_weapon.sql"
    })
    void findById() throws Exception {
        long existingWeaponId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/weapons/" + existingWeaponId)
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
    void create() throws Exception {
        WeaponCreateRequestDto weapon = new WeaponCreateRequestDto(
                "New Weapon",
                "New Description",
                20D,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1
                );

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/weapons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weapon)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_attribute.sql",
            "/sql/insert_weapon.sql"
    })
    void update() throws Exception {
        WeaponUpdateRequestDto weapon = new WeaponUpdateRequestDto(
                1L,
                11L,
                "Updated Weapon",
                "Updated Description",
                10D
        );

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/weapons/{id}", weapon.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weapon)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_attribute.sql",
            "/sql/insert_weapon.sql"
    })
    void delete() throws Exception {
        long existingWeaponId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/weapons/" + existingWeaponId))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/weapons/" + existingWeaponId))
                .andExpect(status().isNotFound());
    }
}
