package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.AbstractBaseTest;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.exception.CredentialsExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CredentialServiceTest extends AbstractBaseTest {

    @SpyBean
    private UserService userService;

    @Autowired
    private CredentialService service;

    @Autowired
    private CredentialsMapper mapper;

    private User user;

    @BeforeEach
    public void setup() {
        user = getDefaultUser();
        log.debug("User in setup: " + user);
    }

    @Test
    void addOrEditCredential() {
        Mockito.when(userService.getUserFromAuthentication(Mockito.any())).thenReturn(Optional.of(user));
        try {
            //Authentication authentication = getAuthentication(user);
            Credentials cred = getCred();
            service.addOrEditCredential(cred, (Authentication)null);
            assertThat(mapper.getCredentialById(cred.getCredentialId())).isNotNull().returns("url", from(Credentials::getUrl)).returns("user", from(Credentials::getUsername));
            assertThatExceptionOfType(CredentialsExistException.class).isThrownBy(() -> service.addOrEditCredential(getCred(), (Authentication)null));
        } catch (CredentialsExistException e) {
            e.printStackTrace();
        }
    }

    private Credentials getCred() {
        return new Credentials(null, "url", "user", null, "pass", null);
    }


}