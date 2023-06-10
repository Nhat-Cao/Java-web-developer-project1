package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public List<Note> getNotes(){
        Integer userId = userService.getCurrentUser().getUserId();
        return noteMapper.getNotes(userId);
    }

    public int updateNote(Note note){
        if (note.getNoteId() != null){
            return noteMapper.update(new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(),note.getUserId()));
        } else {
            Integer userId = userService.getCurrentUser().getUserId();
            return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), userId));
        }
    }

    public int deleteNote(int noteId) {
        return noteMapper.delete(noteId);
    }
}
