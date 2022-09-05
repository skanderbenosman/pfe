package com.demo.spring.controller;


import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.demo.spring.ResponseMessage;
import com.demo.spring.domain.Response;
import com.demo.spring.model.FileInfo;

import com.demo.spring.service.FilesStorageServiceImpl;




@RestController
public class FilesController {
  @Autowired
  FilesStorageServiceImpl storageService;
  @PostMapping("/upload")
  public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam(value = "param1", required = false) String param1) {
	    String message = "";
	    
	    try {
	    	System.out.println("test="+param1);
	      String msg=storageService.save(file,param1);
	     
	      if(msg.equals("file added")){
	    	  message = "Uploaded the file successfully ";
	      return ResponseEntity.status(HttpStatus.OK).body(new Response(message));}else
	      { message = "Uploaded the file failed ";
	    	  return ResponseEntity.status(HttpStatus.OK).body(new Response(message));
	      }
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(message));
	    }
	  }
    @GetMapping(value="/getuserfile")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<FileInfo>> getUserFile(){
    	
	  List<FileInfo> lf= storageService.findFileUser();
		return new ResponseEntity<List<FileInfo>>(lf,HttpStatus.OK);
		
	}
    @PostMapping("/download")
    public ResponseEntity<ResponseMessage> downloadFile(@RequestParam ("id") Long id) {
  	    String message = "";
  	    
  	    try {
  	    	
  	      storageService.Download(id);
  	      message = "Uploaded the file successfully";
  	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
  	    } catch (Exception e) {
  	      message = "Could not upload the file !";
  	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
  	    }
  	  }
    @PostMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteFile(@RequestParam ("id") Long id) {
  	    String message = "";
  	    
  	    try {
  	    	
  	      storageService.deletefile(id);
  	      message = "delete the file successfully";
  	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
  	    } catch (Exception e) {
  	      message = "Could not delete the file !";
  	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
  	    }
  	  }
    @PostMapping("/delete2")
    public ResponseEntity<ResponseMessage> deleteFile2(@RequestParam ("id") Long id) {
  	    String message = "";
  	    
  	    try {
  	    	
  	      storageService.deletefile2(id);
  	      message = "delete the file successfully";
  	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
  	    } catch (Exception e) {
  	      message = "Could not delete the file !";
  	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
  	    }
  	  }
    
      
  
}
