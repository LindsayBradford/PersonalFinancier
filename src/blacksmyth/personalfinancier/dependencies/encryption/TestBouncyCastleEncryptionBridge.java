package blacksmyth.personalfinancier.dependencies.encryption;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestBouncyCastleEncryptionBridge {
  
  private static IEncryptionBridge testBridge;

  final String STRING_CONTENT = "yippySkippy!";
  final byte[] BINARY_CONTENT = { 0,1,2,3,4,5,6,7 };

  final char[] PASSWORD = "hereIsThePassword".toCharArray();
  
  @BeforeClass
  public static void testSetup() {
    testBridge = new BouncyCastleEncryptionBridge();
  }
  
  @Test
  public void testEncryptDecryptString() {
    
    String encryptedContent = 
        testBridge.encrypt(
            PASSWORD, 
            STRING_CONTENT
        );

    String decryptedContent = 
        testBridge.decrypt(
            PASSWORD, 
            encryptedContent
         );
    
    assertTrue(
        STRING_CONTENT.equals(decryptedContent)
    );
  }
  
  @Test
  public void testEncryptDecryptBinary() {

    byte[] encryptedContent = 
        testBridge.encrypt(
            PASSWORD, 
            BINARY_CONTENT
        );

    byte[] decryptedContent = 
        testBridge.decrypt(
            PASSWORD, 
            encryptedContent
         );
    
    assertTrue(
        Arrays.equals(
            decryptedContent, 
            BINARY_CONTENT
        )
    );
  }

}
