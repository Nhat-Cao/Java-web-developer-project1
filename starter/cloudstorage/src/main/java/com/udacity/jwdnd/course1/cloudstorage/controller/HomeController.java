package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

@Controller()
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final FileService fileService;

    public HomeController(NoteService noteService, CredentialService credentialService, EncryptionService encryptionService, FileService fileService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping()
    public String homeView(Model model) {

        model.addAttribute("notes", this.noteService.getNotes());
        model.addAttribute("credentials", this.credentialService.getCredentials());
        model.addAttribute("files", this.fileService.getFiles());
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    @PostMapping("/note/update")
    public String updateNote(@ModelAttribute Note note, Model model){

        int rowsUpdated = noteService.updateNote(note);

        String error = null;
        if (rowsUpdated < 0) {
            error = "There was an error. Please try again.";
        }
        if (error == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", error);
        }

        return "result";
    }
    @GetMapping("/note/delete/{id}")
    // Method
    public String deleteNote(@PathVariable("id") int noteId, Model model) {
        int rowsUpdated = noteService.deleteNote(noteId);

        String error = null;
        if (rowsUpdated < 0) {
            error = "There was an error. Please try again.";
        }
        if (error == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", error);
        }

        return "result";
    }


    @PostMapping("/credential/update")
    public String updateCredential(@ModelAttribute Credential credential, Model model){

        int rowsUpdated = credentialService.updateCredential(credential);

        String error = null;
        if (rowsUpdated < 0) {
            error = "There was an error. Please try again.";
        }
        if (error == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", error);
        }

        return "result";
    }
    @GetMapping("/credential/delete/{id}")
    // Method
    public String deleteCredential(@PathVariable("id") int credentialId, Model model) {
        int rowsUpdated = credentialService.deleteCredential(credentialId);

        String error = null;
        if (rowsUpdated < 0) {
            error = "There was an error. Please try again.";
        }
        if (error == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", error);
        }

        return "result";
    }

    @PostMapping("/file/upload")
    // Method
    public String uploadFile(@ModelAttribute MultipartFile fileUpload, Model model) throws IOException, SQLException {
        int rowsUpdated = fileService.uploadFile(fileUpload);

        String error = null;
        if (rowsUpdated < 0) {
            error = "The file name is existing. Please try another file or name.";
        }
        if (error == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", error);
        }

        return "result";
    }

    @GetMapping("/file/download/{id}")
    // Method
    public HttpEntity<byte[]> downloadFile(@PathVariable("id") int fileId, HttpServletResponse response) throws IOException {
            File file = fileService.getFile(fileId);

            byte[] documentBody = file.getFileData();

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.valueOf(file.getContentType()));
            header.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=" + file.getFileName());
            header.setContentLength(documentBody.length);

            return new HttpEntity<byte[]>(documentBody, header);
    }

    @GetMapping("/file/delete/{id}")
    // Method
    public String deleteFile(@PathVariable("id") int fileId, Model model) {
        int rowsUpdated = fileService.deleteFile(fileId);

        String error = null;
        if (rowsUpdated < 0) {
            error = "There was an error. Please try again.";
        }
        if (error == null) {
            model.addAttribute("changeSuccess", true);
        } else {
            model.addAttribute("changeError", error);
        }

        return "result";
    }
}
