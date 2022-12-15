package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getFilesByUserId(int userid);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFile(int fileId);

    @Insert("INSERT INTO FILES (fileId, fileName, contentType, fileSize, userId, fileData) VALUES(#{fileId}, #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileid}")
    boolean deleteByFileId(int fileid);
}
