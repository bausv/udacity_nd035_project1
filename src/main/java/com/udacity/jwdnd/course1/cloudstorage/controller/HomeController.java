package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.exception.CredentialsExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private UserService userService;
    private FileService fileService;
    private NotesService notesService;
    private CredentialService credentialService;

    @Autowired
    public HomeController(UserService userService, FileService fileService, NotesService notesService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.notesService = notesService;
        this.credentialService = credentialService;
    }

    @RequestMapping("/home")
    public String getHomePage(Authentication authentication, Model model, @PathVariable(name = "credResponse", required = false) String credResponse, @PathVariable(name = "noteResponse", required = false) String noteResponse, @PathVariable(name = "fileResponse", required = false) String fileResponse) {
        log.debug("getHomePage(" + authentication + ", " + model + ")");
        try {
            model.addAttribute("files", fileService.getFilesForUser(authentication));
            model.addAttribute("notes", notesService.getNotesForUser(authentication));
            model.addAttribute("creds", credentialService.getCredentialsForUser(authentication));
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("globalError", e.getMessage());
        }
        if (credResponse != null) {
            model.addAttribute("credResponse", credResponse);
        }
        if (noteResponse != null) {
            model.addAttribute("noteResponse", noteResponse);
        }
        return "home";
    }

    @PostMapping("/addOrEditCred")
    public String addOrEditCredential(Authentication authentication, @ModelAttribute Credentials cred, Model model) {
        log.debug("addOrEditCredential(" + authentication + ", " + cred + ", " + model + ")");
        try {
            model.addAttribute("credResponse", credentialService.addOrEditCredential(cred, authentication));
        } catch (CredentialsExistException e) {
            log.error(e.getMessage());
            model.addAttribute("credResponse", e.getMessage());
        }
        return "redirect:/home?credResponse=" + model.getAttribute("credResponse");
    }

    @RequestMapping("/deleteCred/{credId:.+}")
    public String deleteCredential(Authentication authentication,
                             @PathVariable("credId") Integer credId, Model model) {
        log.debug("deleteCredential(" + authentication + ", " + credId + ", " + model + ")");
        try {
            credentialService.deleteCredential(credId);
        } catch (Exception e) {
            model.addAttribute("globalError", e.getMessage());
            return "home";
        }
        return "redirect:/home?credResponse=Credentials deleted";
    }

    @PostMapping("/newNote")
    public String addNewNote(Authentication authentication, @ModelAttribute Notes note, Model model) {
        log.debug("addNewNote");
        String response = null;
        if (note.getNoteId() == null) {
            notesService.addNewNote(authentication, note);
            response = "Note with title '" + note.getNoteTitle() + "' created";
        }
        else {
            notesService.updateNote(note);
            response = "Note with title '" + note.getNoteTitle() + "' updated";
        }
        return "redirect:/home?noteResponse=" + response;
    }

    @RequestMapping("/deleteNote/{noteId:.+}")
    public String deleteNote(Authentication authentication,
                             @PathVariable("noteId") Integer noteId, Model model) {
        log.debug("deleteNote");
        notesService.deleteNote(noteId);
        model.addAttribute("notes", notesService.getNotesForUser(authentication));
        model.addAttribute("responseMessage", "Note with id '" + noteId + "' deleted");
        return "redirect:/home?noteResponse=Note deleted.";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, Model model) {
        String fileResponse = fileService.saveFile(authentication, file);
        model.addAttribute("fileResponse", fileResponse);
        model.addAttribute("files", fileService.getFilesForUser(authentication));
        return "redirect:/home?fileResponse=" + fileResponse;
    }

    @RequestMapping("/download/{fileId:.+}")
    public void downloadPDFResource(Authentication authentication,
                                    HttpServletResponse response, @PathVariable("fileId") Integer fileId) {
        Files file1 = fileService.getFileById(fileId);
        response.setContentType(file1.getContentType());
        response.addHeader("Content-Disposition", "attachment; filename=" + file1.getFileName());
        try {
            response.getOutputStream().write(file1.getFileData());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/delete/{fileId:.+}")
    public String deleteFile(Authentication authentication,
                                    @PathVariable("fileId") Integer fileId, Model model) {
        fileService.deleteFile(fileId);
        model.addAttribute("files", fileService.getFilesForUser(authentication));
        return "redirect:/home";
    }

}
