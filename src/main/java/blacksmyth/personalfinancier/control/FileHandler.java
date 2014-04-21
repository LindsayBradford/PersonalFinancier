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

import blacksmyth.general.file.IFileHandlerModel;
import blacksmyth.general.file.IFileHandlerView;
import blacksmyth.general.file.IObjectFileConverter;
import blacksmyth.personalfinancier.model.IPreferenceItem;

public class FileHandler<T> {
  
  private IFileHandlerModel<T> model;
  private IFileHandlerView     view;
  
  private IObjectFileConverter<T> fileAdapter;
  private IPreferenceItem<String> filePathPreferenceItem;

  public FileHandler (IFileHandlerView view, IFileHandlerModel<T> model, 
                      IObjectFileConverter<T> fileAdapter,
                      IPreferenceItem<String> filepathPreferenceItem) {
    setModel(model);
    setView(view);
    this.fileAdapter = fileAdapter;
    this.filePathPreferenceItem = filepathPreferenceItem;
  }
  
  private void setView(IFileHandlerView view) {
    this.view = view;
  }

  public IFileHandlerModel<T> gettModel() {
    return this.model;
  }

  public void setModel(IFileHandlerModel<T> model) {
    this.model = model;
  }

  public void save() {
    if (view.getFilename() == null) {
      if (view.promptForSaveFilename()) {
        save(view.getFilename());
        
        filePathPreferenceItem.setPreference(
            view.getFilename()
        );
      }
    } else {
      save(view.getFilename());
      
      filePathPreferenceItem.setPreference(
          view.getFilename()
      );
    }
  }

  public void saveAs() {
    if (view.promptForSaveFilename()) {
      save(view.getFilename());
      
      filePathPreferenceItem.setPreference(
          view.getFilename()
      );
    }
  }

  private void save(String fileName) {
    fileAdapter.toFileFromObject(
        fileName, 
        this.gettModel().toSerializable()
    );
  }
  
  public void load() {
    view.setPromptDirectory(
        filePathPreferenceItem.getPreference()
    );

    if (view.promptForLoadFilename()) {
      load(view.getFilename());
      
      filePathPreferenceItem.setPreference(
          view.getFilename()
      );
    }
  }
  
  public void load(String filename) {
    this.gettModel().fromSerializable(
        fileAdapter.toObjectFromFile(filename)
    );
  }
}
