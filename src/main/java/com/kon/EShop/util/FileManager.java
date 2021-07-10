package com.kon.EShop.util;

import com.kon.EShop.model.MyFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileManager {

    @Value("${upload.path}")
    private String path;

    @Value("${upload.label}")
    private String label;

    @Value("${upload.photo}")
    private String photo;

    public String upload(MultipartFile resource, String keyName, String where) throws IOException {
        Files.createDirectories(Paths.get(where));
        Path file = Files.createFile(Paths.get(where, keyName));
        resource.transferTo(file);
        return file.toString();
    }

    public void delete(String key, String where) throws IOException {
        Path path = Paths.get(this.path + (where.equals("label") ?
            label :
            where.equals("csv") ?
                getAbsolutePath() + "static/csv/" :
                photo) + key);

        Files.delete(path);
    }

    public List<String> doUpload(MultipartFile[] file, String dir) throws IOException {
        List<String> uploaded = new ArrayList<>();
        for (MultipartFile fileData : file) {
            String name = fileData.getOriginalFilename();
            if (name != null && name.length() > 0) {
                upload(fileData, name, path + ((dir.equals("label")) ? label : photo));
                uploaded.add(name);
            }
        }
        return uploaded;
    }

    public static String getAbsolutePath() {
        String file = null;
        try {
            file = ResourceUtils.getURL("classpath:").toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert file != null;
        return file.replace('\\', '/');
    }

    public static String getPath(String where) {
        String csv = where.equals("csv") ? getAbsolutePath() + "static/csv/" : "static/img/" + where + "/";
        return csv.substring(6);
    }

    public MyFile getMyFile(MultipartFile file, String name, String where) {
        String dir = getPath(where);
        MyFile myFile = new MyFile();
        File output;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            output = new File(dir);
            if (!output.exists()) output.mkdirs();
            output = new File(output + File.separator + name);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output, StandardCharsets.UTF_8))) {
                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    if (count == 0){
                        myFile.setColumnNames(Arrays.stream(line.split(",")).collect(Collectors.toList()));}
                    else {
                        writer.write(line);
                        writer.newLine();
                    }
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myFile;
    }

}














