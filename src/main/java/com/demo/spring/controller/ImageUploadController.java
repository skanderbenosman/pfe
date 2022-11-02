package com.demo.spring.controller;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;



import javax.imageio.ImageIO;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.spring.model.ImageModel;
import com.demo.spring.repository.ImageRepository;
import com.demo.spring.service.ImageService;






@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
@RestController
public class ImageUploadController {
	public static String b_msg="";
	public static int len = 0;
	@Autowired
	ImageService images ;
	@Autowired
	ImageRepository imageRepository;

	public void uplaodImage( MultipartFile file,Long id) throws
	
	Exception {
		
		ImageModel img= new ImageModel(file.getOriginalFilename(),file.getContentType(),id,file.getBytes());
		ImageModel imgdb=images.save(img);
		Path filePath=null;
		String aa="src/main/resources/static/"+id.toString();
		
		 Path uploadPath= Paths.get(aa);
	
		 if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	             filePath = uploadPath.resolve(file.getOriginalFilename());
	            Files.copy(file.getInputStream(), filePath);
	        }
		
	String aaa="uploads2/"+id.toString()+"/"+file.getOriginalFilename();
	
	 
	



	System.out.println("text");
	File f= new File(aaa);
    

	
		
	    //Files.delete(uploadPath);
	

	
	}
	
	
	@GetMapping(path = { "/getaaa" })
	public ResponseEntity<ByteArrayResource> getImage() throws Exception {
		Long a=(long) 1;
	final Optional<ImageModel> retrievedImage = imageRepository.findById(a);
	ImageModel img = new ImageModel(retrievedImage.get().getName(),
	retrievedImage.get().getType(),retrievedImage.get().getIduser(),
	retrievedImage.get().getPicByte());
	
		
	
	return ResponseEntity.ok().
            header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + img.getName() + "\"").
            body(new ByteArrayResource(img.getPicByte()));
	}
	
	
	
	
	
	

	}

