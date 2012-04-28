package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;

import blacksmyth.personalfinancier.model.MoneyAmountFactory;
import blacksmyth.personalfinancier.model.PreferencesModel;


public class BudgetItemFactory {
  // TODO: Expand to be more than just a nickname device.
  
  public static BudgetItem create(String description) {
    return  new BudgetItem(
        description,
        MoneyAmountFactory.createAmount(BigDecimal.ZERO),
        PreferencesModel.getInstance().getPreferredCashflowFrequency(),
        PreferencesModel.getInstance().getPreferredBudgetAccount()
    );
  }
}
