package com.demo.spring.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.spring.ResponseMessage;

@RestController
public class KeyController {
	  @GetMapping(value="/getBlowfishKey")
	  	public String getKey(){
	    	  KeyGenerator keygenerator;
			try {
				keygenerator = KeyGenerator.getInstance("Blowfish");
				 SecretKey cle = keygenerator.generateKey();
				 return bytesToHex(cle.getEncoded());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	  	   return null;
	  	   
	  	  
	  	 
	  		
	  	}
	      @GetMapping(value="/getAESKey")
	    	public String getKey2(){
	      	  KeyGenerator keygenerator;
	  		try {
	  			keygenerator = KeyGenerator.getInstance("AES");
	  			 SecretKey cle = keygenerator.generateKey();
	  			 return bytesToHex(cle.getEncoded());
	  		} catch (NoSuchAlgorithmException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}

	    	   return null;
	    	   
	    	  
	    	 
	    		
	    	}
	      @GetMapping(value="/getDESKey")
	  	public String getKey3(){
	    	  KeyGenerator keygenerator;
			try {
				keygenerator = KeyGenerator.getInstance("DES");
				 SecretKey cle = keygenerator.generateKey();
				 return bytesToHex(cle.getEncoded());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	  	   return null;
	  	   	
	  	}
	      @GetMapping(value="/getRSAKey")
		  	public String getKey5(){
		    	  
				try {
					KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
					generator.initialize(512);
					KeyPair pair = generator.generateKeyPair();
					PrivateKey privateKey = pair.getPrivate();
					PublicKey publicKey = pair.getPublic();
					String pubkey=bytesToHex(publicKey.getEncoded());
					String prikey=bytesToHex(privateKey.getEncoded());
					String keypair= pubkey+"--"+prikey;
					for (String s: keypair.split("--")) {
				         System.out.println(s);
				      }
					 return prikey;
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		  	   return null;
		  	   
		  	  
		  	 
		  		
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
	      @GetMapping(value="/getRSAKey1")
		  	public void getKey6() throws NoSuchAlgorithmException, InvalidKeySpecException{
	    	  String publicKeyB64 = "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQBV8xakN/wOsB6qHpyMigk/5PrSxxd6tKTJsyMIq5f9npzZue0mI4H2o8toYImtRk6VHhcldo0t7UwsQXmFMk7D"
	    	            + "i3C53Xwfk7yEFSkXGpdtp/7fbqNnjVoJl/EPcgoDsTPrHYF/HgtmbhzuYvYeY1zpV0d2uYpFxAuqkE9FreuuH0iI8xODFe5NzRevXH116elwdCGINeAecHKgiWe"
	    	            + "bGpRPml0lagrfi0qoQvNScmi/WIN2nFcI3sQFCq3HNYDBKDhO0AEKPB2FjvoEheJJwTs5URCYsJglYyxEUon3w6KuhVa+hzYJUAgNTCsrAhQCUlX4+5LOGlwI5gonm1DYvJJZAgMBAAEB";
	    	    byte[] decoded = Base64.getDecoder().decode(publicKeyB64);
	    	    X509EncodedKeySpec spec =
	    	            new X509EncodedKeySpec(decoded);
	    	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    	    RSAPublicKey generatePublic = (RSAPublicKey) kf.generatePublic(spec);
	    	    BigInteger modulus = generatePublic.getModulus();
	    	    System.out.println(modulus);
	    	    BigInteger exponent = generatePublic.getPublicExponent();
	    	    System.out.println(exponent);
		  	 
		  		
		  	}
}
