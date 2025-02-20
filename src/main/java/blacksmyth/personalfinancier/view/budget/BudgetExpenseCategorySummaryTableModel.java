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
  ExpenseCategory, Amount
}


@SuppressWarnings("serial")
public class BudgetExpenseCategorySummaryTableModel extends AbstractBudgetTableModel<EXPENSE_CATEGORY_SUMMARY_COLUMNS> {

  public BudgetExpenseCategorySummaryTableModel(BudgetModel budgetModel) {
    super();
    this.setBudgetModel(budgetModel);
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    return false;
 }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
      case ExpenseCategory:
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
      case ExpenseCategory:
        return "Total Expense:";
      case Amount: 
        return getBudgetModel().getTotalExpense().getTotal();
      default:
           return null;
      }
    }
    
    if (rowNum < getBudgetModel().getExpenseCategorySummaries().size()) {
      CategorySummary summary = getBudgetModel().getExpenseCategorySummaries().get(rowNum);
      
      switch (this.getColumnEnumValueAt(colNum)) {
      case ExpenseCategory:
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