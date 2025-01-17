package org.gogame.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gogame.server.domain.entities.dto.user.UserInviteDto;
import org.gogame.server.auth.UserRegisterDto;
import org.gogame.server.repositories.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserFriendInviteControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserFriendInviteControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatUserCanAddFriend() throws Exception {
        UserRegisterDto regA = TestData.RegisterDtoUtils.createA();
        String regAJson = objectMapper.writeValueAsString(regA);

        MvcResult mvcResultA = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regAJson)
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.CREATED.value())
        ).andReturn();


        UserRegisterDto regB = TestData.RegisterDtoUtils.createB();
        String regBJson = objectMapper.writeValueAsString(regB);

        MvcResult mvcResultB = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regBJson)
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.CREATED.value())
        ).andReturn();

        UserInviteDto userInviteDto = UserInviteDto.builder()
                .userSenderId(1L)
                .userReceiverId(2L)
                .build();
        String userInviteDtoJson = objectMapper.writeValueAsString(userInviteDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/friend/invite/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInviteDtoJson)
                        .header("Authorization", TestData.getJwtToken(mvcResultA))
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.OK.value())
        );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/friend/invite/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInviteDtoJson)
                        .header("Authorization", TestData.getJwtToken(mvcResultB))
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.OK.value())
        );
    }

    @Test
    public void testThatUserCanRejectFriendInvite() throws Exception {
        UserRegisterDto regA = TestData.RegisterDtoUtils.createA();
        String regAJson = objectMapper.writeValueAsString(regA);

        MvcResult mvcResultA = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regAJson)
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.CREATED.value())
        ).andReturn();


        UserRegisterDto regB = TestData.RegisterDtoUtils.createB();
        String regBJson = objectMapper.writeValueAsString(regB);

        MvcResult mvcResultB = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regBJson)
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.CREATED.value())
        ).andReturn();

        UserInviteDto userInviteDto = UserInviteDto.builder()
                .userSenderId(1L)
                .userReceiverId(2L)
                .build();
        String userInviteDtoJson = objectMapper.writeValueAsString(userInviteDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/friend/invite/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInviteDtoJson)
                        .header("Authorization", TestData.getJwtToken(mvcResultA))
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.OK.value())
        );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/friend/invite/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInviteDtoJson)
                        .header("Authorization", TestData.getJwtToken(mvcResultB))
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.OK.value())
        );
    }

    @Test
    public void testDuplicateUserInvitesReturns403() throws Exception {
        UserRegisterDto regA = TestData.RegisterDtoUtils.createA();
        String regAJson = objectMapper.writeValueAsString(regA);

        MvcResult mvcResultA = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regAJson)
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.CREATED.value())
        ).andReturn();


        UserRegisterDto regB = TestData.RegisterDtoUtils.createB();
        String regBJson = objectMapper.writeValueAsString(regB);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regBJson)
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.CREATED.value())
        );

        UserInviteDto userInviteDto = UserInviteDto.builder()
                .userSenderId(1L)
                .userReceiverId(2L)
                .build();
        String userInviteDtoJson = objectMapper.writeValueAsString(userInviteDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/friend/invite/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInviteDtoJson)
                        .header("Authorization", TestData.getJwtToken(mvcResultA))
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.OK.value())
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/friend/invite/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInviteDtoJson)
                        .header("Authorization", TestData.getJwtToken(mvcResultA))
        ).andExpect(
                MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value())
        );
    }
}
