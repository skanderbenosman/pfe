package com.demo.spring.service;

import java.security.KeyPair;
import java.security.Signature;
import java.util.Base64;


public class AutomaticHash {

	  //====================================================================================
	  // AUTOMATICALLY SIGN
	  //====================================================================================
	  public static  byte[]  sign(String algorithms, KeyPair keyPair, byte[] dataBytes) throws Exception {

	    //CREATE SIGNATURE (use Hash first)
	    Signature         signature = Signature.getInstance(algorithms);
	                      signature.initSign(keyPair.getPrivate());
	                      signature.update(dataBytes);
	    byte[]            signatureBytes = signature.sign();

	    //ENCODE SIGNATURE
	    byte[]            signatureEncodedBytes  = Base64.getEncoder().encode(signatureBytes);
	    String            signatureEncodedString = new String(signatureEncodedBytes);

	    //DISPLAY ENCODED SIGNATURE
	    System.out.println("SIGNATURE = " + signatureEncodedString);
	    signature.initVerify(keyPair.getPublic());
	    signature.update(dataBytes);
	    System.out.println("veriff"+signature.verify(signatureBytes));
	    //RETURN SIGNATURE
	    return signatureBytes;

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
