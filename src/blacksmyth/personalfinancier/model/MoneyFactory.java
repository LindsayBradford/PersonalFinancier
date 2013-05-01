/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model;

import java.math.BigDecimal;

public class MoneyFactory {
   public static Money createAmount(BigDecimal amount) {
     return new Money(
         PreferencesModel.getInstance().getPreferredCurrency(), 
         PreferencesModel.getInstance().getPreferredRoundingMode(), 
         amount
     );
   }
   
   public static Money createAmount(double amount) {
     return new Money(
         PreferencesModel.getInstance().getPreferredCurrency(), 
         PreferencesModel.getInstance().getPreferredRoundingMode(), 
         BigDecimal.valueOf(amount)
     );
   }

   public static Money createAmount(String amount) {
     return new Money(
         PreferencesModel.getInstance().getPreferredCurrency(), 
         PreferencesModel.getInstance().getPreferredRoundingMode(), 
         new BigDecimal(amount)
     );
   }
   
   public static Money copy(Money originalMoney) {
     return new Money(
         originalMoney.getCurrency(),
         originalMoney.getRounding(),
         originalMoney.getTotal()
     );
   }
}
