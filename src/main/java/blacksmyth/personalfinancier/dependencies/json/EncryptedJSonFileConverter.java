/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.dependencies.json;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blacksmyth.general.ByteUtilities;
import blacksmyth.general.file.IFileSystemBridge;
import blacksmyth.general.file.IObjectFileConverter;
import blacksmyth.personalfinancier.control.IPasswordPromptPresenter;
import blacksmyth.personalfinancier.dependencies.encryption.IEncryptionBridge;
import blacksmyth.personalfinancier.view.IPasswordPromptView;

/**
 * An adapter class that transfers state between instantiated objects and file-serialised
 * object state via a 3rd-party JSON Serialization library.
 */

public class EncryptedJSonFileConverter<T> implements IObjectFileConverter<T>, IPasswordPromptPresenter {

  private static final Logger LOG = LogManager.getLogger(EncryptedJSonFileConverter.class);
  
  private IPasswordPromptView passwordView;
  
  private IJSonSerialisationBridge<T> jsonBridge;
  private IEncryptionBridge encryptionBridge;
  private IFileSystemBridge fileSystemBridge;

  @Override
  public void setView(IPasswordPromptView view) {
    this.passwordView = view;
  }
  
  public void setEncryptionBridge(IEncryptionBridge bridge) {
    this.encryptionBridge = bridge;
  }
  
  public void setJSonBridge(IJSonSerialisationBridge<T> bridge) {
    this.jsonBridge = bridge;
  }

  public void setFileSystemBridge(IFileSystemBridge bridge) {
    this.fileSystemBridge = bridge;
  }
  
  private boolean hasValidConfig() {
    if (passwordView == null || encryptionBridge == null || 
        jsonBridge == null || fileSystemBridge == null) {
      return false;
    }
    return true;
  }
  
  @Override
  public void toFileFromObject(final String filePath, final T t) {
    assert hasValidConfig();
    
    passwordView.displaySavePrompt();
    
    if (!passwordView.passwordSpecified()) {
      return;
    }

    byte[] contentAsBytes = ByteUtilities.stringToBytes(jsonBridge.toJSon(t));

    LOG.info("Saving encrypted content to file [{}]", filePath);

    fileSystemBridge.saveBinaryFile(
        filePath, 
        encryptionBridge.encrypt(
            passwordView.getPassword(),
            contentAsBytes
        )
    );

    passwordView.clearPassword();
  }

  @Override
  public T toObjectFromFile(final String filePath) {
    assert hasValidConfig();
    
    passwordView.displayLoadPrompt();

    if (!passwordView.passwordSpecified()) {
      return null;
    }

    LOG.info("Loading encrypted content from file [{}]", filePath);

    byte[] decryptedContent = encryptionBridge.decrypt(
        passwordView.getPassword(), 
        fileSystemBridge.loadBinaryFile(filePath)
    );

    passwordView.clearPassword();

    if (decryptedContent == null) {
      passwordView.displayError(
          "The password specified could not decrypt this file."
      );
      return null;
    }

    T objectFromFile = jsonBridge.fromJSon(
        ByteUtilities.bytesToString(decryptedContent)
    );
    
    return objectFromFile;
  }
}
