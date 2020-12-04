package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CredentialsMapper {


    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    public Credentials getCredentialById(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    public List<Credentials> getCredentialsByUserId(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credentials credential);

    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url} AND username = #{username} AND credentialId <> #{credentialId}")
    public Optional<Credentials> getCredentialByUrlAndUsername(Integer credentialId, String url, String username);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password}, key = #{key} WHERE credentialId = #{credentialId}")
    void updateCredential(Integer credentialId, String url, String username, String password, String key);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void deleteCredential(Integer credentialId);
}
