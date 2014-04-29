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

import java.util.Observer;

import blacksmyth.personalfinancier.control.IApplicationMessagePresenter;
import blacksmyth.personalfinancier.model.IPreferenceItem;

/**
 * Default implementation of the IFileHandler interface.
 * @author linds
 *
 * @param <T>
 */
public final class FileHandler<T> implements IFileHandler<T> {

  private IFileHandlerView     view;
  private IFileHandlerModel<T> model;
  
  private IObjectFileConverter<T> converter;
  private IPreferenceItem<String> filePathPreference;
  
  private IApplicationMessagePresenter appMessagePresenter;
  
  public void setAppMessagePresenter(IApplicationMessagePresenter presenter){
    this.appMessagePresenter = presenter;
  }
  
  @Override
  public void setFilePathPreferenceItem(IPreferenceItem<String> preference) {
    this.filePathPreference = preference;
  }
  
  @Override
  public void setObjectFileConverter(IObjectFileConverter<T> converter) {
    this.converter = converter;
  }
  
  @Override
  public void setView(IFileHandlerView view) {
    this.view = view;
  }

  @Override
  public void setModel(IFileHandlerModel<T> model) {
    this.model = model;
  }
  
  private boolean hasValidConfig() {
    if (view == null || model == null || 
        converter == null || filePathPreference == null) return false;
    
    return true;
  }

  @Override
  public void save() {
    assert hasValidConfig();

    if (view.getFilename() == null) {
      if (view.promptForSaveFilename()) {
        save(view.getFilename());
        
        filePathPreference.setPreference(
            view.getFilename()
        );
      }
    } else {
      save(view.getFilename());
      
      filePathPreference.setPreference(
          view.getFilename()
      );
    }
  }

  @Override
  public void saveAs() {
    assert hasValidConfig();

    if (view.promptForSaveFilename()) {
      save(view.getFilename());
      
      filePathPreference.setPreference(
          view.getFilename()
      );
    }
  }

  private void save(String fileName) {
    assert hasValidConfig();

    appMessagePresenter.setMessage(
        "Saving file " + view.getFilename()
    );

    converter.toFileFromObject(
        fileName, 
        model.toSerializable()
    );

    appMessagePresenter.setMessage(
        "File " + view.getFilename() + " saved."
    );
  }
  
  @Override
  public void load() {
    assert hasValidConfig();

    view.setPromptDirectory(
        filePathPreference.getPreference()
    );

    if (view.promptForLoadFilename()) {
      load(view.getFilename());
      
      filePathPreference.setPreference(
          view.getFilename()
      );
    }
  }
  
  private void load(String filename) {
    assert hasValidConfig();

    appMessagePresenter.setMessage(
        "Loading file " + view.getFilename()
    );
    
    T objectFromFile = converter.toObjectFromFile(filename);
    
    if (objectFromFile == null) {
      appMessagePresenter.setMessage(
          "File " + view.getFilename() + " failed to load."
      );
      return;
    }

    model.fromSerializable(
        objectFromFile
    );
    
    appMessagePresenter.setMessage(
        "File " + view.getFilename() + " loaded."
    );
  }
  
  @Override
  public void addObserver(Observer o) {
    appMessagePresenter.addObserver(o);
  }
  
}
