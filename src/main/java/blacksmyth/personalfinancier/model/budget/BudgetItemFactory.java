/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;

import blacksmyth.personalfinancier.model.MoneyFactory;
import blacksmyth.personalfinancier.model.ModelPreferences;

public class BudgetItemFactory {
  // TODO: Expand to be more than just a nickname device.

  public static BudgetItem createExpense() {
    return createExpense("<New Expense Budget Item>");
  }
  
  public static BudgetItem createExpense(String description) {
    return  new BudgetItem(
        Categories.DISCRETIONARY,
        description,
        MoneyFactory.createAmount(BigDecimal.ZERO),
        ModelPreferences.getInstance().getPreferredCashflowFrequency(),
        ModelPreferences.getInstance().getPreferredBudgetAccount()
    );
  }
  
  public static BudgetItem createIncome() {
    return createIncome("<New Income Budget Item>");
  }
  
  public static BudgetItem createIncome(String description) {
    return  new BudgetItem(
        Categories.SALARY,
        description,
        MoneyFactory.createAmount(BigDecimal.ZERO),
        ModelPreferences.getInstance().getPreferredCashflowFrequency(),
        ModelPreferences.getInstance().getPreferredBudgetAccount()
    );
  }

}
