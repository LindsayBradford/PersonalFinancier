package blacksmyth.personalfinancier.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.BudgetSummary;

@SuppressWarnings("serial")
public class BudgetAccountSummaryController extends AbstractTableModel implements Observer {

  private BudgetModel budgetModel;
  
  public BudgetAccountSummaryController(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }

  private void setBudgetModel(BudgetModel budgetModel) {
    budgetModel.addObserver(this);
    this.budgetModel = budgetModel;
  }

  public int getColumnCount() {
    return BUDGET_SUMMARY_COLUMNS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return BUDGET_SUMMARY_COLUMNS.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (BUDGET_SUMMARY_COLUMNS.values()[colNum]) {
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
    return budgetModel.getBudgetSummaries().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    return false;
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    BudgetSummary summary = (BudgetSummary) getBudgetModel().getBudgetSummaries().get(rowNum);
    
    switch (BUDGET_SUMMARY_COLUMNS.values()[colNum]) {
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