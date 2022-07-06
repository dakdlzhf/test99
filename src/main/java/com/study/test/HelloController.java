package com.study.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HelloController {

  @PostMapping("/file")
  public String home(HttpServletRequest request, MultipartFile file) {
    try {
      Part filePart = request.getPart("file");
      InputStream fis = filePart.getInputStream();
      String realPath = request.getServletContext().getRealPath("/"); //현재프로젝트 경로 리얼패스 추출
      System.out.println("물리경로 : " + realPath); 
      String fileName = filePart.getSubmittedFileName();//파일 이름 추출
      String filePath = realPath + File.separator + fileName;

      String imageFileName = file.getOriginalFilename();
      byte[] imageByte = extractBytes(filePath);
      byte[] baseIncodingBytes = encodingBase64(imageByte);
      
      System.out.println(new String(baseIncodingBytes));
      
      FileOutputStream fos = new FileOutputStream(filePath);
      int size = 0;
      byte[] buf = new byte[1024];
      while ((size = fis.read(buf)) != -1) {
        fos.write(buf, 0, size);
      }
      fos.close();
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServletException e) {
      e.printStackTrace();
    }

    return "/hello";
  }

  private byte[] extractBytes(String imageName) throws IOException {
    File imgPath = new File(imageName);
    FileInputStream fis = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    int size = 0;
    try {
      fis = new FileInputStream(imgPath);
      byte[] buf = new byte[1024];
      while ((size = fis.read(buf)) != -1) {
        baos.write(buf, 0, size);
      }
      baos.close();
      fis.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    byte[] fileArray = baos.toByteArray();
    return fileArray;
  }

  private byte[] encodingBase64(byte[] imageByte) {
    Encoder encoder = Base64.getEncoder();
    
    return encoder.encode(imageByte);
  }

  @GetMapping("/hello")
  public String hello() {

    return "hello";
  }
}
