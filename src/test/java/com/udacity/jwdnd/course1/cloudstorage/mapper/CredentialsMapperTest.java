package com.udacity.jwdnd.course1.cloudstorage.mapper;

import static org.assertj.core.api.Assertions.*;

import com.udacity.jwdnd.course1.cloudstorage.AbstractBaseTest;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CredentialsMapperTest extends AbstractMapperTest {

    @Autowired
    private CredentialsMapper credentialsMapper;

    @Test
    void getCredentialById() {
        Credentials creds = getDefaultCredentials();
        int credId = credentialsMapper.insert(creds);
        Credentials credentialById = credentialsMapper.getCredentialById(creds.getCredentialId());
        assertThat(credentialById).isNotNull().usingComparator((a,b) -> a.getUsername().compareTo(b.getUsername()) + a.getPassword().compareTo(b.getPassword()) + a.getUrl().compareTo(b.getUrl())).isEqualTo(creds);
    }

    @Test
    void testCredByUrlAndUsername() {
        Credentials cred = getDefaultCredentials();
        credentialsMapper.insert(cred);
        assertThat(credentialsMapper.getCredentialByUrlAndUsername(cred.getCredentialId(), cred.getUrl(), cred.getUsername())).isEmpty();
        assertThat(credentialsMapper.getCredentialByUrlAndUsername(-1, cred.getUrl(), cred.getUsername())).isPresent().usingValueComparator((a, b) -> a.getUrl().compareTo(b.getUrl()) + a.getUsername().compareTo(b.getUsername())).contains(cred);
    }

    @Test
    void getCredsByUserId() {
        User user = getDefaultUser();
        List<Credentials> creds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Credentials cred = createCredsObjectAndStoreInDB("user" + i, "pass" + i, user.getUserId());
            creds.add(cred);
        }
        List<Credentials> credsByUserId = credentialsMapper.getCredentialsByUserId(user.getUserId());
        assertThat(credsByUserId).isNotNull().isNotEmpty().hasSize(3).usingElementComparatorOnFields("username", "password").containsExactlyElementsOf(creds);
    }

    @Test
    void insert() {
        Credentials creds = getDefaultCredentials();
        int credId = credentialsMapper.insert(creds);
        assertThat(credId).isGreaterThanOrEqualTo(0);
    }

    private Credentials createCredsObjectAndStoreInDB(String user, String pass, Integer userId) {
        Credentials credentials = getCredentials(user, pass, userId);
        credentialsMapper.insert(credentials);
        return credentials;
    }

    private Credentials getDefaultCredentials() {
        return getCredentials("user", "pass", null);
    }

    private Credentials getCredentials(String user, String pass, Integer userId) {
        Credentials creds = new Credentials(null, "http://www.foo.bar", user, "key", pass, userId);
        return creds;
    }
}