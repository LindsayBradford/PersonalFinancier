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
 * A class implementing the 'refined abstraction class' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and the open-source security provider
 * of BouncyCastle (https://www.bouncycastle.org/)
 */

public final class EncryptionBridge implements IEncryptionBridge {
  
  private IEncryptionBridge bridge = BouncyCastleEncryptionBridge.getInstance();

  @Override
  public String encrypt(char[] password, String content) {
    return bridge.encrypt(password, content);
  }

  @Override
  public String decrypt(char[] password, String content) {
    return bridge.decrypt(password, content);
  }
  
  @Override
  public byte[] encrypt(char[] password, byte[] content) {
    return bridge.encrypt(password, content);
  }

  @Override
  public byte[] decrypt(char[] password, byte[] content) {
    return bridge.decrypt(password, content);
  }
}
