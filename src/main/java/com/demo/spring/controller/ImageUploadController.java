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


import org.springframework.beans.factory.annotation.Autowired;
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
	
	LSB_encode le=new LSB_encode();
	
	LSB_decode ld =new LSB_decode();
	@Autowired
	ImageService images ;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	KeyController keyController;
	public ImageModel uplaodImage( MultipartFile file,Long id) throws
	
	Exception {
		Path filePath=null;
		String aa="uploads2/"+id.toString();
		
		 Path uploadPath= Paths.get(aa);
	
		 if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	             filePath = uploadPath.resolve(file.getOriginalFilename());
	            Files.copy(file.getInputStream(), filePath);
	        }
		 KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(512);
			KeyPair pair = generator.generateKeyPair();
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();
			//String pubkey=keyController.bytesToHex(publicKey.getEncoded());
			//String prikey="30820155020100300d06092a864886f70d01010105000482013f3082013b020100024100838ea0d3c97c9293dbe8dccca7fe07533fb3bfe07e540e767bbf63537ef818fff394bb485f7262807a4c90a6cabd648e4b82ac2a7b28c5f71e6bd8eb5aa5ba09020301000102400abcc869a0c7571b54569436ebfb32d3db9e2b2a02858fcca1db041b5b809a61a3362490b8e61d0d25027ef8b24";
	String key="aaa";
	//String key="azd";
	String aaa="uploads2/"+id.toString()+"/"+file.getOriginalFilename();
	
	 
	

	
	le.aa(key,aaa,aaa);

	System.out.println("text");
	File f= new File(aaa);
    

	byte[] fileContent = Files.readAllBytes(f.toPath());
	
	System.out.println("text");
   


	ImageModel img = new ImageModel(file.getOriginalFilename(), aaa,
			
			fileContent);
	
	img.setIduser(id);

		System.out.println("text"+img.getIduser());
		ImageModel imgdb=images.save(img);
		
	    //Files.delete(uploadPath);
	
	return imgdb;
	
	}
	@GetMapping(path = { "/get/{id}" })
	public ImageModel getImage(@PathVariable("id") String id) throws Exception {
	final Optional<ImageModel> retrievedImage = imageRepository.findById(Long.parseLong(id));
	ImageModel img = new ImageModel(retrievedImage.get().getName(),
	retrievedImage.get().getType(),
	retrievedImage.get().getPicByte());
	File f =new File("uploads2/522/test.jpg");
	img.setIduser(retrievedImage.get().getIduser());
    FileInputStream input = new FileInputStream(f);

	 //InputStream stream = new ByteArrayInputStream();
	Image a=ImageIO.read(input);
	String msg="";
	
		
		String b=ld.aa(img.getType());
		
		System.out.println("aa="+b);
		
	
	return img;
	}
	
	
	
	
	
	
	

	}

