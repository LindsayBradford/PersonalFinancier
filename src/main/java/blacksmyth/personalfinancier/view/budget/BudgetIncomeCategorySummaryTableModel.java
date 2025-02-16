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

enum INCOME_CATEGORY_SUMMARY_COLUMNS {
  Income, Budgeted
}

@SuppressWarnings("serial")
public class BudgetIncomeCategorySummaryTableModel extends AbstractBudgetTableModel<INCOME_CATEGORY_SUMMARY_COLUMNS> {

  public BudgetIncomeCategorySummaryTableModel(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
      case Income:
        return String.class;
      case Budgeted: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return getBudgetModel().getIncomeCategorySummaries().size();
  }

  public Object getValueAt(int rowNum, int colNum) {
    CategorySummary summary = getBudgetModel().getIncomeCategorySummaries().get(rowNum);
    
    switch (this.getColumnEnumValueAt(colNum)) {
    case Income:
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