package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.LoginRequest;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private LambdaServiceClient lambdaServiceClient;

    @Autowired
    public UserService(UserRepository userRepository, LambdaServiceClient lambdaServiceClient) {
        this.userRepository = userRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createAccount(User user) {

        if (userAlreadyExists(user)) {
            throw new IllegalArgumentException();
        }

        UserRecord userRecord = new UserRecord();
        userRecord.setName(user.getName());
        userRecord.setUsername(user.getUsername());
        userRecord.setEmail(user.getEmail());
        userRecord.setPassword(user.getPassword());

        userRepository.save(userRecord);
        try{
            if (user.getEmail().equals("mlinarello25@gmail.com")) {
                sendWelcomeEmail(user.getEmail());
                //This was done because this email was the only email verified by MailGun API, and thus
                //was the only email address that would actually result in an email being sent.
                // Any other email results in 400 error from MailGun.
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public String hashPassword(String password) {
        return String.valueOf(password.hashCode());
    }
    public boolean userAlreadyExists(User user) {
        return userRepository.existsById(user.getUsername());
    }

    public String login(LoginRequest loginRequest) {
        System.out.println(loginRequest.toString());
        if(userRepository.existsById(loginRequest.getUsername())){
            Optional<UserRecord> userRecord = userRepository.findById(loginRequest.getUsername());
            if(userRecord.get().getPassword().equals(loginRequest.getPassword())){
                return userRecord.get().getUsername();
            }
        }
        return "nologin";
    }

    public void sendWelcomeEmail(String email) {
        lambdaServiceClient.sendWelcomeEmail(email);
    }
}


