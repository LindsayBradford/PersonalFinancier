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
import java.math.MathContext;

public final class MoneyUtilties {
  
  // TODO: make this an observer of the Preferences so we react correctly to context chages.
  private static final MathContext MATH_CONTEXT = PreferencesModel.getInstance().getPreferredMathContext();
  
  private static final BigDecimal MONTHS_PER_QUARTER = BigDecimalFactory.create(3);
  private static final BigDecimal MONTHS_PER_YEAR = BigDecimalFactory.create(12);

  private static final BigDecimal DAYS_PER_YEAR = BigDecimalFactory.create(365.25);
  private static final BigDecimal DAYS_PER_MONTH = DAYS_PER_YEAR.divide(MONTHS_PER_YEAR, MATH_CONTEXT);
  private static final BigDecimal DAYS_PER_FORTNIGHT = BigDecimalFactory.create(14);
  private static final BigDecimal DAYS_PER_WEEK = BigDecimalFactory.create(7);
  
  public static BigDecimal convertFrequencyAmount(BigDecimal initialAmount, CashFlowFrequency initialFrequency, 
                                                                     CashFlowFrequency newFrequency) {
    
    if (initialFrequency == newFrequency) return initialAmount;
    
    // Convert initial amount to a monthly amount, then convert the monthly amount to 
    // the amount at newFrequency. 

    // It doesn't really matter which frequency I choose to standardise on.  I've picked
    // Monthly as many of the lower frequency periods are v-easy to calculate from using it.

     return convertMonthlyAmountToNewFrequency(
         convertFrequencyAmountToMonthly(
             initialAmount, 
             initialFrequency
         ), 
         newFrequency
     );
  }
  
  /**
   * Converts an <tt>initialAmount</tt> with a cash-flow frequency of <tt>initialFreqnency</tt> to the equvalent
   * amount at a monthly frequency.
   * @param initialAmount
   * @param initialFrequency
   * @return the amount at a cash-flow frequency of monthly.
   */
  private static BigDecimal convertFrequencyAmountToMonthly(BigDecimal initialAmount, CashFlowFrequency initialFrequency) {
    switch (initialFrequency) {
    case Yearly:
      return initialAmount.divide(MONTHS_PER_YEAR, MATH_CONTEXT);  
    case Quarterly:
      return initialAmount.divide(MONTHS_PER_QUARTER, MATH_CONTEXT);  // 3 months in a quarter
    case Monthly:
      return initialAmount;  // no conversion necessary. 
    case Fortnightly:
      return initialAmount.multiply(
          DAYS_PER_MONTH.divide(DAYS_PER_FORTNIGHT, MATH_CONTEXT),
          MATH_CONTEXT
      );
    case Weekly:
      return initialAmount.multiply(
          DAYS_PER_MONTH.divide(DAYS_PER_WEEK, MATH_CONTEXT),
          MATH_CONTEXT
      );
    case Daily: 
      return initialAmount.multiply(DAYS_PER_MONTH, MATH_CONTEXT);
    }
    return null;
  }

  /**
   * Converts an amount at a cash-flow frequency of monthly to a new cash-flow frequency of 
   * <tt>newFrequency</tt>
   * @param monthlyAmount
   * @param newFrequency
   * @return The amount converted to the specified cash-flow frequency.
   */
  private static BigDecimal convertMonthlyAmountToNewFrequency(BigDecimal monthlyAmount, 
                                                               CashFlowFrequency newFrequency) {
    
    switch(newFrequency) {
    case Yearly:
      return monthlyAmount.multiply(MONTHS_PER_YEAR, MATH_CONTEXT);
    case Quarterly:
      return monthlyAmount.multiply(MONTHS_PER_QUARTER, MATH_CONTEXT);
    case Monthly:
      return monthlyAmount;
    case Fortnightly:
      return monthlyAmount.divide(
           DAYS_PER_MONTH.divide(DAYS_PER_FORTNIGHT, MATH_CONTEXT),
           MATH_CONTEXT
      );
    case Weekly:
      return monthlyAmount.divide(
          DAYS_PER_MONTH.divide(DAYS_PER_WEEK, MATH_CONTEXT),
          MATH_CONTEXT
     );
    case Daily:
      return monthlyAmount.divide(DAYS_PER_MONTH, MATH_CONTEXT);
    }
    return null;
  }
}

