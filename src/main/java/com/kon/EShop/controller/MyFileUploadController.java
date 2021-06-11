package com.kon.EShop.controller;

import com.kon.EShop.model.MyUploadForm;
import com.kon.EShop.util.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class MyFileUploadController {

    private final FileManager manager;

    @Value("${upload.path}")
    private String path;

    public MyFileUploadController(FileManager manager) {
        this.manager = manager;
    }


    @GetMapping("/admin/loadProductsCSV")
    public String getCSV(Model model) {
        MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);
        return "uploadMultiFile";
    }

    @DeleteMapping("/admin/file")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@RequestParam String path, @RequestParam(name = "file") String fileName) throws IOException {
        manager.delete(fileName, path);
    }

    @PostMapping("/admin/uploadMultiFile")
    @ResponseBody
    public List<String> uploadMultiFileHandlerPOST(@RequestParam String path, @RequestParam MultipartFile[] file) throws IOException {
        return manager.doUpload(file, path);
    }

}
