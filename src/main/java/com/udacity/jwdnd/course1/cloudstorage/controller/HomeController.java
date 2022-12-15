package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping()
    @RequestMapping("/")
    public String getHomeView(Model model) {
        int userId = this.userService.getCurrentLogin().getUserId();
        model.addAttribute("fileTab", "nav-item nav-link active");
        model.addAttribute("credentialTab", "nav-item nav-link");
        model.addAttribute("noteTab", "nav-item nav-link");

        model.addAttribute("fileTabContent", "tab-pane fade show active");
        model.addAttribute("credentialTabContent", "tab-pane fade");
        model.addAttribute("noteTabContent", "tab-pane fade");

        model.addAttribute("uploadedFiles", this.fileService.getFiles(userId));
        model.addAttribute("notes", this.noteService.getNotes(userId));
        model.addAttribute("credentials", this.credentialService.getCredentials(userId));
        return "home";
    }
}
