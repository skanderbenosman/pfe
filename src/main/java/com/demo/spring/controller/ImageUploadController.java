package com.demo.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
	LSB_encode enlsb =new LSB_encode();
	LSB_decode delsb =new LSB_decode();
	public void uplaodImage( MultipartFile file,Long id) throws
	
	Exception {
		String key = null;
		String bb="uploads/"+id;
		System.out.println(bb+"/"+file.getOriginalFilename());
		 Path uploadPath= Paths.get(bb);
		 if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
		 Path filePath = uploadPath.resolve(file.getOriginalFilename());
     Files.copy(file.getInputStream(), filePath);
    
   //Use SHA-1 algorithm
     
    File f=new File(bb+"/"+file.getOriginalFilename());
    KeyGenerator keygenerator;
    
	try {
		keygenerator = KeyGenerator.getInstance("AES");
		keygenerator.init(128);
		 SecretKey cle = keygenerator.generateKey();
		 key= bytesToHex(cle.getEncoded());
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    enlsb.aa(key, bb+"/"+file.getOriginalFilename(), "uploads/"+file.getOriginalFilename());
    File f2=new File("uploads/"+file.getOriginalFilename());
   
    	
		ImageModel img= new ImageModel(file.getOriginalFilename(),file.getContentType(),id,Files.readAllBytes(f2.toPath()));
		ImageModel imgdb=images.save(img);
		Path filePat2h = uploadPath.resolve(f2.getAbsolutePath());
		 Files.delete(filePath);
         Files.delete(filePat2h);
	
		
	    //Files.delete(uploadPath);
	

	
	}
	
	
	@GetMapping(path = { "/getaaa/{id}" })
	public ResponseEntity<ByteArrayResource> getImage(@PathVariable("id") long id) throws Exception {
		
	final Optional<ImageModel> retrievedImage = imageRepository.findByIduser(id);
	ImageModel img = new ImageModel(retrievedImage.get().getName(),
	retrievedImage.get().getType(),retrievedImage.get().getIduser(),
	retrievedImage.get().getPicByte());
	

	return ResponseEntity.ok().
            header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + img.getName() + "\"").
            body(new ByteArrayResource(img.getPicByte()));
	}
	
	
	
	@GetMapping(path = { "/getaaa123/{id}" })
	public String sssss(@PathVariable("id") long id) throws Exception  {
		
		final Optional<ImageModel> retrievedImage = imageRepository.findByIduser(id);
		ImageModel img = new ImageModel(retrievedImage.get().getName(),
		retrievedImage.get().getType(),retrievedImage.get().getIduser(),
		retrievedImage.get().getPicByte());
	
		Path path = Paths.get("uploads/"+img.getIduser()+"/"+img.getName());
		Path path2 = Paths.get("uploads/"+img.getIduser());
		if (!Files.exists(path2)) {
            Files.createDirectories(path2);
        }
		Files.write(path, img.getPicByte());
	
		String bb=delsb.aa("uploads/"+img.getIduser()+"/"+img.getName());
		
		Files.delete(path);
		Files.delete(path2);
		return bb;
		
	}
	  public static String bytesToHex(byte[] bytes) {
  	    StringBuilder sb = new StringBuilder();
  	    for (byte hashByte : bytes) {
  	        int intVal = 0xff & hashByte;
  	        if (intVal < 0x10) {
  	            sb.append('0');
  	        }
  	        sb.append(Integer.toHexString(intVal));
  	    }
  	    return sb.toString();
  	}
	

	}

