/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view.budget;

import java.beans.PropertyChangeEvent;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.CategorySummary;

enum EXPENSE_CATEGORY_SUMMARY_COLUMNS {
  Expense, Budgeted
}


@SuppressWarnings("serial")
public class BudgetExpenseCategorySummaryTableModel extends AbstractBudgetTableModel<EXPENSE_CATEGORY_SUMMARY_COLUMNS> {

  public BudgetExpenseCategorySummaryTableModel(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
      case Expense:
        return String.class;
      case Budgeted: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return getBudgetModel().getExpenseCategorySummaries().size();
  }

  public Object getValueAt(int rowNum, int colNum) {
    CategorySummary summary = getBudgetModel().getExpenseCategorySummaries().get(rowNum);
    
    switch (this.getColumnEnumValueAt(colNum)) {
    case Expense:
      return summary.getBudgetCategory().toString();
    case Budgeted: 
      return summary.getBudgettedAmountAtFrequency(CashFlowFrequency.Fortnightly);
    default:
         return null;
    }
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.fireTableDataChanged();
  }
}