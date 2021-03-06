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
/**
 * Abstract interface for a modal view that prompts the user for a 
 * password. The password may be retrieved after the return to the
 * display() call. Once the user of the view is done with the password,
 * it should clear the memory occupied by the password text by calling clearPassword().
 */
public interface IPasswordPromptView  {
  /**
   * Display the password prompt for saving, and wait for a response.
   */
  void displaySavePrompt();
  
  /**
   * Display the password prompt for loading, and wait for a response.
   */
  void displayLoadPrompt();
  
  /**
   * reports on whether the user specified a password (true), or cancelled the operation (false).
   */
  boolean passwordSpecified();

  /**
   * If a password was specified, returns the password as a character array..
   */
  char[] getPassword();

  /**
   * Clears the memory occupied by the password string once it's been processed.
   */
  void clearPassword();
  
  
  /**
   * Displays an error message around password prompting.
   * @param errorMessage
   */
  void displayError(String errorMessage);
}
