package edu.itstep.backendandroid.controller;

import edu.itstep.backendandroid.entity.Note;
import edu.itstep.backendandroid.respository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/notes")
    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    @GetMapping("/notes/{id}")
    public Note getById(@PathVariable int id) {
        return noteRepository.findById(id).orElse(null);
    }

    @PostMapping("/notes")
    public ResponseEntity<Note> add(@RequestBody Note note) {
        noteRepository.save(note);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @PutMapping("/notes")
    public Note update(@RequestBody Note note) {
        noteRepository.save(note);
        return note;
    }

    @DeleteMapping("/notes/{id}")
    public void delete(@PathVariable int id) {
        noteRepository.deleteById(id);
    }
}
