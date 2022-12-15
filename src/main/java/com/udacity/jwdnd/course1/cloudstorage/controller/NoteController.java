package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;


@Controller
public class NoteController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;

    public NoteController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping("/notes")
    public String getFiles(Model model) {
        updateModel(model);
        return "home";
    }

    @PostMapping()
    @RequestMapping("/notes")
    public String addNote(@ModelAttribute Note note, Model model) {
        int userId = this.userService.getCurrentLogin().getUserId();
        note.setUserId(userId);
        if (Objects.nonNull(note.getNoteId())) {
            if (this.noteService.updateNote(note)) {
                model.addAttribute("resultSuccess", "Note was updated successfully.");
            } else {
                model.addAttribute("resultError", "There was an error update note. Please try again.");
            }

        } else {
            if (this.noteService.addNote(note) < 0) {
                model.addAttribute("resultError", "There was an error add note. Please try again.");
            } else {
                model.addAttribute("resultSuccess", "Note was added successfully.");
            }
        }

        updateModel(model);

        return "home";
    }


    @GetMapping("/notes/{noteId}")
    @ResponseBody
    public Note getNote(@PathVariable("noteId") int noteId) {
        return this.noteService.getDetailNote(noteId);
    }

    @GetMapping()
    @RequestMapping("/notes/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") int noteId, Model model) {
        if (this.noteService.deleteNote(noteId)) {
            model.addAttribute("resultSuccess", "Note was deleted successfully.");
        } else {
            model.addAttribute("resultError", "There was an error delete note. Please try again.");
        }

        updateModel(model);
        return "home";
    }

    private void updateModel(Model model) {
        int userId = this.userService.getCurrentLogin().getUserId();
        model.addAttribute("uploadedFiles", this.fileService.getFiles(userId));
        model.addAttribute("notes", this.noteService.getNotes(userId));
        model.addAttribute("credentials", this.credentialService.getCredentials(userId));

        model.addAttribute("fileTab", "nav-item nav-link");
        model.addAttribute("credentialTab", "nav-item nav-link");
        model.addAttribute("noteTab", "nav-item nav-link active");

        model.addAttribute("fileTabContent", "tab-pane fade");
        model.addAttribute("credentialTabContent", "tab-pane fade");
        model.addAttribute("noteTabContent", "tab-pane fade show active");
    }
}
