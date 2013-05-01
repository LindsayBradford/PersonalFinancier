package blacksmyth.personalfinancier.view.budget;

import java.util.Observable;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.CategorySummary;

enum CATEGORY_SUMMARY_COLUMNS {
  Category, Budgeted
}

@SuppressWarnings("serial")
public class BudgetCategorySummaryTableModel extends AbstractBudgetTableModel<CATEGORY_SUMMARY_COLUMNS> {

  public BudgetCategorySummaryTableModel(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
      case Category:
        return String.class;
      case Budgeted: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return getBudgetModel().getCategorySummaries().size();
  }

  public Object getValueAt(int rowNum, int colNum) {
    CategorySummary summary = getBudgetModel().getCategorySummaries().get(rowNum);
    
    switch (this.getColumnEnumValueAt(colNum)) {
    case Category:
      return summary.getBudgetCategory().toString();
    case Budgeted: 
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