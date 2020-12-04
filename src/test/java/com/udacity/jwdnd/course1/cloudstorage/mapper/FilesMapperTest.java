package com.udacity.jwdnd.course1.cloudstorage.mapper;

import static org.assertj.core.api.Assertions.*;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilesMapperTest extends AbstractMapperTest {

    public static final String FILE_NAME = "name";
    public static final String CONTENT_TYPE = "contenType";
    public static final String FILE_SIZE = "12345";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FilesMapper filesMapper;

    @Test
    void getFileById() throws IOException {
        Files fileId = createFilesObjectAndStoreInDB();
        Files fileById = filesMapper.getFileById(fileId.getFileId());
        Files example = new Files(null, FILE_NAME, CONTENT_TYPE, FILE_SIZE, null, null);
        assertThat(fileById).isNotNull().usingComparator((a, b) -> a.getFileName().compareTo(b.getFileName()) + a.getFileSize().compareTo(b.getFileSize())).isEqualTo(example);
    }

    @Test
    void getFilesByUserId() throws IOException {
        User user = getDefaultUser();
        log.debug("User: " + user);
        List<Files> files = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Files fileId = createFilesObjectAndStoreInDB("Test-content" + i, user.getUserId());
        }
        List<Files> filesByUserId = filesMapper.getFilesByUserId(user.getUserId());
        assertThat(filesByUserId).isNotNull().isNotEmpty().hasSize(3);
    }

    @Test
    void insert() throws IOException {
        Files fileId = createFilesObjectAndStoreInDB();
        assertThat(fileId.getFileId()).isNotNull();
    }

    private Files createFilesObjectAndStoreInDB() throws IOException {
        return createFilesObjectAndStoreInDB("This is a test", null);
    }

    private Files createFilesObjectAndStoreInDB(String fileContent, Integer userId) throws IOException {
        File tempFile = getFileWithContent(fileContent);
        byte[] bytes = FileUtils.readFileToByteArray(tempFile);
        Files file = new Files(null, FILE_NAME, CONTENT_TYPE, FILE_SIZE, userId, bytes);
        int fileId = filesMapper.insert(file);
        return file;
    }

    private File getFileWithContent(String fileContent) throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent);
        }
        return tempFile;
    }
}