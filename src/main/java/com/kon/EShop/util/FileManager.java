package com.kon.EShop.util;

import com.kon.EShop.DelLabel;
import com.kon.EShop.model.MyFile;
import lombok.extern.java.Log;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Component
public class FileManager {

    @Value("${upload.path}")
    private String path;

    @Value("${upload.label}")
    private String label;

    @Value("${upload.photo}")
    private String photo;

    @Value("${main.category.height}")
    private int mcHeight;

    @Value("${main.category.width}")
    private int mcWidth;

    @Value("${catalog.photo.height}")
    private int catalogHeight;

    @Value("${catalog.photo.width}")
    private int catalogWidth;

    @Value("${upload.small}")
    private String small;

    public String upload(MultipartFile resource, String keyName, String where) throws IOException {
        Files.createDirectories(Paths.get(where));
        Path file = Files.createFile(Paths.get(where, keyName));
        String name = keyName.substring(keyName.lastIndexOf("-") + 1);
        saveLabelOfPhoto(where, resource, mcWidth, mcHeight, name);

        return file.toString();
    }

    public void delete(String key, String where) throws IOException {
        if (!key.equals("temp.jpg") && !key.equals("favicon.ico")) {
            Path path = Paths.get(this.path + (!where.equals("label") ? where.equals("csv") ? getAbsolutePath() + "static/csv/" : photo : label) + key);
            if (Files.exists(path)) Files.delete(path);
            if (where.equals("photos")) {
                path = Paths.get(this.path + small + key);
                if (Files.exists(path)) Files.delete(path);
            }
        }
    }

    public List<String> doUpload(MultipartFile[] file, String dir) throws IOException {
        List<String> uploaded = new ArrayList<>();
        for (MultipartFile fileData : file) {
            String name = fileData.getOriginalFilename();
            if (name != null && name.length() > 0) {
                if (dir.equals("label"))
                    upload(fileData, name, path + label);
                if (dir.equals("photos"))
                    waterMark("am.dn.ua", path + photo, fileData);
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
        String csv = where.equals("csv") ? getAbsolutePath() + "static/csv/" : "static/image/" + where + "/";
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

    public void delLabel(DelLabel entity) throws IOException {
        if (entity != null && entity.getLabel() != null) {
            if (!entity.getLabel().equals("temp.jpg"))
                this.delete(entity.getLabel(), "label");
        }
    }

    public void waterMark(String mark, String path, MultipartFile file) throws IOException {
        ImageIcon image = new ImageIcon(file.getBytes());
        BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image.getImage(), 0, 0, null);
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(alpha);
        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.BOLD, 60));

        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(mark, g2d);
        g2d.drawString(mark, (image.getIconWidth() - (int) rect.getWidth()) / 2, (image.getIconHeight() - (int) rect.getHeight()) / 2);
        g2d.drawString(mark, (image.getIconWidth() - (int) rect.getWidth()) / 3, (image.getIconHeight() - (int) rect.getHeight()) / 3);

        g2d.dispose();

        String name = file.getOriginalFilename();
        saveImage(bufferedImage, path + name);
        saveLabelOfPhoto(this.path + this.small, file, catalogWidth, catalogHeight, null);
    }

    public void saveLabelOfPhoto(String path, MultipartFile file, int imgWidth, int imgHeight, String name) throws IOException {
        ImageIcon image = new ImageIcon(file.getBytes());

        int width = image.getIconWidth();
        int height = image.getIconHeight();

        int scaleW, scaleH;
        int d = (int) (imgWidth * 1.0 / width * height);
        if (d <= imgHeight) {
            scaleW = imgWidth;
            scaleH = d;
        }else {
            scaleH = imgHeight;
            d = (int) (imgHeight * 1.0 / height * width +0.5);
            scaleW = d;
        }

        BufferedImage bufferedImage = resizeImage(ImageIO.read(file.getInputStream()), scaleW, scaleH);

        if (name == null) name = file.getOriginalFilename();
        saveImage(bufferedImage, path + name);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        long time = new Date().getTime();
        BufferedImage resize = Scalr.resize(originalImage, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
        log.info("Resizing image took " + (new Date().getTime() - time) + "ms");
        return resize;
    }

    private void saveImage(BufferedImage bufferedImage, String fullName) throws IOException {
        File out = new File(fullName);
        String fileExtension = fullName.substring(fullName.lastIndexOf(".") + 1);
        ImageIO.write(bufferedImage, fileExtension, out);
    }

}














