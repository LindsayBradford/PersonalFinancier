package blacksmyth.personalfinancier.gui;

import java.util.Observable;

import javax.swing.table.AbstractTableModel;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.AccountSummary;

@SuppressWarnings("serial")
public class BudgetAccountSummaryViewer extends AbstractTableModel 
               implements IBudgetObserver {

  private BudgetModel budgetModel;
  
  public BudgetAccountSummaryViewer(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }

  private void setBudgetModel(BudgetModel budgetModel) {
    budgetModel.addObserver(this);
    this.budgetModel = budgetModel;
  }

  public int getColumnCount() {
    return BudgetAccountSummaryTable.COLUMN_HEADERS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return BudgetAccountSummaryTable.COLUMN_HEADERS.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (BudgetAccountSummaryTable.COLUMN_HEADERS.values()[colNum]) {
      case Account:
        return String.class;
      case Detail:
        return String.class;
      case Budgetted: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return budgetModel.getAccountSummaries().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    return false;
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    AccountSummary summary = (AccountSummary) getBudgetModel().getAccountSummaries().get(rowNum);
    
    switch (BudgetAccountSummaryTable.COLUMN_HEADERS.values()[colNum]) {
    case Account:
      return summary.getAccountNickname();
    case Detail:
      return summary.getAccountDetail();
    case Budgetted: 
      return summary.getBudgettedAmountAtFrequency(CashFlowFrequency.Monthly);
    default:
         return null;
    }
  }

  public void update(Observable o, Object arg) {
    this.fireTableDataChanged();
  }
  
  public BudgetModel getBudgetModel() {
    return budgetModel;
  }
}