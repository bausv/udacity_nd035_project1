package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.exception.CredentialsExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private static final Logger log = LoggerFactory.getLogger(CredentialService.class);
    public static final String CREDENTIAL_ADDED = "Credential added.";
    public static final String CREDENTIAL_MODIFIED = "Credential modified.";

    private CredentialsMapper credentialsMapper;
    private UserService userService;
    private EncryptionService encryptionService;

    public CredentialService(CredentialsMapper credentialsMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    public String addOrEditCredential(Credentials cred, Authentication authentication) throws CredentialsExistException {
            String responseMessage = null;
        Optional<User> user = userService.getUserFromAuthentication(authentication);
        if (user.isPresent()) {
            return addOrEditCredential(cred, user.get());
        } else {
            responseMessage = "User '" + authentication.getPrincipal() + "' not in database!";
        }
        return responseMessage;
    }

    public String addOrEditCredential(Credentials cred, User user) throws CredentialsExistException {
        String responseMessage;
        log.debug("addOrEditCredential(" + cred + ")");
        Optional<Credentials> optionalCredentials = credentialsMapper.getCredentialByUrlAndUsername(cred.getCredentialId() == null ? -1 : cred.getCredentialId(), cred.getUrl(), cred.getUsername());
        if (optionalCredentials.isPresent()) {
            throw new CredentialsExistException(cred.getUrl(), cred.getUsername());
        }
        try {
            if (cred.getKey() == null) {
                cred.setKey(encryptionService.generateSecretKey());
            }
            cred.setPassword(encryptionService.encryptValue(cred.getPassword(), cred.getKey()));
            if (cred.getCredentialId() == null) {
                cred.setUserId(user.getUserId());
                credentialsMapper.insert(cred);
                responseMessage = CREDENTIAL_ADDED;
            } else {
                credentialsMapper.updateCredential(cred.getCredentialId(), cred.getUrl(), cred.getUsername(), cred.getPassword(), cred.getKey());
                responseMessage = CREDENTIAL_MODIFIED;
            }
        } catch (Exception e) {
            log.error("error editing creds: ", e);
            responseMessage = e.getMessage();
        }
        log.debug("returning " + responseMessage);
        return responseMessage;
    }

    public List<Credentials> getCredentialsForUser(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication).orElseThrow();
        return getCredentialsForUser(user);
    }

    public List<Credentials> getCredentialsForUser(User user) {
        return credentialsMapper.getCredentialsByUserId(user.getUserId()).stream().map(c -> decryptPassword(c)).collect(Collectors.toList());
    }

    public void deleteCredential(Integer cred) {
        credentialsMapper.deleteCredential(cred);
    }

    private Credentials decryptPassword(Credentials cred) {
        log.debug("decrypting password for " + cred);
        String decryptValue = encryptionService.decryptValue(cred.getPassword(), cred.getKey());
        cred.setKey(decryptValue);
        return cred;
    }

}
