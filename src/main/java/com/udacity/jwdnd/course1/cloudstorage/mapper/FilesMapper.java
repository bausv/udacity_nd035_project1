package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FilesMapper {

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    public Files getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE fileName = #{fileName} AND userId = #{userId}")
    public Optional<Files> getFileByFileName(String fileName, Integer userId);

    @Select("SELECT * FROM Files WHERE userId = #{userId}")
    public List<Files> getFilesByUserId(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(Files file);


    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    boolean deleteFileById(Integer fileId);
}
