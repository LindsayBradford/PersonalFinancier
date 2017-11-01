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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class BouncyCastleEncryptionBridgeTest {
  
  private static IEncryptionBridge testBridge;

  final String STRING_CONTENT = "yippySkippy!";
  final byte[] BINARY_CONTENT = { 0,1,2,3,4,5,6,7 };

  final char[] VALID_PASSWORD = "hereIsThePassword".toCharArray();
  final char[] INVALID_PASSWORD = "hereIsTheBorkedPassword".toCharArray();
  
  @BeforeClass
  public static void testSetup() {
    testBridge = new BouncyCastleEncryptionBridge();
  }
  
  @Test
  public void EncryptDecrypt_StringContent_SuccessfulDecryption() {
    
    String encryptedContent = 
        testBridge.encrypt(
            VALID_PASSWORD, 
            STRING_CONTENT
        );

    String decryptedContent = 
        testBridge.decrypt(
            VALID_PASSWORD, 
            encryptedContent
         );
    
    
    assertThat(decryptedContent, is(STRING_CONTENT));
  }
  
  @Test
  public void EncryptDecrypt_BinaryContent_SuccessfulDecryption() {

    byte[] encryptedContent = 
        testBridge.encrypt(
            VALID_PASSWORD, 
            BINARY_CONTENT
        );

    byte[] decryptedContent = 
        testBridge.decrypt(
            VALID_PASSWORD, 
            encryptedContent
         );

    
    assertThat(decryptedContent, is(BINARY_CONTENT));
  }
  
  @Test
  public void EncryptDecrypt_StringContent_UnsuccessfulDecryption() {
    
    String encryptedContent = 
        testBridge.encrypt(
            VALID_PASSWORD, 
            STRING_CONTENT
        );

    String decryptedContent = 
        testBridge.decrypt(
            INVALID_PASSWORD, 
            encryptedContent
         );
    
    assertThat(decryptedContent, is(""));
  }
  
  @Test
  public void EncryptDecrypt_BinaryContent_UnsuccessfulDecryption() {

    byte[] encryptedContent = 
        testBridge.encrypt(
            VALID_PASSWORD, 
            BINARY_CONTENT
        );

    byte[] decryptedContent = 
        testBridge.decrypt(
            INVALID_PASSWORD, 
            encryptedContent
         );
    
    assertThat(decryptedContent, is(nullValue()));
  }
}
