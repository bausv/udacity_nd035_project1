package com.udacity.jwdnd.course1.cloudstorage.mapper;

import static org.assertj.core.api.Assertions.*;

import com.udacity.jwdnd.course1.cloudstorage.AbstractBaseTest;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NotesMapperTest extends AbstractMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotesMapper notesMapper;

    @Test
    void getNotesByUserId() {
        User user = new User(null, "username", "salt", "pass", "first", "last");
        int userId = userMapper.insert(user);
        user = userMapper.getUser(user.getUsername());
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isNotNull();
        List<Notes> notes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Notes note = getNote("title" + i, "desc" + i, user.getUserId());
            notesMapper.insert(note);
            notes.add(note);
        }
        List<Notes> notesByUserId = notesMapper.getNotesByUserId(user.getUserId());
        assertThat(notesByUserId).isNotNull().isNotEmpty().hasSize(notes.size()).usingElementComparatorOnFields("noteTitle", "noteDescription").containsAll(notes);

    }

    @Test
    void getNotesById() {
        Notes note = getNote();
        int id = notesMapper.insert(note);
        Notes notesById = notesMapper.getNotesById(note.getNoteId());
        assertThat(notesById).isNotNull().hasFieldOrPropertyWithValue("noteTitle", note.getNoteTitle()).hasFieldOrPropertyWithValue("noteDescription", note.getNoteDescription());
    }

    @Test
    void insert() {
        Notes note = getNote();
        assertThat(notesMapper).returns(1, from(m -> m.insert(note)));
    }

    private Notes getNote() {
        Notes note = getNote("title1", "description1", null);
        return note;
    }

    private Notes getNote(String noteTitle, String noteDescription, Integer userId) {
        Notes note = new Notes(null, noteTitle, noteDescription, userId);
        return note;
    }
}