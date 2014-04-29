package blacksmyth.personalfinancier.dependencies.json;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class JSonIoBridgeTest {
  
  class Bork {
    public String borkFront = "Front";
    public String borkBack = "Back";
    
    public boolean equals(Object obj) {
      if (this.getClass() != obj.getClass()) {
        return false;
      }
      Bork objAsBork = (Bork) obj;

      if (!this.borkFront.equals(objAsBork.borkFront)) {
        return false;
      }

      if (!this.borkBack.equals(objAsBork.borkBack)) {
        return false;
      }

      return true;
    }
  }

  private static IJSonSerialisationBridge<Bork> testBridge;

  final String STRING_CONTENT = "yippySkippy!";
  
  @BeforeClass
  public static void testSetup() {
    testBridge = new JSonIoBridge<Bork>();
  }
  
  @Test
  public void EncodeDecode_StringContent_SuccessfulDecode() {
    Bork originalBork = new Bork();
    originalBork.borkFront = "stuff";
    
    String encodedContent = 
        testBridge.toJSon(
            originalBork
        );

    Bork decodedContent = 
        testBridge.fromJSon(
            encodedContent
         );
    
    assertTrue(
        originalBork.equals(decodedContent)
    );
  }

  @Test
  public void Decode_StringContent_UnsuccessfulDecode() {
    Bork decodedContent = 
        testBridge.fromJSon(
            "blueSkyCorporation"
         );
    
    assertNull(decodedContent);
  }

  @Test
  public void Decode_NullContent_UnsuccessfulDecode() {
    Bork decodedContent = 
        testBridge.fromJSon(
            null
         );
    
    assertNull(decodedContent);
  }

  
}
