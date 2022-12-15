package com.udacity.jwdnd.course1.cloudstorage.mapper;

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
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getNotesByUserId(int userId);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNote(int noteid);

    @Insert("INSERT INTO NOTES (noteId, noteTitle, noteDescription, userId) VALUES(#{noteId}, #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int addNote(Note note);

    @Update("UPDATE NOTES SET noteId = #{noteId}, noteTitle = #{noteTitle}, noteDescription = #{noteDescription}, userId = #{userId} WHERE noteId = #{noteId}")
    boolean update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    boolean deleteByNoteId(int noteId);
}
