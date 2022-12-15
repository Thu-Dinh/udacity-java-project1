package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public Note getDetailNote(int noteId) {
        return noteMapper.getNote(noteId);
    }

    public int addNote(Note note) {
        return noteMapper.addNote(note);
    }

    public boolean deleteNote(int noteId) {
        return noteMapper.deleteByNoteId(noteId);
    }

    public boolean updateNote(Note note) {
        return noteMapper.update(note);
    }
}
