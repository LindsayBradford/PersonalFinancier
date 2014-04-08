/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.dependencies;

/**
 * An interface defining the 'Abstraction' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and some 3rd-party AES-256 encryption library.
 */

public interface IEncryptionBridge {
  /**
   * Encrypts the content using supplied password. 
   * @param password
   * @param content
   * @return AES-256 encrypted content.
   */
  public String encrypt(char[] password, String content);

  /**
   * Decrypts the content using the supplied password,
   * @param password
   * @param salt
   * @param initialisationVector
   * @param content
   * @return
   */
  
  public String decrypt(char[] password, String content);
}
