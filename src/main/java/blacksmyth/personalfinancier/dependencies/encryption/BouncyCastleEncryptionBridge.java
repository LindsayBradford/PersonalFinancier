/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.dependencies.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import blacksmyth.general.ByteUtilities;

/**
 * A class implementing the 'Concrete Implementor class' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and the open-source security provider
 * BouncyCastle (https://www.bouncycastle.org/)
 */

final class BouncyCastleEncryptionBridge implements IEncryptionBridge {
  
  private static final Logger LOG = LogManager.getLogger(BouncyCastleEncryptionBridge.class);

  private static PaddedBufferedBlockCipher CIPHER;
  
  /**
   * Implemented as a singleton to ensure we register the BouncyCastle
   * security provider only once.
   */
  private static BouncyCastleEncryptionBridge instance;
  
  public static BouncyCastleEncryptionBridge getInstance() {
    if (instance == null) {
      instance = new BouncyCastleEncryptionBridge();
    }
    return instance;
  }
  
  protected BouncyCastleEncryptionBridge() {
    Security.addProvider(
        new BouncyCastleProvider()
    );

    CIPHER = AESBuilder.buildCipher();
  }
  
  @Override
  @Deprecated
  public String encrypt(char[] password, String content) {
    return ByteUtils.toHexString(
        encrypt(
            password, 
            ByteUtilities.stringToBytes(
                content
            )
        )
    );
  }
  
  @Override
  public byte[] encrypt(char[] password, byte[] content) {
    LOG.info("Encrypting content against password supplied");
    
    AESElements e = AESBuilder.buildElements(password);

    byte[] encryptedContent = processEncryption(e, content);
    byte[] header = ByteUtils.concatenate(e.salt, e.iv);
    
    return ByteUtils.concatenate(header, encryptedContent);
  }

  private byte[] processEncryption(AESElements e, byte[] content) {
    ParametersWithIV parameterIV =
        new ParametersWithIV(new KeyParameter(e.key),e.iv);

    LOG.debug("Encrypt salt: " + ByteUtils.toHexString(e.salt));
    LOG.debug("Encrypt  key: " + ByteUtils.toHexString(e.key));
    LOG.debug("Encrypt   IV: " + ByteUtils.toHexString(e.iv));
   
    CIPHER.init(true, parameterIV);

    return processContent(content);
  }
  
  @Override
  @Deprecated
  public String decrypt(char[] password, String content) {
    return ByteUtilities.bytesToString(
        decrypt(
            password, 
            ByteUtils.fromHexString(
                content
            )
        )
    );
  }

  @Override
  public byte[] decrypt(char[] password, byte[] content) {
    LOG.info("Decrypting content against password supplied");
    
    AESElements e = new AESElements();

    e.salt = ByteUtils.subArray(content, 0, AESBuilder.SALT_BYTES);
    e.iv   = ByteUtils.subArray(content, AESBuilder.SALT_BYTES, AESBuilder.SALT_BYTES + AESBuilder.IV_BYTES);
    byte[] encryptedContent = ByteUtils.subArray(content, AESBuilder.SALT_BYTES + AESBuilder.IV_BYTES);
    
    try {
      e.key = AESBuilder.buildKey(password, e.salt);
    } catch (Exception ex) {
      return null;
    }

    byte[] decryptedContent = processDecryption(e,encryptedContent);
    
    if (decryptedContent == null) {
      return null;
    } 
     
    return ByteUtilities.trimBytes(decryptedContent);
  }
  
  private byte[] processDecryption(AESElements e, byte[] encryptedContent) {
    ParametersWithIV parameterIV =
        new ParametersWithIV(new KeyParameter(e.key),e.iv);

    LOG.debug("Encrypt salt: " + ByteUtils.toHexString(e.salt));
    LOG.debug("Encrypt  key: " + ByteUtils.toHexString(e.key));
    LOG.debug("Encrypt   IV: " + ByteUtils.toHexString(e.iv));
   
    CIPHER.init(false, parameterIV);

    return processContent(encryptedContent);
  }
  
  private byte[] processContent(byte[] content) {
    byte[] processedBytes = new byte[CIPHER.getOutputSize(content.length)];
    int numProcessed = CIPHER.processBytes(content, 0, content.length, processedBytes, 0);

    try {
      CIPHER.doFinal(processedBytes, numProcessed);
    } catch (DataLengthException e) {
      return null;
    } catch (IllegalStateException e) {
      return null;
    } catch (InvalidCipherTextException e) {
      return null;
    }

    return processedBytes;
  }

  @Override
  public boolean encryptionAvailable() {
    return true;
  }
}

/**
 * Key salting and hashing based loosely on discussion here:
 * https://crackstation.net/hashing-security.htm#javasourcecode
 */
final class AESBuilder {
  private static SecureRandom RANDOM = generateRANDOM();

  public static final int BLOCK_BITS = 128;
  public static final int BLOCK_BYTES = BLOCK_BITS / Byte.SIZE;
  
  public static final int IV_BYTES = BLOCK_BYTES;;
  
  public static final int KEY_BITS = 256;
  public static final int KEY_BYTES = KEY_BITS / Byte.SIZE;
  
  private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

  public static final int SALT_BYTES = 32;
  public static final int HASH_ITERATIONS = 5000;
  
  private static SecureRandom generateRANDOM() {
    SecureRandom sr = new SecureRandom();
    sr.setSeed(
        System.currentTimeMillis()
    );
    return sr;
  }

  /**
   * Returns an AES block ciphers in CBC mode with padding
   * @return AES padded buffered block cipher
   */
  public static PaddedBufferedBlockCipher buildCipher() {
    return
        new PaddedBufferedBlockCipher(
            new CBCBlockCipher(
                new AESEngine()
            )
      );
  }
  
  /**
   * Builds the various random elements needed for encrypting and decryypting
   * content using AES encryption with a user-supplied password.
   * @param password
   * @return 
   */
  public static AESElements buildElements(char[]  password) {
    try {
      AESElements elements = buildKeyAndSalt(password);
      elements.iv = buildIV();
      
      return elements;
    } catch (Exception e) {
      return null;
    }
  }

  private static AESElements buildKeyAndSalt(char[] password)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    
    AESElements e = new AESElements();
    
    e.salt = buildSalt();
    e.key = buildKey(password, e.salt);

    return e;
  }

  /**
   * Builds a hashed key from the supplied password and salt.
   * @param password
   * @param salt
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  public static byte[] buildKey(char[] password, byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
      PBEKeySpec spec = new PBEKeySpec(password, salt, HASH_ITERATIONS, KEY_BYTES * 8);
      SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
      return skf.generateSecret(spec).getEncoded();
  }

  private static byte[] buildIV() {
    return buildRandomByteArray(BLOCK_BYTES);
  }

  private static byte[] buildSalt() {
    return buildRandomByteArray(SALT_BYTES);
  }
  
  private static byte[] buildRandomByteArray(int arraySize) {
    byte[] array = new byte[arraySize];
    
    RANDOM.nextBytes(array);
    
    return array;
  }
}

/**
 * Convenience state storage for all needed random AES elements.
 */
final class AESElements {
  public byte[] salt;  // salt as byte-array used to hash a password. Said hash becomes the AES key.
  public byte[] key;   // AES key as byte-array
  public byte[] iv;    // AES Initialisation Vector as a byte-array
}
