/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general.file;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import blacksmyth.personalfinancier.control.IApplicationMessagePresenter;
import blacksmyth.personalfinancier.model.IPreferenceItem;
import static org.mockito.Mockito.*;

public class FileHandlerTest {
  
  private static FileHandler<String> testHandler = new FileHandler<String>();
  
  private static IObjectFileConverter<String> mockConverter;
  private static IPreferenceItem<String> mockPreference;
  private static IApplicationMessagePresenter mockPresenter;
  private static IFileHandlerModel<String> mockModel;
  
  private static final String PREFERENCE_PATH = "/this/is/a/preferred/path";

  private static final String LOAD_FILE_CONTENT = "YippySkippy!";
  
  @BeforeClass
  public static void testClassSetup() {}
  
  @Before
  public void testSetup() {
    buildConfigMocks();
    
    testHandler.setObjectFileConverter(mockConverter);
    testHandler.setFilePathPreferenceItem(mockPreference);
    testHandler.setAppMessagePresenter(mockPresenter);
    testHandler.setModel(mockModel);
  }
  
  @SuppressWarnings({ "unchecked", "cast" })
  private static void buildConfigMocks() {
    
    mockConverter = mock(IObjectFileConverter.class);

    when(
        mockConverter.toObjectFromFile(any(String.class))
    ).thenReturn(LOAD_FILE_CONTENT);

    mockPresenter = mock(IApplicationMessagePresenter.class);
    mockPreference = (IPreferenceItem<String>) mock(IPreferenceItem.class);
    
    when(
        mockPreference.getPreference()
    ).thenReturn(PREFERENCE_PATH);
    
    mockModel = (IFileHandlerModel<String>) mock(IFileHandlerModel.class);
  }
  
  @Test(expected=AssertionError.class)
  public void Save_NoConfigSet_AssertionError() {
    FileHandler<String> noConfigHandler = new FileHandler<String>();
    noConfigHandler.save();
  }

  @Test(expected=AssertionError.class)
  public void SaveAs_NoConfigSet_AssertionError() {
    FileHandler<String> noConfigHandler = new FileHandler<String>();
    noConfigHandler.saveAs();
  }

  @Test(expected=AssertionError.class)
  public void Load_NoConfigSet_AssertionError() {
    FileHandler<String> noConfigHandler = new FileHandler<String>();
    noConfigHandler.load();
  }

  @Test
  public void Save_MandatoryFilenamePromptIgnored_NoActionTaken() {

    IFileHandlerView view = mock(IFileHandlerView.class);

    when(
        view.getFilename()
    ).thenReturn(null);

    when(
        view.promptForSaveFilename()
    ).thenReturn(false);
    
    testHandler.setView(view);
    
    testHandler.save();

    verifyNoInteractions(mockPresenter);
    verifyNoInteractions(mockConverter);
    verifyNoInteractions(mockPreference);
  }
  
  @Test
  public void Save_MandatoryFilenameSupplied_SaveActionTaken() {
    
    final String NEW_FILENAME = "newPromptedFile.txt";
    
    IFileHandlerView view = mock(IFileHandlerView.class);
    when(
        view.getFilename()
    ).thenReturn(null).thenReturn(NEW_FILENAME);

    when(
        view.promptForSaveFilename()
    ).thenReturn(true);
    
    testHandler.setView(view);
    
    testHandler.save();

    verify(mockPresenter, times(2)).setMessage(any(String.class));
    verify(mockConverter).toFileFromObject(NEW_FILENAME, null);
    verify(mockPreference).setPreference(NEW_FILENAME);
  }

  @Test
  public void Save_OldFilenameUsed_SaveActionTaken() {
    final String OLD_FILENAME = "oldSave.txt";
    
    IFileHandlerView view = mock(IFileHandlerView.class);
    when(
        view.getFilename()
    ).thenReturn(OLD_FILENAME).thenReturn(OLD_FILENAME);
    
    testHandler.setView(view);
    
    testHandler.save();

    verify(mockPresenter, times(2)).setMessage(any(String.class));
    verify(mockConverter).toFileFromObject(OLD_FILENAME, null);
    verify(mockPreference).setPreference(OLD_FILENAME);
  }
  
  @Test
  public void SaveAs_NewFilenameSupplied_SaveActionTaken() {
    IFileHandlerView view = mock(IFileHandlerView.class);
    
    final String NEW_SAVEAS_FILENAME = "SaveAs.txt";

    when(
        view.promptForSaveFilename()
    ).thenReturn(true);

    when(
        view.getFilename()
    ).thenReturn(NEW_SAVEAS_FILENAME).thenReturn(NEW_SAVEAS_FILENAME);
    
    testHandler.setView(view);
    
    testHandler.saveAs();

    verify(mockPresenter, times(2)).setMessage(any(String.class));
    verify(mockConverter).toFileFromObject(NEW_SAVEAS_FILENAME, null);
    verify(mockPreference).setPreference(NEW_SAVEAS_FILENAME);
  }

  @Test
  public void SaveAs_FilenamePromptIgnored_NoActionTaken() {
    IFileHandlerView view = mock(IFileHandlerView.class);

    when(
        view.promptForSaveFilename()
    ).thenReturn(false);
    
    testHandler.setView(view);
    
    testHandler.saveAs();
    
    verifyNoInteractions(mockPresenter);
    verifyNoInteractions(mockConverter);
    verifyNoInteractions(mockPreference);
  }
  
  @Test
  public void Load_FilenameSupplied_LoadSucceeded() {

    final String NEW_LOAD_FILENAME = "NewLoad.txt";

    IFileHandlerView view = mock(IFileHandlerView.class);

    when(
        view.promptForLoadFilename()
    ).thenReturn(true);
    
    when(
        view.getFilename()
    ).thenReturn(NEW_LOAD_FILENAME).thenReturn(NEW_LOAD_FILENAME);
    
    testHandler.setView(view);
    
    testHandler.load();

    verify(mockPresenter, times(2)).setMessage(any(String.class));
    verify(view).setPromptDirectory(PREFERENCE_PATH);
    verify(mockConverter).toObjectFromFile(NEW_LOAD_FILENAME);
    verify(mockModel).fromSerializable(LOAD_FILE_CONTENT);
    
    verify(mockPreference).setPreference(NEW_LOAD_FILENAME);
  }

  @Test
  public void Load_FilenamePromptIgnored_NoActionTaken() {

    IFileHandlerView view = mock(IFileHandlerView.class);

    when(
        view.promptForLoadFilename()
    ).thenReturn(false);
    
    testHandler.setView(view);
    
    testHandler.load();

    verifyNoInteractions(mockPresenter);
    verifyNoInteractions(mockConverter);
  }
}
