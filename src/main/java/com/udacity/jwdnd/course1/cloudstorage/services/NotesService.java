package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private final NotesMapper notesMapper;
    private final UserService userService;

    public NotesService(NotesMapper notesMapper, UserService userService) {
        this.notesMapper = notesMapper;
        this.userService = userService;
    }

    public void addNewNote(Authentication authentication, Notes note) {
        User user = userService.getUserFromAuthentication(authentication).orElseThrow();
        addNewNote(user, note);
    }

    public void addNewNote(User user, Notes note) {
        note.setUserId(user.getUserId());
        notesMapper.insert(note);
    }

    public List<Notes> getNotesForUser(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication).orElseThrow();
        return notesMapper.getNotesByUserId(user.getUserId());
    }

    public void deleteNote(Integer noteId) {
        notesMapper.deleteNoteById(noteId);
    }

    public void updateNote(Notes note) {
        notesMapper.updateNote(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription());
    }
}
