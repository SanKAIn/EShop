package com.kon.EShop.controller;

import com.kon.EShop.util.FileManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class MyFileUploadController {

    private final FileManager manager;

    public MyFileUploadController(FileManager manager) {
        this.manager = manager;
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
