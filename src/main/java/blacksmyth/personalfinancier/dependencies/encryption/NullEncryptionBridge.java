package blacksmyth.personalfinancier.dependencies.encryption;

/**
 * A Null Encryption bridge that simply echos content back the caller.
 * Acts as a default encryption bridge if no actual encryption is possible.
 */
public class NullEncryptionBridge implements IEncryptionBridge {

	  /**
	   * Implemented as a singleton to ensure we register the bridge only once.
	   */
	  private static NullEncryptionBridge instance;
	  
	  public static NullEncryptionBridge getInstance() {
	    if (instance == null) {
	      instance = new NullEncryptionBridge();
	    }
	    return instance;
	  }
	
	@Override
	public boolean encryptionAvailable() {
		return false;
	}

	@Override
	public String encrypt(char[] password, String content) {
		return content;
	}

	@Override
	public String decrypt(char[] password, String content) {
		return content;
	}

	@Override
	public byte[] encrypt(char[] password, byte[] content) {
		return content;
	}

	@Override
	public byte[] decrypt(char[] password, byte[] content) {
		return content;
	}
}
