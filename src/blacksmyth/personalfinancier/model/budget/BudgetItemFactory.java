package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.MoneyAmountFactory;
import blacksmyth.personalfinancier.model.PreferencesModel;


public class BudgetItemFactory {
  // TODO: Expand to be more than just a nickname device.
  
  public static BudgetItem createBudgetItem(String description) {
    return  new BudgetItem(
        description,
        MoneyAmountFactory.createAmount(BigDecimal.ZERO),
        PreferencesModel.getInstance().getPreferredCashflowFrequency(),
        Account.DEFAULT
    );
  }
}
