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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import blacksmyth.general.file.IFileSystemBridge;
import blacksmyth.personalfinancier.dependencies.encryption.IEncryptionBridge;
import blacksmyth.personalfinancier.dependencies.json.EncryptedJSonFileConverter;
import blacksmyth.personalfinancier.dependencies.json.IJSonSerialisationBridge;
import blacksmyth.personalfinancier.dependencies.json.JSonObjectFileConverter;
import blacksmyth.personalfinancier.view.IPasswordPromptView;

public class EncryptedJSonFileConverterTest {
  private EncryptedJSonFileConverter<String> testConverter;

  private IPasswordPromptView mockView;
  private IPasswordPromptView noPasswordView;
  private IPasswordPromptView validPasswordView;
  private IPasswordPromptView invalidPasswordView;
  
  private IFileSystemBridge mockFileSystemBridge;
  private IJSonSerialisationBridge<String> mockJSonBridge;
  private IEncryptionBridge mockEncryptionBridge;
  
  private static final String JSON_PARSE_RESULT =  "JSonSaveResult";

  private static final char[] VALID_PASSWORD =  "BinkyIsMyNewRide".toCharArray();
  private static final char[] INVALID_PASSWORD =  "BinkyIsMyNewRider".toCharArray();

  final byte[] RAW_CONTENT = { 0,1,2,3,4,5,6,7 };  
  
  @SuppressWarnings({"unchecked", "cast"})
  @Before
  public void testSetup() {
    testConverter = new EncryptedJSonFileConverter<String>();

    mockView = mock(IPasswordPromptView.class);
    testConverter.setView(mockView);
    
    noPasswordView = mock(IPasswordPromptView.class);
    
    when(
        noPasswordView.passwordSpecified()
    ).thenReturn(
        false
    );

    invalidPasswordView = mock(IPasswordPromptView.class);
    
    when(
        invalidPasswordView.passwordSpecified()
    ).thenReturn(
        true
    );

    when(
        invalidPasswordView.getPassword()
    ).thenReturn(
        INVALID_PASSWORD
    );

    validPasswordView = mock(IPasswordPromptView.class);
    
    when(
        validPasswordView.passwordSpecified()
    ).thenReturn(
        true
    );

    when(
        validPasswordView.getPassword()
    ).thenReturn(
        VALID_PASSWORD
    );

    
    mockFileSystemBridge = mock(IFileSystemBridge.class);
    
    when(
        mockFileSystemBridge.loadBinaryFile(
            any(String.class)
         )
    ).thenReturn(
        RAW_CONTENT
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
    
    mockEncryptionBridge = mock(IEncryptionBridge.class);

    when(
        mockEncryptionBridge.decrypt(
            eq(VALID_PASSWORD),
            any(byte[].class)
         )
    ).thenReturn(
        RAW_CONTENT
    );

    when(
        mockEncryptionBridge.decrypt(
            eq(INVALID_PASSWORD),
            any(byte[].class)
         )
    ).thenReturn(
        null
    );
    
    testConverter.setEncryptionBridge(mockEncryptionBridge);
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
  public void ToFileFromObject_NoPasswordSpecified_NoActionTaken() {
    testConverter.setView(noPasswordView);

    testConverter.toFileFromObject("test", "fart");

    verify(noPasswordView).displaySavePrompt();
    verify(noPasswordView).passwordSpecified();
    
    verifyZeroInteractions(mockEncryptionBridge);
    verifyZeroInteractions(mockJSonBridge);
  }

  @Test
  public void ToObjectFromFile_NoPasswordSpecified_NoActionTaken() {
    testConverter.setView(noPasswordView);

    testConverter.toObjectFromFile("test");

    verify(noPasswordView).displayLoadPrompt();
    verify(noPasswordView).passwordSpecified();

    verifyZeroInteractions(mockEncryptionBridge);
    verifyZeroInteractions(mockJSonBridge);
  }

  @Test
  public void ToFileFromObject_PasswordSpecified_ActionTaken() {
    testConverter.setView(validPasswordView);

    testConverter.toFileFromObject("test", "fart");

    verify(validPasswordView).displaySavePrompt();
    verify(validPasswordView).passwordSpecified();
    verify(validPasswordView).getPassword();
    verify(mockJSonBridge).toJSon("fart");

    verify(mockEncryptionBridge).encrypt(
        eq(VALID_PASSWORD), 
        any(byte[].class)
    );
    
    verify(mockFileSystemBridge).saveBinaryFile(
        eq("test"), 
        any(byte[].class)
    );
    
    verify(validPasswordView).clearPassword();
  }

  @Test
  public void ToObjectFromFile_CorrectPasswordSpecified_ActionTaken() {
    testConverter.setView(validPasswordView);

    testConverter.toObjectFromFile("test");

    verify(validPasswordView).displayLoadPrompt();
    verify(validPasswordView).passwordSpecified();
    verify(validPasswordView).getPassword();

    verify(mockEncryptionBridge).decrypt(
        eq(VALID_PASSWORD), 
        any(byte[].class)
    );
    
    verify(mockFileSystemBridge).loadBinaryFile("test");

    verify(validPasswordView).clearPassword();
    verify(mockJSonBridge).fromJSon(any(String.class));
  }
  
  @Test
  public void ToObjectFromFile_IncorrectPasswordSpecified_ActionFailed() {
    testConverter.setView(invalidPasswordView);

    testConverter.toObjectFromFile("test");

    verify(invalidPasswordView).displayLoadPrompt();
    verify(invalidPasswordView).passwordSpecified();
    verify(invalidPasswordView).getPassword();

    verify(mockEncryptionBridge).decrypt(
        INVALID_PASSWORD, 
        RAW_CONTENT
    );
    
    verify(mockFileSystemBridge).loadBinaryFile("test");

    verify(invalidPasswordView).clearPassword();
    verify(invalidPasswordView).displayError(any(String.class));
  }
}
