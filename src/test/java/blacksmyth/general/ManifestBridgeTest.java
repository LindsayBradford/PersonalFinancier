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

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static org.hamcrest.CoreMatchers.is;

import blacksmyth.general.ManifestBridge.JarFactory;

public class ManifestBridgeTest {

  ManifestBridge bridgeUnderTest; 
  
  private static final String IMPLEMENTATION_TITLE = "Implementation-Title";
  private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
  private static final String UNSPECIFIED = "<Unspecified>";
  
  
  @Before
  public void beforeEachTest() {
    ManifestBridge.reset();
    bridgeUnderTest = ManifestBridge.getInstance();
  }
  
  @Test
  public void testSingletonInstance() {
    ManifestBridge instanceOne = ManifestBridge.getInstance();
    ManifestBridge instanceTwo = ManifestBridge.getInstance();
    
    assertThat(instanceOne, is(instanceTwo));
  }
  
  @Test
  public void testNoJarManifestAttributes() {
    ManifestBridge bridgeUnderTest = ManifestBridge.getInstance();
    
    assertThat(bridgeUnderTest.getAttribute(IMPLEMENTATION_TITLE), is(UNSPECIFIED));
    assertThat(bridgeUnderTest.getAttribute(IMPLEMENTATION_VERSION), is(UNSPECIFIED));
  }
  
  @Test
  public void testJarManifestAttributes() throws IOException {

    String expectedTitle = "Expected Title";
    String expectedVersion = "Expected Version";

    Attributes mockAttribs = mock(Attributes.class);
    when(
        mockAttribs.getValue(IMPLEMENTATION_TITLE)
    ).thenReturn(expectedTitle);

    when(
        mockAttribs.getValue(IMPLEMENTATION_VERSION)
    ).thenReturn(expectedVersion);
    
    Manifest mockManifest = mock(Manifest.class);
    when(
        mockManifest.getMainAttributes()
    ).thenReturn(mockAttribs);

    JarFile mockJarFile = mock(JarFile.class);
    when(
        mockJarFile.getManifest()
    ).thenReturn(mockManifest);

    JarFactory mockFactory = mock(JarFactory.class);
    when(
        mockFactory.createFromPath(ManifestBridge.class.getProtectionDomain().getCodeSource().getLocation().getPath())
    ).thenReturn(mockJarFile);

    bridgeUnderTest.setJarFactory(mockFactory);
    
    assertThat(bridgeUnderTest.getAttribute(IMPLEMENTATION_TITLE), is(expectedTitle));
    assertThat(bridgeUnderTest.getAttribute(IMPLEMENTATION_VERSION), is(expectedVersion));
  }
}
