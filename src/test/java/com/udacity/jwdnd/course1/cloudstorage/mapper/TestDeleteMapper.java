package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDeleteMapper {

    @Delete("DELETE FROM USERS")
    int deleteAllUsers();

    @Delete("DELETE FROM FILES")
    int deleteAllFiles();

    @Delete("DELETE FROM NOTES")
    int deleteAllNotes();

    @Delete("DELETE FROM CREDENTIALS")
    int deleteAllCredentials();
}
