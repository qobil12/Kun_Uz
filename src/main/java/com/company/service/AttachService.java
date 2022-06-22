package com.company.service;

import com.company.entity.ArticleEntity;
import com.company.entity.AttachEntity;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

// PROJECT NAME Kun_Uz
// TIME 17:06
// MONTH 06
// DAY 20
@Service
public class AttachService {

    @Autowired
    private AttachRepository attachRepository;

    private final String attachURL = "attaches/";

    public String saveToSystem(MultipartFile file) {
        try {
            // zari.jpg
            // asdas-dasdas-dasdasd-adadsd.jpg
            AttachEntity attachEntity = new AttachEntity();
            String pathFolder = getYmDString(); //2022/06/20
            //String uuid = UUID.randomUUID().toString(); //asdas-dasdas-dasdasd-adadsd
            String extension = getExtension(file.getOriginalFilename()); //jpg


            File folder = new File(attachURL + pathFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            byte[] bytes = file.getBytes();
            attachEntity.setOriginName(file.getOriginalFilename());
            attachEntity.setPath(String.valueOf(folder));
            attachEntity.setType(extension);
            attachRepository.save(attachEntity);
            String fileName = attachEntity.getUuid() + "." + extension; //asdas-dasdas-dasdasd-adadsd.jpg
            Path path = Paths.get(attachURL + pathFolder + "/" + fileName);
            Files.write(path, bytes);

            return getPathFolder(pathFolder) + "_" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public byte[] loadImage(String fileName) {
//        byte[] imageInByte;
//fileName = getPathFolderFromUrl(fileName);
//        BufferedImage originalImage;
//        try {
//            originalImage = ImageIO.read(new File("attaches/" + fileName));
//        } catch (Exception e) {
//            return new byte[0];
//        }
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(originalImage, "png", baos);
//            baos.flush();
//            imageInByte = baos.toByteArray();
//            baos.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return imageInByte;
//    }

    public byte[] open_general(String fileName) {
        byte[] data;
        try {
            Optional<AttachEntity> attach = attachRepository.findById(fileName);
            if (attach.isEmpty()) {
                throw new ItemNotFoundEseption("Attach not found mazgiii");
            }

            AttachEntity attachEntity = attach.get();
            // fileName -> zari.jpg

            String path = attachEntity.getPath() + "/" + attachEntity.getUuid() + "." + attachEntity.getType();
            Path file = Paths.get(path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public Resource download(String fileName) {

        try {
            Optional<AttachEntity> byId = attachRepository.findById(fileName);
            if (byId.isEmpty()) {
                throw new ItemNotFoundEseption("Attach not found mazgiii");
            }
            AttachEntity entity = byId.get();
            Path file = Paths.get(entity.getPath() + "/" + entity.getUuid() + "." + entity.getType());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public String getPathFolder(String fileName) {
        String[] arr = fileName.split("/");

        return arr[0] + "_" + arr[1] + "_" + arr[2];

    }

    public String delete(String id) {
        Optional<AttachEntity> byId = attachRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ItemNotFoundEseption("Mazgimisan bunaqa attach yo'q");
        }
        AttachEntity attachEntity = byId.get();

        try {
            Files.delete(Path.of(attachEntity.getPath() + "/" + attachEntity.getUuid() + "." + attachEntity.getType()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        attachRepository.deleteById(attachEntity.getUuid());
        return "succesfully deleted";
    }

//    public String getPathFolderFromUrl(String fileName) {
//        String[] arr = fileName.split("_");
//
//        return arr[0]+"/"+arr[1]+"/"+arr[2]+"/"+arr[3];
//
//    }

}
