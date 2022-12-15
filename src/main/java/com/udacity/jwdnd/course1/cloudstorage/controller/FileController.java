package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public FileController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }


    @GetMapping("/files")
    public String getFiles(Model model) {
        updateModel(model);
        return "home";
    }

    @PostMapping("/files")
    @RequestMapping("/files")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile uploadFile, Model model) throws IOException {
        String uploadError = null;
        int userId = this.userService.getCurrentLogin().getUserId();
        if (StringUtils.isEmpty(uploadFile.getOriginalFilename())) {
            uploadError = "Please select file!";
        } else if (this.fileService.isExistFileName(uploadFile.getOriginalFilename(), userId)) {
            uploadError = "File name has already existed.";
        }

        if (StringUtils.isEmpty(uploadError)) {
            int fileAdd = this.fileService.uploadFile(uploadFile, userId);
            if (fileAdd < 0) {
                model.addAttribute("uploadError", "There was an error upload file. Please try again.");
            } else {
                model.addAttribute("uploadSuccess", "File was uploaded successfully!");
            }

        } else {
            model.addAttribute("uploadError", uploadError);
        }

        updateModel(model);
        return "home";
    }

    @GetMapping()
    @RequestMapping("/files/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") int fileId) {
        File file = this.fileService.getFile(fileId);
        String attachment = "attachment; filename=" + file.getFileName();
        return ResponseEntity.ok()
                .contentLength(Integer.valueOf(file.getFileSize()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, attachment)
                .body(file.getFileData());
    }

    @GetMapping("/files/{fileId}")
    @RequestMapping("/files/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") int fileId, Model model) {
        if (this.fileService.deleteFile(fileId)) {
            model.addAttribute("uploadSuccess", "File was deleted successfully!");
        } else {
            model.addAttribute("uploadError", "There was an error delete file. Please try again.");
        }

        updateModel(model);
        return "home";
    }

    private void updateModel(Model model) {
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
    }
}
