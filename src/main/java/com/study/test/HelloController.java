package com.study.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

  @PostMapping("/inputStream")
  public String stream() throws FileNotFoundException {

    File f = new File("C:\\aistudy\\github_download\\test99\\src\\main\\webapp\\emotion2.png"); // 읽을파일경로를 지정해주고 f 에 저장

    FileInputStream fis = new FileInputStream(f); // 파일을 읽을 입력 FileInputStream 이필요
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Encoder encoder = Base64.getEncoder();
    int size = 0; // 파일을 읽고 남은 크기를 size 에 저장한다
    byte[] buf = new byte[1024]; // 파일을 읽어들일 크기 설정 ( 좀더빠르게읽기위해 )
    try {
      while ((size = fis.read(buf)) != -1) {
        baos.write(buf, 0, size);
      }
      baos.close();
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    byte[] fileArray = baos.toByteArray();
    byte[] result = encoder.encode(fileArray);
    System.out.println("2번버튼"+new String(result));
    return "null";
  }

  @PostMapping("/file")
  public String home(HttpServletRequest request) {
    
    String[] check = {"image/png","image/jpg","image/jpeg"};
   // String x = "image/png";
    //System.out.println(Arrays.asList(check).contains(x)); //image type 을 체크할때 쓰면될것같다.
    try {
      Part filePart = request.getPart("file");
      System.out.println("이미지타입 체크 "+Arrays.asList(check).contains(filePart.getContentType()));//file 에 type 을 확인할수있다.
      InputStream fis = filePart.getInputStream();
      String realPath = request.getServletContext().getRealPath("/"); // 현재프로젝트 경로 리얼패스 추출
      System.out.println("물리경로 : " + realPath);
      String fileName = filePart.getSubmittedFileName();// 파일 이름 추출
      String filePath = realPath + File.separator + fileName;

      //String imageFileName = file.getOriginalFilename(); // 갖고온 파일 의이름 출
       
      if(Arrays.asList(check).contains(filePart.getContentType())) {
          
        byte[] imageByte = extractBytes(filePath); // 업로드된파일을 바이너리코드로 바꾼후 리턴해서 바이트배열에저장
        byte[] baseIncodingBytes = encodingBase64(imageByte); // 바이너리코드가 담긴 배열을 Base64Encoding 하여 다시 byte 배열에저장
        System.out.println("1번버튼"+new String(baseIncodingBytes));// binary 코드 를 다시 new String 문자열로 출력
      }


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
    System.out.println("이미지 현재주소 : "+imgPath);
    FileInputStream fis = null;
    // String 형으로 변환하기위해서는 byteOutputStream 에 파일을 쓰고 바이트 배열형식으로 형을 변환하여 Base64로
    // 인코딩하면된다
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    int size = 0;
    try {
      fis = new FileInputStream(imgPath);
      byte[] buf = new byte[1024];
      while ((size = fis.read(buf)) != -1) {
        // System.out.println(size);
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
