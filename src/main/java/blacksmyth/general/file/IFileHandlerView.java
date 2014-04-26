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

/**
 * An MVP view interface, responsible for prompting users to supply save and load filenames.
 */
public interface IFileHandlerView {

  /**
   * Sets the directory path this view should default to when prompting for a file.
   * @param directory
   */
  public void setPromptDirectory(String directory);

  /**
   * Prompts the user to supply a filename for saving a file to.
   * @return true if the user supplied a filename, false otherwise.
   */
  public boolean promptForSaveFilename();
  
  /**
   * Prompts the user to supply a filename for loading from. 
   * @return true if the user supplied a filemane, false otherwise.
   */
  public boolean promptForLoadFilename();

  /**
   * If the user supplied a filename from a prompt, calling this method
   * will retrieve the filename specified.
   * @return The filename specified on the last successful filename prompt.
   */
  public String getFilename();
}
