package com.softserve.edu.controller.file.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.service.storage.impl.FileOperationImpl;
import com.softserve.edu.service.storage.impl.SaveOptions;


@Controller
@RequestMapping(value = "/uploadFile/")
public class FileUploadController {


    @Autowired
    FileOperationImpl newOperation;
    
    @Autowired
    PathGenerator pg;

    private static final String contentExtPattern = "^.*\\.(jpg|JPG|gif|GIF|doc|DOC|pdf|PDF)$";

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadFile(){
        return "/WEB-INF/views/upload.jsp";
    }
    
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody String uploadFileHandler(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file) {
        
        try {
            SaveOptions options = new SaveOptions(true);
            String fileType = file.getOriginalFilename().substring(
                    file.getOriginalFilename().lastIndexOf('.'));
            if (Pattern.matches(contentExtPattern, fileType)) {
                
                Path path = pg.getPath(fileType);
                newOperation.putResourse(file.getInputStream(), path, "hi", options);
                
                return "You successfully uploaded file=" + name;
            } else {
                return "Wrong file type";
            }
            
        } catch (Exception e) {
            return "You failed to upload " + name + " => " + e.getMessage();
        }
    }
//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    public URI uploadFileHandler(@RequestParam("name") String fileName, HttpRequest request) {
//        
//        System.out.println(newOperation.getResourseURI(Paths.get(pg.getROOT_PATH()), fileName));
//        return null;
//        
//    }
//    
//
//    @RequestMapping(value = "/img", method = RequestMethod.GET, produces = "image/jpg")
//    public @ResponseBody byte[] getFileHadler(@RequestParam("name") String name) {
//        try {
//
//            InputStream is = newOperation.getResourseStream(Paths.get(pg.getROOT_PATH()));
//
//            BufferedImage img;
//
//            img = ImageIO.read(is);
//            ByteArrayOutputStream bao = new ByteArrayOutputStream();
//            ImageIO.write(img, "jpg", bao);
//
//            return bao.toByteArray();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
}
