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

import blacksmyth.general.FileUtilities;
import blacksmyth.personalfinancier.dependencies.encryption.EncryptionBridge;
import blacksmyth.personalfinancier.dependencies.encryption.IEncryptionBridge;
import blacksmyth.personalfinancier.dependencies.json.IJSonSerialisationBridge;
import blacksmyth.personalfinancier.dependencies.json.JSonBridge;
import blacksmyth.personalfinancier.view.IPasswordPromptPresenter;
import blacksmyth.personalfinancier.view.IPasswordPromptView;

/**
 * An adapter class that transfers state between instantiated objects and file-serialised
 * object state via a 3rd-party JSON Serialization library.
 */

public class EncryptedJSonFileAdapter<T> implements IPersonalFinancierFileAdapter<T>, IPasswordPromptPresenter {
  
  private IPasswordPromptView passwordView;
  
  private IJSonSerialisationBridge<T> jsonBridge;
  private IEncryptionBridge encryptionBridge;
  
  public EncryptedJSonFileAdapter(IPasswordPromptView view) {
    init(
        new JSonBridge<T>(),
        view,
        new EncryptionBridge()
    );
  }

  public EncryptedJSonFileAdapter(IJSonSerialisationBridge<T> jsonBridge,
                                  IPasswordPromptView view,
                                  IEncryptionBridge encryptionBridge) {
    init(jsonBridge, view, encryptionBridge);
  }
  
  private void init(IJSonSerialisationBridge<T> jsonBridge,
                    IPasswordPromptView view,
                    IEncryptionBridge encryptionBridge) {
    this.jsonBridge = jsonBridge;
    this.passwordView = view;
    this.encryptionBridge = encryptionBridge;
  }
  
  @Override
  public void toFileFromObject(final String filePath, final T t) {
    passwordView.display();
    
    if (!passwordView.passwordSpecified()) {
      return;
    }
    
    FileUtilities.saveTextFile(
        filePath, 
        encryptionBridge.encrypt(
            passwordView.getPassword(), 
            jsonBridge.toJSon(t)
        )
    );

    passwordView.clearPassword();
  }

  @Override
  public T toObjectFromFile(final String filePath) {
    passwordView.display();

    if (!passwordView.passwordSpecified()) {
      return null;
    }
    
    T objectFromFile = jsonBridge.fromJson(
        encryptionBridge.decrypt(
            passwordView.getPassword(), 
            FileUtilities.loadTextFile(filePath)
        )
    );
    passwordView.clearPassword();

    return objectFromFile;
  }
  
  @Override
  public void setView(IPasswordPromptView view) {
    this.passwordView = view;
  }
}
