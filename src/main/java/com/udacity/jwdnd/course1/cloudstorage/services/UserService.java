package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private HashService hashService;
    private UserMapper userMapper;
    private EncryptionService encryptionService;

    @Autowired
    public UserService(HashService hashService, UserMapper userMapper, EncryptionService encryptionService) {
        this.hashService = hashService;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    public Optional<User> getUserFromAuthentication(Authentication authentication) {
        if (authentication == null)
            return Optional.empty();
        String userName = authentication.getName();
        User user = getUser(userName);
        return Optional.of(user);
    }

    public User createUser(User user) {
        String encodedSalt = encryptionService.generateSecretKey();
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        userMapper.insert(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
        return getUser(user.getUsername());
    }

    public User createAndGetUser(User user) {
        createUser(user);
        return getUser(user.getUsername());
    }
}
