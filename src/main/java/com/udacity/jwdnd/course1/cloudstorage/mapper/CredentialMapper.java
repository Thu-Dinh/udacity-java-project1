package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credential> getCredentialsByUserId(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credential getCredential(int credentialId);

    @Insert("INSERT INTO CREDENTIALS (credentialId, url, username, key, password, userId) VALUES(#{credentialId}, #{url}, #{username}, #{key} , #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int addCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET credentialId = #{credentialId}, url = #{url}, username = #{username}, key = #{key}, password = #{password}, userId = #{userId} WHERE credentialId = #{credentialId}")
    boolean updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    boolean deleteById(int credentialId);
}
