/**
 * Copyright (c) 2015, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class CashFlowFrequencyUtilityTest {
  
  private int        initialValueAsInt = 100;
  private BigDecimal initialValue;
  private BigDecimal convertedValue;

  @Before
  public void testSetup() {
    initialValue = null;
    convertedValue = null;
  }

  @Test
  public void ValidParameters_SuccessfulConversions() {
    
    initialValue = new BigDecimal(initialValueAsInt);
    
    // Do a full loop of conversions, and see if the final result is equivalent to the original.
    
    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        initialValue, 
        CashFlowFrequency.Daily,
        CashFlowFrequency.Weekly
    );

    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        convertedValue, 
        CashFlowFrequency.Weekly,
        CashFlowFrequency.Fortnightly
    );

    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        convertedValue, 
        CashFlowFrequency.Fortnightly,
        CashFlowFrequency.Monthly
    );

    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        convertedValue, 
        CashFlowFrequency.Monthly,
        CashFlowFrequency.Quarterly
    );

    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        convertedValue, 
        CashFlowFrequency.Quarterly,
        CashFlowFrequency.Yearly
    );

    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        convertedValue, 
        CashFlowFrequency.Yearly,
        CashFlowFrequency.Daily
    );
    
    // we use BigDecimal.compareTo() instead of equals() because compareTo()
    // considers equality of numbers irrespective of scale.
       
    final int comparedNumbersAreEqual = 0;
	assertThat(initialValue.compareTo(convertedValue), is(comparedNumbersAreEqual));
  }

  @Test
  public void NullNumber_ConversionToNull() {
    
    initialValue = null;
    
    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        initialValue, 
        CashFlowFrequency.Daily,
        CashFlowFrequency.Weekly
    );
    
    // GIGO as a NINO: Null-in, null-out. 
    
    assertThat(convertedValue, is(nullValue()));
  }
  
  @Test
  public void MatchingFrequencies_ConversionIsOriginal() {
    
    initialValue = new BigDecimal(initialValueAsInt);
    
    convertedValue = CashFlowFrequencyUtility.convertFrequencyAmount(
        initialValue, 
        CashFlowFrequency.Daily,
        CashFlowFrequency.Daily
    );
    
    assertThat(convertedValue, sameInstance(initialValue));
  }
}
