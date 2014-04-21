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

import blacksmyth.general.ReflectionUtilities;

/**
 * A class implementing the 'refined abstraction class' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and the open-source security provider
 * of BouncyCastle (https://www.bouncycastle.org/)
 */

public final class EncryptionBridge implements IEncryptionBridge {
  
  
  @Override
  public String encrypt(char[] password, String content) {
    if (encryptionAvailable()) {
      return getConcreteBridge().encrypt(password, content);
    }
    return content;
  }

  @Override
  public String decrypt(char[] password, String content) {
    if (encryptionAvailable()) {
      return getConcreteBridge().decrypt(password, content);
    }
    return null;
  }
  
  @Override
  public byte[] encrypt(char[] password, byte[] content) {
    if (encryptionAvailable()) {
      return getConcreteBridge().encrypt(password, content);
    }
    return content;
  }

  @Override
  public byte[] decrypt(char[] password, byte[] content) {
    if (encryptionAvailable()) {
      return getConcreteBridge().decrypt(password, content);
    }
    return null;
  }
  
  @Override
  public boolean encryptionAvailable() {
    return ReflectionUtilities.classAvailable(
        "org.bouncycastle.jce.provider.BouncyCastleProvider"
    );
  }
  
  private IEncryptionBridge getConcreteBridge() {
    if (encryptionAvailable()) {
      return BouncyCastleEncryptionBridge.getInstance();
    }
    return null;
  }
}
