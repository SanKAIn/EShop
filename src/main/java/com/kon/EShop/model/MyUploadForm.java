package com.kon.EShop.model;

import org.springframework.web.multipart.MultipartFile;

public class MyUploadForm {

    private MultipartFile[] fileDataS;

    public MultipartFile[] getFileDataS() {
        return fileDataS;
    }

    public void setFileDataS(MultipartFile[] fileDataS) {
        this.fileDataS = fileDataS;
    }

}
