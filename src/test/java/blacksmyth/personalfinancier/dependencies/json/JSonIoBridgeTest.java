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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class JSonIoBridgeTest {
  
  class FakeJsonDto {
    public String fakeFieldOne = "fakeFieldOne";
    public String fakeFieldTwo = "fakeFieldTwo";
    
    public boolean equals(Object obj) {
      if (this.getClass() != obj.getClass()) { return false; }
      
      FakeJsonDto objAsDto = (FakeJsonDto) obj;

      if (!this.fakeFieldOne.equals(objAsDto.fakeFieldOne)  || 
    	  !this.fakeFieldTwo.equals(objAsDto.fakeFieldTwo)) {
        return false;
      }

      return true;
    }
  }

  private static IJSonSerialisationBridge<FakeJsonDto> bridgeBeingTestsd;

  @BeforeClass
  public static void testSetup() {
    bridgeBeingTestsd = new JSonIoBridge<FakeJsonDto>();
  }
  
  @Test
  public void EncodeDecode_StringContent_SuccessfulDecode() {
    FakeJsonDto initialDto = new FakeJsonDto();
    initialDto.fakeFieldOne = "stuff";
    
    String encodedContent = bridgeBeingTestsd.toJSon(initialDto);
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon(encodedContent);
    
    assertThat(decodedContent, is(initialDto));
  }

  // TODO (LWB): FIx balow ignores.
  @Ignore("Generics erasure means we won't know we have the wrong class type until out here.")
  public void Decode_RawJson_SuccessfulDecode() {
    FakeJsonDto initialDto = new FakeJsonDto();
    initialDto.fakeFieldOne = "stuff";

    final String jsonToDecode = 
    	"{ \"fakeFieldOne\": \"valueOne\", \"faleFieldTwo\": \"valueTwo\"}";
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon(jsonToDecode);
    
    assertThat(decodedContent, is(notNullValue()));
  }
  
  @Ignore("Generics erasure means we won't know we have the wrong class type until out here.")
  public void Decode_StringContent_UnsuccessfulDecode() {
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon("{\"NotValidDtoJson\": \"true\"}");
    assertThat(decodedContent, is(nullValue()));
  }

  @Test
  public void Decode_NullContent_UnsuccessfulDecode() {
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon(null);
    assertThat(decodedContent, is(nullValue()));
  }
}
