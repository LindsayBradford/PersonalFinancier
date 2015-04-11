/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model;

import java.math.BigDecimal;

public class BigDecimalFactory {
    public static BigDecimal create(int value) {
      return new BigDecimal(
          value, 
          ModelPreferences.getInstance().getPreferredMathContext()
      );
    }
    
    public static BigDecimal create(double value) {
      return new BigDecimal(
          value, 
          ModelPreferences.getInstance().getPreferredMathContext()
      );
    }
    
    public static BigDecimal create(String value) {
      return new BigDecimal(
          value, 
          ModelPreferences.getInstance().getPreferredMathContext()
      );
    }
}
