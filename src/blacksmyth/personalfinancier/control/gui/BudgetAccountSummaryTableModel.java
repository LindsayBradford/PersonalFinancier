package blacksmyth.personalfinancier.control.gui;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.AccountSummary;

enum ACCOUNT_SUMMARY_COLUMNS {
  Account, Detail, Budgeted
}

@SuppressWarnings("serial")
public class BudgetAccountSummaryTableModel extends AbstractBudgetTableModel<ACCOUNT_SUMMARY_COLUMNS> {

  public BudgetAccountSummaryTableModel(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (this.getColumnEnumValueAt(colNum)) {
      case Account:
        return String.class;
      case Detail:
        return String.class;
      case Budgeted: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return getBudgetModel().getAccountSummaries().size();
  }

  public Object getValueAt(int rowNum, int colNum) {
    AccountSummary summary = getBudgetModel().getAccountSummaries().get(rowNum);
    
    switch (this.getColumnEnumValueAt(colNum)) {
    case Account:
      return summary.getAccountNickname();
    case Detail:
      return summary.getAccountDetail();
    case Budgeted: 
      return summary.getBudgettedAmountAtFrequency(CashFlowFrequency.Fortnightly);
    default:
         return null;
    }
  }
}