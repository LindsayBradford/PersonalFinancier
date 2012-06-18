package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;

import blacksmyth.personalfinancier.model.MoneyFactory;
import blacksmyth.personalfinancier.model.PreferencesModel;

public class BudgetItemFactory {
  // TODO: Expand to be more than just a nickname device.

  public static ExpenseItem createExpense() {
    return createExpense("");
  }
  
  public static ExpenseItem createExpense(String description) {
    return  new ExpenseItem(
        "Discretionary",
        description,
        MoneyFactory.createAmount(BigDecimal.ZERO),
        PreferencesModel.getInstance().getPreferredCashflowFrequency(),
        PreferencesModel.getInstance().getPreferredBudgetAccount()
    );
  }
  
  public static IncomeItem createIncome() {
    return createIncome("");
  }
  
  public static IncomeItem createIncome(String description) {
    return  new IncomeItem(
        "Default",
        description,
        MoneyFactory.createAmount(BigDecimal.ZERO),
        PreferencesModel.getInstance().getPreferredCashflowFrequency(),
        PreferencesModel.getInstance().getPreferredBudgetAccount()
    );
  }

}
