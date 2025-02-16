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
  IncomeCategory, Amount
}

@SuppressWarnings("serial")
public class BudgetIncomeCategorySummaryTableModel extends AbstractBudgetTableModel<INCOME_CATEGORY_SUMMARY_COLUMNS> {

  public BudgetIncomeCategorySummaryTableModel(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
     return false;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
      case IncomeCategory:
        return String.class;
      case Amount: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    int income = getBudgetModel().getIncomeCategorySummaries().size();
    int expenses = getBudgetModel().getExpenseCategorySummaries().size();
    
    int balancedRowCount = Math.max(income, expenses) + 1;
    
    return balancedRowCount;
  }

  public Object getValueAt(int rowNum, int colNum) {
    if (rowNum == this.getRowCount() - 1) {
      switch (this.getColumnEnumValueAt(colNum)) {
      case IncomeCategory:
        return "Total Income:";
      case Amount: 
        return getBudgetModel().getTotalIncome().getTotal();
      default:
           return null;
      }
    }
    
    if (rowNum < getBudgetModel().getIncomeCategorySummaries().size()) {
      CategorySummary summary = getBudgetModel().getIncomeCategorySummaries().get(rowNum);
      
      switch (this.getColumnEnumValueAt(colNum)) {
      case IncomeCategory:
        return summary.getBudgetCategory().toString();
      case Amount: 
        return summary.getBudgettedAmountAtFrequency(CashFlowFrequency.Fortnightly);
      default:
           return null;
      }
    }
    
    return null;
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.fireTableDataChanged();
  }
}