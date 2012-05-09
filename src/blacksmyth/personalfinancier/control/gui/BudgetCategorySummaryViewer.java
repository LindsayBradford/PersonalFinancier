package blacksmyth.personalfinancier.control.gui;

import java.util.Observable;

import javax.swing.table.AbstractTableModel;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.CategorySummary;

@SuppressWarnings("serial")
public class BudgetCategorySummaryViewer extends AbstractTableModel 
               implements IBudgetObserver {

  private BudgetModel budgetModel;
  
  public BudgetCategorySummaryViewer(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }

  private void setBudgetModel(BudgetModel budgetModel) {
    budgetModel.addObserver(this);
    this.budgetModel = budgetModel;
  }

  public int getColumnCount() {
    return BudgetCategorySummaryTable.COLUMN_HEADERS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return BudgetCategorySummaryTable.COLUMN_HEADERS.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (BudgetCategorySummaryTable.COLUMN_HEADERS.values()[colNum]) {
      case Category:
        return String.class;
      case Budgetted: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return budgetModel.getCategorySummaries().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    return false;
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    CategorySummary summary = (CategorySummary) getBudgetModel().getCategorySummaries().get(rowNum);
    
    switch (BudgetCategorySummaryTable.COLUMN_HEADERS.values()[colNum]) {
    case Category:
      return summary.getBudgetCategory().toString();
    case Budgetted: 
      return summary.getBudgettedAmountAtFrequency(CashFlowFrequency.Fortnightly);
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