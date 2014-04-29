/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import blacksmyth.personalfinancier.control.EncryptedJSonFileConverterTest;
import blacksmyth.personalfinancier.control.JSonObjectFileConverterTest;
import blacksmyth.personalfinancier.dependencies.encryption.BouncyCastleEncryptionBridgeTest;
import blacksmyth.personalfinancier.dependencies.json.JSonIoBridgeTest;

@RunWith(Suite.class)

@SuiteClasses({ 
  BouncyCastleEncryptionBridgeTest.class,
  JSonIoBridgeTest.class,
  JSonObjectFileConverterTest.class,
  EncryptedJSonFileConverterTest.class
})

public class AllPersonalFinancierTests {

}
