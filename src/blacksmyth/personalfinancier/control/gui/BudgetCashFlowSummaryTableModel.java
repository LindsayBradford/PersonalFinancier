package blacksmyth.personalfinancier.control.gui;

import java.util.Observable;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.AccountSummary;

enum ACCOUNT_SUMMARY_COLUMNS {
  Account, Detail, CashFlow
}

@SuppressWarnings("serial")
public class BudgetCashFlowSummaryTableModel extends AbstractBudgetTableModel<ACCOUNT_SUMMARY_COLUMNS> {

  public BudgetCashFlowSummaryTableModel(BudgetModel budgetModel) {
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
      case CashFlow: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return getBudgetModel().getCashFlowSummaries().size() + 1;
  }

  public Object getValueAt(int rowNum, int colNum) {
    
    if (rowNum == this.getRowCount() - 1) {
      switch (this.getColumnEnumValueAt(colNum)) {
      case Account:
        return null;
      case Detail:
        return "Net Cash Flow: ";
      case CashFlow: 
        return getBudgetModel().getNetCashFlow().getTotal();
      default:
           return null;
      }
    }
    
    AccountSummary summary = getBudgetModel().getCashFlowSummaries().get(rowNum);

    switch (this.getColumnEnumValueAt(colNum)) {
      case Account:
        return summary.getAccountNickname();
      case Detail:
        return summary.getAccountDetail();
      case CashFlow: 
        return summary.getBudgettedAmountAtFrequency(CashFlowFrequency.Fortnightly);
      default:
           return null;
    }
  }
  
  @Override
  public void update(Observable arg0, Object arg1) {
    this.fireTableDataChanged();
  }

}