package com.demo.spring.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.spring.model.FileInfo;
import com.demo.spring.model.User;
import com.demo.spring.repository.FileInfoRepository;
import com.demo.spring.repository.UserRepository;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
@Service
public class FilesStorageServiceImpl {
  private final Path root = Paths.get("uploads");
  @Autowired
  private FileInfoRepository fileInfoRepository;
  @Autowired
  private UserRepository userRepository;

  public void init() {
	    try {
	      Files.createDirectory(root);
	    } catch (IOException e) {
	      throw new RuntimeException("Could not initialize folder for upload!");
	    }
	  }
public String save(MultipartFile file,String algo) {
	Long idfile = null;
	try {
	  
	   

		 
			
		  
			 	
		        String sftpPath = "/sftp_user";
		        String sftpHost = "20.199.45.64";
		        String sftpPort = "22";
		        String sftpUser = "sftp_user";
		        String sftpPassword = "000000";

		        try{
		            /**
		             * Open session to sftp server
		             */
		            JSch jsch = new JSch();
		            Session session = jsch.getSession(sftpUser, sftpHost, Integer.valueOf(sftpPort));
		            session.setConfig("StrictHostKeyChecking", "no");
		            session.setPassword(sftpPassword);
		            System.out.println("Connecting------");
		            session.connect();
		            System.out.println("Established Session");
		            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
		                    .getPrincipal();
					User u=userRepository.findByEmailIgnoreCase(userDetails.getUsername());
					System.out.println("skon::"+userDetails.getUsername());
					FileInfo ff=new FileInfo(file.getOriginalFilename(), u.getId());
					ff.setAlgo(algo);
					fileInfoRepository.save(ff);
					System.out.println("azerty=="+ff.getId());
					idfile=ff.getId();
					String aa="uploads/"+u.getId()+"/"+ff.getId()+"/";
					String bb="uploads/"+u.getId()+"/";
					 Path uploadPath= Paths.get(aa);
					 Path uploadPath1= Paths.get(bb);
					 if (!Files.exists(uploadPath)) {
				            Files.createDirectories(uploadPath);
				        }
					 Path filePath = uploadPath.resolve(file.getOriginalFilename());
			      Files.copy(file.getInputStream(), filePath);
			     
			    //Use SHA-1 algorithm
			      
			     File f=new File(aa+"/"+file.getOriginalFilename());
			     
			   //Use SHA-1 algorithm
			     MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
			      
			
			     
			     String shaChecksum = getFileChecksum(shaDigest, f);
			     System.out.println("skon::"+shaChecksum);
			      byte[] bytes = Files.readAllBytes(f.toPath());
			     
					KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
				    keyPairGen.initialize(512, new SecureRandom());
				    KeyPair keyPair = keyPairGen.generateKeyPair();
				    AutomaticHash ah=new AutomaticHash();
				    byte[] signatureBytes= ah.sign("SHA256withRSA", keyPair, bytes);

		            Channel channel = session.openChannel("sftp");
		            ChannelSftp sftpChannel = (ChannelSftp) channel;
		            sftpChannel.connect();

		            System.out.println("Opened sftp Channel");

		          
		           
		            sftpChannel.cd(sftpPath);
		           
		            sftpChannel.mkdir(ff.getId().toString());
		            System.out.println("Copying file to Host");
		            sftpChannel.put(aa+"/"+file.getOriginalFilename(), sftpPath+"/"+ff.getId().toString());
		            System.out.println("Copied file to Host");
		            String FilePath="/home/sftp_user/"+ff.getId()+"/"+ff.getName();
		            String RepPath="/home/sftp_user/"+ff.getId();
		           
		            byte[]    keyBytes  =Base64.getEncoder().encode(keyPair.getPublic().getEncoded());
		          
		            String Key=new String(keyBytes,"UTF-8");
		            String aaaaasdqsd=Key.replace('+', '-').replace('/', '_');
		            
		           
		            byte[] signatureBytes2=Base64.getEncoder().encode(signatureBytes);
		            String signatureBytes3=new String(signatureBytes2,"UTF-8");
		            String signatureBytes4=signatureBytes3.replace('+', '-').replace('/', '_');;
		            System.out.println("Disconnected from sftp"+aaaaasdqsd);
		           int exit=Cyptage(FilePath,algo,RepPath,shaChecksum,signatureBytes4,aaaaasdqsd);
		            sftpChannel.disconnect();
		            
		            session.disconnect();

		            //System.out.println("Disconnected from sftp"+keyBytes);
		            Files.delete(filePath);
		            Files.delete(uploadPath);
		            System.out.println( uploadPath);
		            System.out.println( "file added");
		            if(exit==0 || exit==-1 ){
		            return "file added";}
		            else{
		            	fileInfoRepository.deleteById(idfile);
		            	return "failed";
		            }
		            
		            
		        } catch(Exception e) {fileInfoRepository.deleteById(idfile);
		            e.printStackTrace();
		        }
	    } catch (Exception e) {fileInfoRepository.deleteById(idfile);
	    	
	      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	    }
	System.out.println( "failed");
	fileInfoRepository.deleteById(idfile);
	return "failed";
	
}

public List<FileInfo> findFileUser() {
	List<FileInfo> ListFileUser=new ArrayList();;
	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
	User u=userRepository.findByEmailIgnoreCase(userDetails.getUsername());
	List<FileInfo> lf=  (List<FileInfo>) fileInfoRepository.findAll();
	for(FileInfo f : lf){
		if(f.getUser_id()==u.getId()){
			ListFileUser.add(f);
			//System.out.println("azerty=="+f.getName());
		}
		
	}
	return ListFileUser;
}
public int Cyptage(String path,String algo,String pathrep,String shaChecksum,String aa,String bb){
	String sftpHost = "20.199.45.64";
    String sftpPort = "22";
    String sftpUser = "skander";
    String sftpPassword = "000000";

    try{
        /**
         * Open session to sftp server
         */
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, Integer.valueOf(sftpPort));
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(sftpPassword);
       
        session.connect();
        System.out.println(session.isConnected());

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        InputStream in = (InputStream) channel.getInputStream();
       
       
	    if(algo.equals("ARIA/BLOWFISH")){
        channel.setCommand( "sudo /home/sftp_user/chiff1.sh "+path+" "+pathrep+" "+shaChecksum+" "+aa+" "+bb);}
	    if(algo.equals("AES/CAMILLIA")){
	        channel.setCommand( "sudo /home/sftp_user/chiff2.sh "+path+" "+pathrep+" "+shaChecksum+" "+aa+" "+bb );}
        System.out.println("azerty=="+path);
      
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();
        BufferedReader reader= new BufferedReader(new InputStreamReader(in));
        String line=null;
        while((line = reader.readLine()) != null){
        	 System.out.println("**"+line);
        }
        
        System.out.println("**"+channel.getExitStatus());
       return channel.getExitStatus();
        
       
       

    } catch(Exception e) {
        e.printStackTrace();
    }
	return (Integer) null;
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
private static String getFileChecksum(MessageDigest digest, File file) throws IOException
{
  //Get file input stream for reading the file content
  FileInputStream fis = new FileInputStream(file);
   
  //Create byte array to read data in chunks
  byte[] byteArray = new byte[1024];
  int bytesCount = 0; 
    
  //Read file data and update in message digest
  while ((bytesCount = fis.read(byteArray)) != -1) {
    digest.update(byteArray, 0, bytesCount);
  };
   
  //close the stream; We don't need it now.
  fis.close();
   
  //Get the hash's bytes
  byte[] bytes = digest.digest();
   
  //This bytes[] has bytes in decimal format;
  //Convert it to hexadecimal format
  StringBuilder sb = new StringBuilder();
  for(int i=0; i< bytes.length ;i++)
  {
    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
  }
   
  //return complete hash
   return sb.toString();
}
public void Download(Long id){
	String sftpHost = "213.199.135.92";
    String sftpPort = "22";
    String sftpUser = "skander";
    String sftpPassword = "000000";

    try{
        /**
         * Open session to sftp server
         */
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, Integer.valueOf(sftpPort));
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(sftpPassword);
       
        session.connect();
        System.out.println(session.isConnected());

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        InputStream in = (InputStream) channel.getInputStream();
        Optional<FileInfo> fileList=fileInfoRepository.findById(id);
        FileInfo f=fileList.get();
        if(f.getAlgo().equals("3DES/BLOWFISH")){
        	channel.setCommand( "sudo /home/sftp_user/dechiffrer.sh "+id+" "+f.getName() );}
    	    if(f.getAlgo().equals("AES/DES")){
    	    	channel.setCommand( "sudo /home/sftp_user/dechiffrer2.sh "+id+" "+f.getName() );}

	   
	    
	        
   
      
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();
        BufferedReader reader= new BufferedReader(new InputStreamReader(in));
        String line=null;
        while((line = reader.readLine()) != null){
        	System.out.println(line);
        }
        
        System.out.println("**"+channel.getExitStatus());
       
        
       
       

    } catch(Exception e) {
        e.printStackTrace();
    }
}
public int deletefile(Long id){
	String sftpHost = "213.199.135.92";
    String sftpPort = "22";
    String sftpUser = "skander";
    String sftpPassword = "000000";

    try{
        /**
         * Open session to sftp server
         */
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, Integer.valueOf(sftpPort));
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(sftpPassword);
       
        session.connect();
        System.out.println(session.isConnected());

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        InputStream in = (InputStream) channel.getInputStream();
       
     
        channel.setCommand( "sudo rm -r /home/skander/"+id+" /var/www/vm3.westeurope.cloudapp.azure.com/"+id);
	  
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();
        BufferedReader reader= new BufferedReader(new InputStreamReader(in));
        String line=null;
        while((line = reader.readLine()) != null){
        	 System.out.println("**"+line);
        }
        
        System.out.println("**"+channel.getExitStatus());
       return channel.getExitStatus();
        
       
       

    } catch(Exception e) {
        e.printStackTrace();
    }
	return (Integer) null;
}
public int deletefile2(Long id){
	String sftpHost = "213.199.135.92";
    String sftpPort = "22";
    String sftpUser = "skander";
    String sftpPassword = "000000";

    try{
        /**
         * Open session to sftp server
         */
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, Integer.valueOf(sftpPort));
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(sftpPassword);
       
        session.connect();
        System.out.println(session.isConnected());
        fileInfoRepository.deleteById(id);
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        InputStream in = (InputStream) channel.getInputStream();
       
     
        channel.setCommand( "sudo rm -r /home/skander/"+id);
	  
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();
        BufferedReader reader= new BufferedReader(new InputStreamReader(in));
        String line=null;
        while((line = reader.readLine()) != null){
        	 System.out.println("**"+line);
        }
        
        System.out.println("**"+channel.getExitStatus());
       return channel.getExitStatus();
        
       
       

    } catch(Exception e) {
        e.printStackTrace();
    }
	return (Integer) null;
}
public int deletefile3(Long id){
	String sftpHost = "20.234.164.108";
    String sftpPort = "22";
    String sftpUser = "skander";
    String sftpPassword = "000000";

    try{
        /**
         * Open session to sftp server
         */
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, Integer.valueOf(sftpPort));
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(sftpPassword);
       
        session.connect();
        System.out.println(session.isConnected());
       
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        InputStream in = (InputStream) channel.getInputStream();
       
     
        channel.setCommand( "sudo rm -r /home/sftp_user/"+id);
	  
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();
        BufferedReader reader= new BufferedReader(new InputStreamReader(in));
        String line=null;
        while((line = reader.readLine()) != null){
        	 System.out.println("**"+line);
        }
        
        System.out.println("**"+channel.getExitStatus());
        fileInfoRepository.deleteById(id);
       return channel.getExitStatus();
        
       
       

    } catch(Exception e) {
        e.printStackTrace();
    }
	return (Integer) null;
}

}
