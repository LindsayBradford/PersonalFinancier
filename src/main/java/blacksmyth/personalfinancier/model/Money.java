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
import java.math.RoundingMode;
import java.util.Currency;

/**
 * This class based loosely off the <tt>Money</tt> class described here:
 * http://www.javapractices.com/topic/TopicAction.do?Id=13
 * @author linds
 *
 */
public final class Money  implements Comparable<Money> {
  /**
   * 
   */
  final static int AMOUNTS_EQUAL = 0;
  
  private Currency currency;
  private BigDecimal total;
  private RoundingMode rounding;
  
  
  public Money(String currency, String rounding, String total) {
    this.currency = Currency.getInstance(currency);
    this.rounding = RoundingMode.valueOf(rounding);
    this.total = BigDecimal.valueOf(Double.parseDouble(total));
  }
  
  public Money(Currency currency, RoundingMode rounding, BigDecimal total) {
    this.currency = currency;
    this.rounding = rounding;
    this.total = total;
  }

  public Currency getCurrency() {
    return currency;
  }

  public  void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public RoundingMode getRounding() {
    return rounding;
  }

  public void setRounding(RoundingMode rounding) {
    this.rounding = rounding;
  }
  
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  /**
   * Return <tt>true<tt> if currency of <tt>otherAmount</tt> matches this amount's currency.
   */
   public boolean hasSameCurrencyAs(Money otherAmount){
     if (otherAmount == null) return false;
     if (!this.currency.equals(otherAmount.currency)) return false;

     return true;
   }

  public int compareTo(Money otherAmount) {
    
    if (this == otherAmount) return AMOUNTS_EQUAL;

    int comparison = AMOUNTS_EQUAL;
    
    comparison = this.total.compareTo(otherAmount.total);
    if (comparison != AMOUNTS_EQUAL) return comparison;

    comparison = this.currency.getCurrencyCode().compareTo(otherAmount.currency.getCurrencyCode());
    if (comparison != AMOUNTS_EQUAL) return comparison;

    comparison = this.rounding.compareTo(otherAmount.rounding);
    if (comparison != AMOUNTS_EQUAL) return comparison;
    
    return AMOUNTS_EQUAL;
  }
}
