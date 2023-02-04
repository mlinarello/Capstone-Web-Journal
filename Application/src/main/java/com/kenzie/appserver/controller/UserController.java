package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.CreateUserRequest;
import com.kenzie.appserver.controller.model.CreateUserResponse;
import com.kenzie.appserver.controller.model.LoginRequest;
import com.kenzie.appserver.controller.model.SendEmailRequest;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createAccount(@RequestBody CreateUserRequest createUserRequest) {
        if (createUserRequest.getUsername() == null || createUserRequest.getPassword() == null || createUserRequest.getName()
                == null || createUserRequest.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = new User(createUserRequest.getUsername(),
                createUserRequest.getName(), createUserRequest.getEmail(),
                createUserRequest.getPassword());

        try {
            userService.createAccount(user);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().build();
        }

        CreateUserResponse createUserResponse = createUserResponse(user);
        return ResponseEntity.ok(createUserResponse);
    }

    private CreateUserResponse createUserResponse(User user) {
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setUsername(user.getUsername());
        createUserResponse.setPassword(user.getPassword());
        createUserResponse.setName(user.getName());
        createUserResponse.setEmail(user.getEmail());
        return createUserResponse;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
            String id = userService.login(loginRequest);
            if (id.equals("nologin")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(id);
    }


    @PostMapping("/email/welcome")
    public ResponseEntity<String> sendWelcomeEmail(@RequestBody SendEmailRequest sendEmailRequest){
        userService.sendWelcomeEmail(sendEmailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/notify/{username}")
//    public ResponseEntity<String> notify(@RequestBody NotifyUserRequest notifyUserRequest) {
//
//    }
}


