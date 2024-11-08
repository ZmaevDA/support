package ru.zmaev.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import ru.zmaev.domain.dto.request.InvitationCreateRequestDto;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class InvitationControllerIT extends PostgresTestContainer {

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
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_build.sql",
            "/sql/insert_invitation.sql"
    })
    void findAll() throws Exception {
        int pagePosition = 0;
        int pageSize = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/invitations")
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
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_build.sql",
            "/sql/insert_invitation.sql"
    })
    void findById() throws Exception {
        long existingInvitationId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/invitations/" + existingInvitationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_build.sql"
    })
    void create() throws Exception {
        Mockito.when(userInfo.getUserId()).thenReturn("uuid1");
        InvitationCreateRequestDto requestDto = new InvitationCreateRequestDto();
        requestDto.setUserIds(List.of("uuid2"));
        requestDto.setBuildId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/invitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_build.sql",
            "/sql/insert_invitation.sql"
    })
    void delete() throws Exception {
        long existingInvitationId = 1L;
        Mockito.when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/invitations/" + existingInvitationId))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/invitations/" + existingInvitationId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_build.sql",
            "/sql/insert_invitation.sql"
    })
    void addUsers() throws Exception {
        long invitationId = 1L;
        List<String> userIdsToAdd = List.of("uuid4", "uuid5");
        Mockito.when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/invitations/" + invitationId + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userIdsToAdd)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"editor"})
    @Sql(scripts = {
            "/sql/delete_all.sql",
            "/sql/insert_principal.sql",
            "/sql/insert_ashes_of_war.sql",
            "/sql/insert_weapon.sql",
            "/sql/insert_build.sql",
            "/sql/insert_invitation.sql"
    })
    void deleteUsers() throws Exception {
        long invitationId = 1L;
        String userIdToDelete = "uuid2";

        Mockito.when(userInfo.getUserId()).thenReturn("uuid1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/invitations/" + invitationId + "/users/" + userIdToDelete))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/invitations/" + invitationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
