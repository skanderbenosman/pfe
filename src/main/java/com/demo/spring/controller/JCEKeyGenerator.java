package com.demo.spring.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DESKeyGenerator;
import org.bouncycastle.crypto.generators.DESedeKeyGenerator;
public class JCEKeyGenerator    extends KeyGeneratorSpi
{
    public String                algName;
    public int                   keySize;
    public int                   defaultKeySize;
    public CipherKeyGenerator    engine;
    public boolean               uninitialised = true;
    public JCEKeyGenerator(
        String              algName,
        int                 defaultKeySize,
        CipherKeyGenerator  engine)
    {
        this.algName = algName;
        this.keySize = this.defaultKeySize = defaultKeySize;
        this.engine = engine;
    }
    public void engineInit(
        AlgorithmParameterSpec  params,
        SecureRandom            random)
    throws InvalidAlgorithmParameterException
    {
        throw new InvalidAlgorithmParameterException("Not Implemented");
    }
    public void engineInit(
        SecureRandom    random)
    {
        if (random != null)
        {
            uninitialised = false;
    
            engine.init(new KeyGenerationParameters(random, defaultKeySize));
        }
    }
    public void engineInit(
        int             keySize,
        SecureRandom    random)
    {
        uninitialised = false;
        try
        {
            engine.init(new KeyGenerationParameters(random, keySize));
        }
        catch (IllegalArgumentException e)
        {
            throw new InvalidParameterException(e.getMessage());
        }
    }
    public SecretKey engineGenerateKey()
    {
        if (uninitialised)
        {
            engine.init(new KeyGenerationParameters(
                                    new SecureRandom(), defaultKeySize));
        }
        return (SecretKey)(new SecretKeySpec(engine.generateKey(), algName));
        
    }
    /**
     * the generators that are defined directly off us.
     */
    /**
     * DES
     */
    
}