/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManifestBridge {
  private static final Logger LOG = LogManager.getLogger(ManifestBridge.class);

  private static ManifestBridge only_instance;
  private static final String MANIFEST_PATH = ManifestBridge.class.getProtectionDomain().getCodeSource().getLocation().getPath();
  
  private JarFactory jarFactory = new JarFactory();

  public static synchronized ManifestBridge getInstance()   {
      if (only_instance == null)
        only_instance = new ManifestBridge(MANIFEST_PATH);

      return only_instance;
  }
  
  private Manifest manifest;
  
  private ManifestBridge(String manifestPath) {
    resolveManifest(manifestPath);
  }
  
  private void resolveManifest(String manifestPath) {
    try (JarFile jarFile = jarFactory.createFromPath(manifestPath)) {
      manifest = jarFile.getManifest();
    } catch (IOException ioe) {
      LOG.error("Could not resolve containing Jar's manifest. Supplying dummy manifest", ioe);
      createNullManifest();
    }
  }
  
  private void createNullManifest() {
    String nullManifestContent = 
        "Implementation-Title: <Unspecified>\n" + 
        "Implementation-Version: <Unspecified>\n";
    
    try (ByteArrayInputStream nullManifestContestStream = new ByteArrayInputStream(nullManifestContent.getBytes(StandardCharsets.UTF_8))) {
      manifest = new Manifest();
      manifest.read(nullManifestContestStream);
    } catch (IOException ioe) {
      LOG.error("Malformed NULL manifest supplied", ioe);
    }
  }
  
  public String getAttribute(String attributeName) {
    return manifest.getMainAttributes().getValue(attributeName);
  }
  
  void setJarFactory(JarFactory factory) {
    this.jarFactory = factory;
    this.resolveManifest(MANIFEST_PATH);
  }
  
  static void reset() {
    only_instance = null;
  }

  // Lightweight JarFile factory, allowing for test mocking.
  public class JarFactory {
    public JarFile createFromPath(String path) throws IOException {
      return new JarFile(path);
    }
  }
}
