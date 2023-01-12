package com.demo.spring.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.spring.ResponseMessage;

@RestController
public class KeyController {
	  
	 
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
	      @PostMapping(value="/getHASH")
		  	public String getHASH(@RequestParam(value = "param1", required = false)String signature,@RequestParam(value = "param2", required = false)String key) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    	 
	    	 
	            System.out.println("caaa"+key);
	    	  String carg = key.replace('-', '+').replace('_', '/');
	    	  System.out.println("caaa2"+carg);
	    	  //carg = carg.replaceAll(" ","+");
	    	  
	    	  String sig=signature.replace('-', '+').replace('_', '/');
	    	  //String a= key.replaceAll(" ", "+");
	    	  //System.out.println("caaa"+key);
				KeyFactory kf = KeyFactory.getInstance("RSA");
				X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(carg.getBytes("UTF-8")));
		        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
		        Cipher rsa;
			        rsa = Cipher.getInstance("RSA");
			        rsa.init(Cipher.DECRYPT_MODE, pubKey);
			        System.out.println("caaa123=="+sig);
			        byte[] utf8 = rsa.doFinal(Base64.getDecoder().decode(sig.getBytes("UTF-8")));
			       String sdfdsf= bytesToHex(utf8);
			     String aaaa = sdfdsf.substring(sdfdsf.length()-64); 
			      System.out.println("resaula"+aaaa );
	    	  System.out.println("caaa"+signature);
				return aaaa;
	      }
	  	@PostMapping(value="/AESsec")
		public String generateAesKey(@RequestParam(value = "param1", required = false)String pubkeyreciv) throws NoSuchAlgorithmException {

		  	KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
	    keyPairGen.initialize(512);
	    KeyPair keyPair = keyPairGen.generateKeyPair();
	    PrivateKey privateKey=keyPair.getPrivate();
	    PublicKey  publicKey=keyPair.getPublic();
	        try {
	            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
	            keyAgreement.init(privateKey);
	            PublicKey recpub=getPublicKey(pubkeyreciv);
	            keyAgreement.doPhase(recpub, true);

	            byte[] secretKey = shortenSecretKey(keyAgreement.generateSecret());
	            System.out.println("1=="+Base64.getEncoder().encodeToString(secretKey));
	            System.out.println(Base64.getUrlEncoder().encodeToString(keyPair.getPublic().getEncoded()));
	            String bb=Base64.getEncoder().encodeToString(secretKey);
	         
	  	      	  KeyGenerator keygenerator;
		  			keygenerator = KeyGenerator.getInstance("AES");
		  			 SecretKey cle = keygenerator.generateKey();
		  			 String aeskey= Base64.getUrlEncoder().encodeToString(cle.getEncoded());
		  			 final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
			            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

			            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

			            final byte[] encryptedMessage = cipher.doFinal(aeskey.getBytes());
			            String encryptedMessagestring= Base64.getUrlEncoder().encodeToString(encryptedMessage);
			            String pubkey= Base64.getUrlEncoder().encodeToString(publicKey.getEncoded());
			            System.out.println("msg="+encryptedMessagestring);
		  			 System.out.println("aes=="+aeskey);
		  		
	            return pubkey+" "+encryptedMessagestring;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return null;
	    }
	  	@PostMapping(value="/BLOsec")
		public String generateBlowfishKey(@RequestParam(value = "param1", required = false)String pubkeyreciv) throws NoSuchAlgorithmException {

		  	KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
	    keyPairGen.initialize(512);
	    KeyPair keyPair = keyPairGen.generateKeyPair();
	    PrivateKey privateKey=keyPair.getPrivate();
	    PublicKey  publicKey=keyPair.getPublic();
	        try {
	            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
	            keyAgreement.init(privateKey);
	            PublicKey recpub=getPublicKey(pubkeyreciv);
	            keyAgreement.doPhase(recpub, true);

	            byte[] secretKey = shortenSecretKey(keyAgreement.generateSecret());
	            System.out.println("1=="+Base64.getEncoder().encodeToString(secretKey));
	            System.out.println(Base64.getUrlEncoder().encodeToString(keyPair.getPublic().getEncoded()));
	            String bb=Base64.getEncoder().encodeToString(secretKey);
	         
	  	      	  KeyGenerator keygenerator;
		  			keygenerator = KeyGenerator.getInstance("Blowfish");
		  			 SecretKey cle = keygenerator.generateKey();
		  			 String aeskey= Base64.getUrlEncoder().encodeToString(cle.getEncoded());
		  			 final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
			            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

			            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

			            final byte[] encryptedMessage = cipher.doFinal(aeskey.getBytes());
			            String encryptedMessagestring= Base64.getUrlEncoder().encodeToString(encryptedMessage);
			            String pubkey= Base64.getUrlEncoder().encodeToString(publicKey.getEncoded());
			            System.out.println("msg="+encryptedMessagestring);
		  			 System.out.println("aes=="+aeskey);
		  		
	            return pubkey+" "+encryptedMessagestring;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return null;
	    }
	  	@PostMapping(value="/Aria")
		public String generate3DeshKey(@RequestParam(value = "param1", required = false)String pubkeyreciv) throws NoSuchAlgorithmException {

		  	KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
	    keyPairGen.initialize(512);
	    KeyPair keyPair = keyPairGen.generateKeyPair();
	    PrivateKey privateKey=keyPair.getPrivate();
	    PublicKey  publicKey=keyPair.getPublic();
	        try {
	            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
	            keyAgreement.init(privateKey);
	            PublicKey recpub=getPublicKey(pubkeyreciv);
	            keyAgreement.doPhase(recpub, true);

	            byte[] secretKey = shortenSecretKey(keyAgreement.generateSecret());
	            System.out.println("1=="+Base64.getEncoder().encodeToString(secretKey));
	            System.out.println(Base64.getUrlEncoder().encodeToString(keyPair.getPublic().getEncoded()));
	            String bb=Base64.getEncoder().encodeToString(secretKey);
	            KeyGenerator keygenerator;
			      
		  		JCEKeyGenerator aa=new JCEKeyGenerator("aria", 256, new CipherKeyGenerator());
				SecretKey cle=aa.engineGenerateKey();
	  	      	 
		  			 String aeskey= Base64.getUrlEncoder().encodeToString(cle.getEncoded());
		  			 final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
			            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

			            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

			            final byte[] encryptedMessage = cipher.doFinal(aeskey.getBytes());
			            String encryptedMessagestring= Base64.getUrlEncoder().encodeToString(encryptedMessage);
			            String pubkey= Base64.getUrlEncoder().encodeToString(publicKey.getEncoded());
			            System.out.println("msg="+encryptedMessagestring);
		  			 System.out.println("aes=="+aeskey);
		  		
	            return pubkey+" "+encryptedMessagestring;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return null;
	    }
	 	@PostMapping(value="/Dessec")
			public String generateDeshKey(@RequestParam(value = "param1", required = false)String pubkeyreciv) throws NoSuchAlgorithmException {

			  	KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
		    keyPairGen.initialize(512);
		    KeyPair keyPair = keyPairGen.generateKeyPair();
		    PrivateKey privateKey=keyPair.getPrivate();
		    PublicKey  publicKey=keyPair.getPublic();
		        try {
		            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
		            keyAgreement.init(privateKey);
		            PublicKey recpub=getPublicKey(pubkeyreciv);
		            keyAgreement.doPhase(recpub, true);

		            byte[] secretKey = shortenSecretKey(keyAgreement.generateSecret());
		            System.out.println("1=="+Base64.getEncoder().encodeToString(secretKey));
		            System.out.println(Base64.getUrlEncoder().encodeToString(keyPair.getPublic().getEncoded()));
		            String bb=Base64.getEncoder().encodeToString(secretKey);
		         
		            KeyGenerator keygenerator;
				      
			  		JCEKeyGenerator aa=new JCEKeyGenerator("camellia", 256, new CipherKeyGenerator());
					SecretKey cle=aa.engineGenerateKey();
			  			 String aeskey= Base64.getUrlEncoder().encodeToString(cle.getEncoded());
			  			 final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
				            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

				            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

				            final byte[] encryptedMessage = cipher.doFinal(aeskey.getBytes());
				            String encryptedMessagestring= Base64.getUrlEncoder().encodeToString(encryptedMessage);
				            String pubkey= Base64.getUrlEncoder().encodeToString(publicKey.getEncoded());
				            System.out.println("msg="+encryptedMessagestring);
			  			 System.out.println("aes=="+aeskey);
			  		
		            return pubkey+" "+encryptedMessagestring;
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				return null;
		    }
		     
	     
	  	 public  PrivateKey getPrivateKey(String privateK) {
			  PrivateKey prvKey = null;
			  try {
			 
			  byte[] privateBytes = Base64.getUrlDecoder().decode(privateK);
			  PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
			  KeyFactory keyFactory = KeyFactory.getInstance("DH");
			  prvKey = keyFactory.generatePrivate(keySpec);
			  } catch (Exception ex) {
			  ex.printStackTrace();
			  }
			  System.out.println("PRIVkey=="+prvKey);
			  return prvKey;
			  }
		  public  PublicKey getPublicKey(String publicK) {
			  PublicKey pubKey = null;
			  try {
			  
			  byte[] publicBytes = Base64.getUrlDecoder().decode(publicK);
			  X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
			  KeyFactory keyFactory = KeyFactory.getInstance("DH");
			  pubKey = keyFactory.generatePublic(keySpec);
			  } catch (Exception ex) {
			  ex.printStackTrace();
			  }
			  return pubKey;
			  }
		  
		  private byte[] shortenSecretKey(final byte[] longKey) {

		        try {

		            // Use 8 bytes (64 bits) for DES, 6 bytes (48 bits) for Blowfish
		            final byte[] shortenedKey = new byte[8];

		            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.length);

		            return shortenedKey;

		            // Below lines can be more secure
		            //final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		            // final DESKeySpec       desSpec    = new DESKeySpec(longKey);
		            //
		            // return keyFactory.generateSecret(desSpec).getEncoded();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		  	        return null;
		 
	} 
			@GetMapping(value="/keys")
			public String generateKeys() {

		        try {
		        	 
		            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
		            keyPairGenerator.initialize(512);

		            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

		            String priv=Base64.getUrlEncoder().encodeToString(keyPair.getPrivate().getEncoded());
		            String pub=Base64.getUrlEncoder().encodeToString(keyPair.getPublic().getEncoded());
		            
		            return priv+" "+pub;
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				return null;
		    }
		      @GetMapping(value="/getAESKey9")
		    	public String getKey22() throws NoSuchProviderException, NoSuchPaddingException{
		      	  KeyGenerator keygenerator;
		      
		  		JCEKeyGenerator aa=new JCEKeyGenerator("camellia", 256, new CipherKeyGenerator());
				SecretKey cleasd=aa.engineGenerateKey();
				System.out.println(bytesToHex(cleasd.getEncoded()));
				

				
				 
				 return bytesToHex(cleasd.getEncoded());
		    	  
		    	 
		    		
		    	}
		      @GetMapping(value="/getAESKey93")
		    	public String getKey223() throws NoSuchProviderException{
		      	  KeyGenerator keygenerator;
		      
		  		try {
		  		
		  			keygenerator = KeyGenerator.getInstance("Blowfish");
		  			SecureRandom secureRandom = new SecureRandom();
		  			int keyBitSize = 256;

		  			keygenerator.init(keyBitSize, secureRandom);
		  			 SecretKey cle = keygenerator.generateKey();
		  			 return bytesToHex(cle.getEncoded());
		  		} catch (NoSuchAlgorithmException e) {
		  			// TODO Auto-generated catch block
		  			e.printStackTrace();
		  		}

		    	   return null;
		    	   
		    	  
		    	 
		    		
		    	}
}
