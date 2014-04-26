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

import blacksmyth.personalfinancier.model.IPreferenceItem;

/**
 * Interface for MVP Presenter object that handle the interaction between
 * a view and model that saves and loads objects of type T. The presenter should
 * ensure that the user is prompted for file names where necessary, and remember
 * the last filename used as a preference item, that will be reused on subsequent 
 * save/load calls.
 * @param <T>
 */

public interface IFileHandler<T> {

  /**
   * Sets the view that the presenter will delegate all user interaction behaviour to.
   * @param view
   */
  public abstract void setView(IFileHandlerView view);

  /**
   * Sets the model that ultimately that will have files saved from and loaded to.
   * @param model
   */
  public abstract void setModel(IFileHandlerModel<T> model);

  /**
   * Sets the converter used to marshal state between objects and files. 
   * @param converter
   */
  public abstract void setObjectFileConverter(IObjectFileConverter<T> converter);

  /**
   * Sets the preference item to be used to remember the last specified filename.
   * @param preference
   */
  
  public abstract void setFilePathPreferenceItem(IPreferenceItem<String> preference);
  
  /**
   * Saves the state of the model to a file (prompting when necessary) using the specified converter.
   */
  
  public abstract void save();

  /**
   * Saves the state of the model to a file (always prompting for a filename) using the specified converter.
   */
  public abstract void saveAs();

  /**
   * Loads the state of the model from a file (always prompting for a filename) using the specified converter.
   */
  public abstract void load();

}