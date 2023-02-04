package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CreateUserRequest;
import com.kenzie.appserver.controller.model.LoginRequest;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@IntegrationTest
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserService userService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();
    /** ------------------------------------------------------------------------
     *  createAccount
     *  ------------------------------------------------------------------------ **/
    @Test
    public void createAccount_goodAttempt_accountCreated() throws Exception {
        String email = "afakeemail@fakewebsite.fake";
        String name = "john johnson";
        String password = "password";
        String username = "johnnyBoy";

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(email);
        createUserRequest.setName(name);
        createUserRequest.setPassword(password);
        createUserRequest.setUsername(username);

        mvc.perform(post("/user/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createUserRequest)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("username")
                        .value(is(username)))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("password")
                        .value(is(password)))
                .andExpect(jsonPath("email")
                        .value(is(email)));

    }

    @Test
    public void createAccount_missingName_badRequest() throws Exception {
        String email = "afakeemail@fakewebsite.fake";
        String name = "john johnson";
        String password = "password";
        String username = "johnnyBoy";

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(email);
        createUserRequest.setPassword(password);
        createUserRequest.setUsername(username);

        mvc.perform(post("/user/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createUserRequest)))

                .andExpect(status().isBadRequest());
    }
    /** ------------------------------------------------------------------------
     *  login
     *  ------------------------------------------------------------------------ **/

    @Test
    public void login_accountExistsGoodRequest_loginString() throws Exception {
        String name = mockNeat.names().valStr();
        String username = mockNeat.users().valStr();
        String email = "email@website.fake";
        String password = mockNeat.passwords().valStr();

        User user = new User(username, name, email, password);

        User persistedUser = userService.createAccount(user);

        LoginRequest loginRequest = new LoginRequest(username, password);

        mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))

                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(username));
    }

    @Test
    public void login_accountExistsBadRequest_badLoginString() throws Exception {
        String name = mockNeat.names().valStr();
        String email = "email@website.fake";
        String username = mockNeat.users().valStr();
        String password = mockNeat.passwords().valStr();

        String incorrectPassword = "jisdhbdhfbkjfdhb fkjhbvsfd";

        User user = new User(username, name, email, password);

        User persistedUser = userService.createAccount(user);

        LoginRequest loginRequest = new LoginRequest(username, incorrectPassword);

        mvc.perform(post("/users/login")
                        .accept(MediaType.TEXT_PLAIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))

                .andExpect(status().isNotFound());
    }

    @Test
    public void login_accountDoesNotExist_badRequest() throws Exception {
        String username = mockNeat.users().valStr();
        String password = mockNeat.passwords().valStr();

        LoginRequest loginRequest = new LoginRequest(username, password);

        mvc.perform(post("/user/login")
                        .accept(MediaType.TEXT_PLAIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))

                .andExpect(status().isBadRequest());
    }
    /** ------------------------------------------------------------------------
     *  sendEmailReminders
     *  ------------------------------------------------------------------------ **/

    /** ------------------------------------------------------------------------
     *  JournalService.getJournalById
     *  ------------------------------------------------------------------------ **/
}
