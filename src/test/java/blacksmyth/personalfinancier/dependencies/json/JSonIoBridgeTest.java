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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.BeforeClass;
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
  public void EncodeDecode_ValidObject_SuccessfulDecode() {
    FakeJsonDto initialDto = new FakeJsonDto();
    initialDto.fakeFieldOne = "stuff";
    
    String encodedContent = bridgeBeingTestsd.toJSon(initialDto);
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon(encodedContent);
    
    assertThat(decodedContent, is(initialDto));
  }

  @Test
  public void Decode_RawJson_SuccessfulDecode() {
    // given
	FakeJsonDto initialDto = new FakeJsonDto();
    initialDto.fakeFieldOne = "stuff";
    final String jsonToDecode = "{\r\n  \"@type\":\"blacksmyth.personalfinancier.dependencies.json.JSonIoBridgeTest$FakeJsonDto\",\r\n  \"fakeFieldOne\":\"stuff\",\r\n  \"fakeFieldTwo\":\"fakeFieldTwo\",\r\n  \"this$0\":{\r\n    \r\n  }\r\n}";
    //when
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon(jsonToDecode);
    //then
    assertThat(decodedContent, is(initialDto));
  }

  @Test
  public void Decode_InvalidStringContent_DecodesToNull() {
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon("{\"NotValidDtoJson\": \"true\"}");
    assertThat(decodedContent, is(nullValue()));
  }

  @Test
  public void Decode_NullContent_DecodesToNull() {
    FakeJsonDto decodedContent = bridgeBeingTestsd.fromJSon(null);
    assertThat(decodedContent, is(nullValue()));
  }
}
