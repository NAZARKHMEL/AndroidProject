package edu.itstep.backendandroid.respository;

import edu.itstep.backendandroid.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}
