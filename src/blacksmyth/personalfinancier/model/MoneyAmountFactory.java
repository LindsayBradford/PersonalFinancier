/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model;

import java.math.BigDecimal;

public class MoneyAmountFactory {
   public static MoneyAmount createAmount(BigDecimal amount) {
     return new MoneyAmount(
         PreferencesModel.getInstance().getPreferredCurrency(), 
         PreferencesModel.getInstance().getPreferredRoundingMode(), 
         amount
     );
   }
   
   public static MoneyAmount createAmount(double amount) {
     return new MoneyAmount(
         PreferencesModel.getInstance().getPreferredCurrency(), 
         PreferencesModel.getInstance().getPreferredRoundingMode(), 
         BigDecimal.valueOf(amount)
     );
   }

   public static MoneyAmount createAmount(String amount) {
     return new MoneyAmount(
         PreferencesModel.getInstance().getPreferredCurrency(), 
         PreferencesModel.getInstance().getPreferredRoundingMode(), 
         new BigDecimal(amount)
     );
   }
}
