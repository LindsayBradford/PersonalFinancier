/**
 * Copyright (c) 2015, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

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
