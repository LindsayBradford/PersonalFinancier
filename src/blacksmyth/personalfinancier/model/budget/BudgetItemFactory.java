package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;

import blacksmyth.personalfinancier.model.MoneyAmountFactory;
import blacksmyth.personalfinancier.model.PreferencesModel;


public class BudgetItemFactory {
  
  public static BudgetItem createBudgetItem(String description) {
    // TODO: Expand to include accounts.
    return  new BudgetItem(
        description,
        MoneyAmountFactory.createAmount(BigDecimal.ZERO),
        PreferencesModel.getInstance().getPreferredCashflowFrequency(),
        null
    );
  }

}
