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
