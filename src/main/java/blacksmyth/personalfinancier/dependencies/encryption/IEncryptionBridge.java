/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.dependencies.encryption;

/**
 * An interface defining the 'Abstraction' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and some 3rd-party AES-256 encryption library.
 * Supports the encryption library not being present by downgrading its behaviour
 * when encryption is not available.
 */

public interface IEncryptionBridge {
  
  /**
   * Reports whether encryption functionality is available.
   * @return true if encryption is possible, false otherwise.
   */
  public boolean encryptionAvailable();
  
  /**
   * Encrypts the string content using supplied password. 
   * if encryptionAvailable() would return false, the original
   * unencrypted content is returned.
   * @param password
   * @param content
   * @return AES-256 encrypted content if encryption is available.
   */
  public String encrypt(char[] password, String content);

  /**
   * Decrypts the string content using the supplied password,
   * if encryptionAvailable() would return false, null 
   * is returned.
   * @param password
   * @param salt
   * @param initialisationVector
   * @param content
   * @return decrypted content if encryption is available.
   */
  public String decrypt(char[] password, String content);
  
  /**
   * Encrypts the binary content using supplied password. 
   * if encryptionAvailable() would return false, the original
   * unencrypted content is returned.
   * @param password
   * @param content
   * @return AES-256 encrypted content if encryption is available..
   */
  public byte[] encrypt(char[] password, byte[] content);

  /**
   * Decrypts the binary content using the supplied password,
   * if encryptionAvailable() would return false, null 
   * is returned.
   * @param password
   * @param salt
   * @param initialisationVector
   * @param content
   * @return decrypted content if encryption is available.
   */
  public byte[] decrypt(char[] password, byte[] content);
  
  default String getName() {
    return this.getClass().getSimpleName();
  }
}
