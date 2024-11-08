package ru.zmaev.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.container.PostgresTestContainer;
import ru.zmaev.domain.dto.request.AshesOfWarWeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildUpdateRequestDto;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BuildControllerIT extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "userInfo")
    private UserInfo userInfo;

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"})
    void findAllPublic() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/builds/public")
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"
    })
    void findAllCommentsByBuild() throws Exception {
        long buildId = 1L;
        int pagePosition = 0;
        int pageSize = 10;
        Sort.Direction sortDirection = Sort.Direction.ASC;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/builds/" + buildId + "/comments")
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("sortDirection", String.valueOf(sortDirection))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"})
    void findAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/builds/all")
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"admin"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"})
    void findAllAllowedUser() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;
        String userId = "uuid1";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/builds/" + userId + "/private")
                        .param("pagePosition", String.valueOf(pagePosition))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"})
    void findById() throws Exception {
        long existingBuildId = 1L;
        when(userInfo.getUserId()).thenReturn("uuid1");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/builds/" + existingBuildId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"})
    void isExistById() throws Exception {
        long existingBuildId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/builds/" + existingBuildId + "/exists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql"
    })
    void create() throws Exception {
        AshesOfWarWeaponCreateRequestDto a1 = new AshesOfWarWeaponCreateRequestDto(1L, 1L);
        AshesOfWarWeaponCreateRequestDto a2 = new AshesOfWarWeaponCreateRequestDto(2L, 2L);
        List<AshesOfWarWeaponCreateRequestDto> ashesOfWarWeapons = List.of(a1, a2);

        BuildCreateRequestDto build = new BuildCreateRequestDto();
        build.setBuildName("New Build");
        build.setBuildDescription("Description");
        build.setAshesOfWarWeapons(ashesOfWarWeapons);
        build.setInventoryItemIds(List.of(1L, 2L));
        build.setIsPrivate(false);

        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/builds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(build)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql",
    })
    void addComment() throws Exception {
        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/builds/1/users/uuid1/comments")
                        .param("content", "content")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql",
            "/sql/insert_comment.sql",
    })
    void deleteComment() throws Exception {
        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/builds/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql",
    })
    void addReaction() throws Exception {
        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/builds/1/users/uuid1/reactions")
                        .param("reactionType", "LIKE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql",
    })
    void deleteReaction() throws Exception {
        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/builds/reactions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"})
    void update() throws Exception {
        AshesOfWarWeaponCreateRequestDto a1 = new AshesOfWarWeaponCreateRequestDto(1L, 1L);
        AshesOfWarWeaponCreateRequestDto a2 = new AshesOfWarWeaponCreateRequestDto(2L, 2L);
        List<AshesOfWarWeaponCreateRequestDto> ashesOfWarWeapons = List.of(a1, a2);

        BuildUpdateRequestDto build = new BuildUpdateRequestDto();

        build.setId(1L);
        build.setBuildName("Updated Build");
        build.setBuildDescription("Updated Description");
        build.setAshesOfWars(ashesOfWarWeapons);
        build.setInventoryItemIds(List.of(1L, 2L));
        build.setIsPrivate(false);

        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/builds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(build)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {"/sql/delete_all.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_build.sql"})
    void delete() throws Exception {
        long existingBuildId = 1L;

        when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/builds/" + existingBuildId))
                .andExpect(status().isOk());
    }
}
