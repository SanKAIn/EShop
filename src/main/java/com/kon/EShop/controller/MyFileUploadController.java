package com.kon.EShop.controller;

import com.kon.EShop.model.MyUploadForm;
import com.kon.EShop.util.FileManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MyFileUploadController {

    private FileManager manager;

    public MyFileUploadController(FileManager manager) {
        this.manager = manager;
    }


    @GetMapping("/admin/loadProductsCSV")
    public String getCSV(Model model) {
        MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);
        return "uploadMultiFile";
    }

    @GetMapping("/uploadMultiFile")
    public String uploadMultiFileHandler(Model model) {
        MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);
        return "uploadMultiFile";
    }

    @PostMapping("/uploadMultiFile")
    public void uploadMultiFileHandlerPOST(@ModelAttribute("myUploadForm") MyUploadForm myUploadForm) {
        this.doUpload(myUploadForm);

    }

    private void doUpload(MyUploadForm myUploadForm) {

        MultipartFile[] fileDataS = myUploadForm.getFileDataS();
        List<String> uploaded = new ArrayList<>();

        for (MultipartFile fileData : fileDataS) {
            String name = fileData.getOriginalFilename();
            if (name != null && name.length() > 0) {
                try {
                    uploaded.add(manager.upload(fileData, name, "big"));
                } catch (Exception e) {
                    System.out.println("error");
                }
            }
        }
    }

}
