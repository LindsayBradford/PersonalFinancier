/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view;

public interface IFileHandlerView {

  public void setPromptDirectory(String directory);

  public boolean promptForSaveFilename();
  public boolean promptForLoadFilename();
  
  public String getFilename();

}
