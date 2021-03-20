package com.kon.EShop.util;

import com.kon.EShop.model.MyFile;
import org.jetbrains.annotations.NotNull;
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

    public String upload(MultipartFile resource, String keyName, String where) throws IOException {
        String paz = getPath(where);
        Files.createDirectories(Files.createDirectories(Paths.get(paz)));
        Path file = Files.createFile(Paths.get(paz, keyName));
        resource.transferTo(file);
        return file.toString();
    }

    public void delete(String key, String where){
        String dir = getPath(where);
        Path path = Paths.get(dir + key);
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println("не получилось удалить фаил");
            e.printStackTrace();
        }
    }

    public static String getPath(String where) {
        String file1 = null;
        try {
            file1 = ResourceUtils.getURL("classpath:").toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert file1 != null;
        String absolutePath = file1.replace('\\', '/');
        String csv = where.equals("csv") ? absolutePath + "static/csv/" : "static/img/" + where + "/";
        return csv.substring(6);
    }

    public MyFile getMyFile(MultipartFile file,  String name, String where) {
        String dir = getPath(where);
        MyFile myFile = new MyFile();
        File output;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            output = new File(dir);
            if (!output.exists()) output.mkdirs();
            output = new File(output.toString() + File.separator + name);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output, StandardCharsets.UTF_8))) {
                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    line = deleteChar(line);
                    if (count == 0){
                        myFile.setTableName(line);}
                    else if (count == 1){
                        myFile.setColumnNames(Arrays.stream(line.split(";")).collect(Collectors.toList()));}
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

    @NotNull
    private String deleteChar(String line) {
        byte[] inBytes = line.getBytes();
        List<Byte> list = new ArrayList<>();
        for (byte inByte : inBytes) {
            if (inByte != -17 && inByte != -65 && inByte != -69)
                list.add(inByte);
        }
        byte[] outBytes = new byte[list.size()];
        int j = 0;
        for (Byte b : list)
            outBytes[j++] = b;
        line = new String(outBytes, StandardCharsets.UTF_8);
        return line;
    }

}














