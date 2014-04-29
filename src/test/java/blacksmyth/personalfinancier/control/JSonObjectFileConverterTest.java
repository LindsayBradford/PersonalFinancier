/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import blacksmyth.general.file.IFileSystemBridge;
import blacksmyth.personalfinancier.dependencies.json.IJSonSerialisationBridge;

public class JSonObjectFileConverterTest {
  
  private JSonObjectFileConverter<String> testConverter;

  private IFileSystemBridge mockFileSystemBridge;
  private IJSonSerialisationBridge<String> mockJSonBridge;
  
  private static final String FILE_JSON_CONTENT =  "fileLoadResult";
  private static final String JSON_PARSE_RESULT =  "JSonSaveResult";
  
  @SuppressWarnings({"unchecked", "cast"})
  @Before
  public void testSetup() {
    testConverter = new JSonObjectFileConverter<String>();
    
    mockFileSystemBridge = mock(IFileSystemBridge.class);
    when(
        mockFileSystemBridge.loadTextFile(
            any(String.class)
         )
    ).thenReturn(
        FILE_JSON_CONTENT
    );
    
    testConverter.setFileSystemBridge(mockFileSystemBridge);

    mockJSonBridge = 
        (IJSonSerialisationBridge<String>) mock(IJSonSerialisationBridge.class);

    when(
        mockJSonBridge.toJSon(
            any(String.class)
         )
    ).thenReturn(
        JSON_PARSE_RESULT
    );
    
    testConverter.setJSonBridge(mockJSonBridge);
  }

  @Test(expected=AssertionError.class)
  public void ToFileFromObject_InvalidConfig_AssertionError() {
    JSonObjectFileConverter<String> converter = new JSonObjectFileConverter<String>();
    converter.toFileFromObject("test", "fart");
  }

  @Test(expected=AssertionError.class)
  public void ToObjectFromFile_InvalidConfig_AssertionError() {
    JSonObjectFileConverter<String> converter = new JSonObjectFileConverter<String>();
    converter.toObjectFromFile("test");
  }
  
  @Test
  public void ToFileFromObject_ValidConfig_ActionTaken() {
    testConverter.toFileFromObject("test", "fart");

    verify(mockJSonBridge).toJSon("fart");
    verify(mockFileSystemBridge).saveTextFile("test", JSON_PARSE_RESULT);
  }

  @Test
  public void ToObjectFromFile_ValidConfig_ActionTaken() {
    testConverter.toObjectFromFile("test");

    verify(mockFileSystemBridge).loadTextFile("test");
    verify(mockJSonBridge).fromJSon(FILE_JSON_CONTENT);
  }
}
