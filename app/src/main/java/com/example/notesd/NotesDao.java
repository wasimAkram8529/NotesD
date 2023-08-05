package com.example.notesd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes WHERE id = :noteId")
    Notes getNoteById(long noteId);

    @Query("select * from notes")
    List<Notes> getAllInstance();

    @Insert
    void addNotes(Notes notes);

    @Update
    void updateNotes(Notes notes);

    @Delete
    void deleteNotes(Notes notes);
}

