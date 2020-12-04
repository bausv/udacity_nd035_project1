package com.udacity.jwdnd.course1.cloudstorage.services;
import static org.assertj.core.api.Assertions.*;

import com.udacity.jwdnd.course1.cloudstorage.AbstractBaseTest;
import com.udacity.jwdnd.course1.cloudstorage.mapper.AbstractMapperTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EncryptionServiceTest extends AbstractBaseTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    void decryptValue() {
        String secret = "this is a secret";
        String key = encryptionService.generateSecretKey();
        String encryptValue = encryptionService.encryptValue(secret, key);
        String decryptValue = encryptionService.decryptValue(encryptValue, key);
        assertThat(decryptValue).isNotNull().isEqualTo(secret);
    }
}