package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
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

import java.util.Objects;

@Controller
public class CredentialController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;


    public CredentialController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping("/credentials")
    public String getFiles(Model model) {
        updateModel(model);
        return "home";
    }

    @PostMapping()
    @RequestMapping("/credentials")
    public String addCredential(@ModelAttribute Credential credential, Model model) {
        int userId = this.userService.getCurrentLogin().getUserId();
        credential.setUserId(userId);
        if (Objects.nonNull(credential.getCredentialId())) {
            if (this.credentialService.updateCredential(credential)) {
                model.addAttribute("creResultSuccess", "Credential was updated successfully!");
            } else {
                model.addAttribute("creResultError", "There was an error update credential. Please try again.");
            }

        } else {
            if (this.credentialService.addCredential(credential) < 0) {
                model.addAttribute("creResultError", "There was an error add credential. Please try again.");
            } else {
                model.addAttribute("creResultSuccess", "Credential was added successfully!");
            }

        }

        updateModel(model);
        return "home";
    }

    @GetMapping("/credentials/{credentialId}")
    @RequestMapping("/credentials/{credentialId}")
    @ResponseBody
    public Credential getCredential(@PathVariable("credentialId") int credentialId) {
        return this.credentialService.getDetailCredential(credentialId);

    }

    @GetMapping("/credentials/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credentialId, Model model) {
        if (this.credentialService.deleteCredentail(credentialId)) {
            model.addAttribute("creResultSuccess", "Credential was deleted successfully!");
        } else {
            model.addAttribute("creResultError", "There was an error delete credential. Please try again.");
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
        model.addAttribute("credentialTab", "nav-item nav-link active");
        model.addAttribute("noteTab", "nav-item nav-link");

        model.addAttribute("fileTabContent", "tab-pane fade");
        model.addAttribute("credentialTabContent", "tab-pane fade  show active");
        model.addAttribute("noteTabContent", "tab-pane fade");
    }
}
