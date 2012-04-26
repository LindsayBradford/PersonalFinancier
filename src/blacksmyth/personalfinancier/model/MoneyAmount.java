/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
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
public final class MoneyAmount  implements Comparable<MoneyAmount> {
  final static int AMOUNTS_EQUAL = 0;
  
  private Currency currency;
  private BigDecimal amount;
  private RoundingMode rounding;
  
  public MoneyAmount(Currency currency, RoundingMode rounding, BigDecimal amount) {
    this.currency = currency;
    this.rounding = rounding;
    this.amount = amount;
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
  
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /**
   * Return <tt>true<tt> if currency of <tt>otherAmount</tt> matches this amount's currency.
   */
   public boolean hasSameCurrencyAs(MoneyAmount otherAmount){
     if (otherAmount == null) return false;
     if (!this.currency.equals(otherAmount.currency)) return false;

     return true;
   }

  public int compareTo(MoneyAmount otherAmount) {
    
    if (this == otherAmount) return AMOUNTS_EQUAL;

    int comparison = AMOUNTS_EQUAL;
    
    comparison = this.amount.compareTo(otherAmount.amount);
    if (comparison != AMOUNTS_EQUAL) return comparison;

    comparison = this.currency.getCurrencyCode().compareTo(otherAmount.currency.getCurrencyCode());
    if (comparison != AMOUNTS_EQUAL) return comparison;

    comparison = this.rounding.compareTo(otherAmount.rounding);
    if (comparison != AMOUNTS_EQUAL) return comparison;
    
    return AMOUNTS_EQUAL;
  }
}
