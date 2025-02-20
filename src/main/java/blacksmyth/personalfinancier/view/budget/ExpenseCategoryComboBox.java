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

import javax.swing.JComboBox;
import javax.swing.JTextField;

import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetEvent;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

@SuppressWarnings("serial")
public class ExpenseCategoryComboBox extends JComboBox<String> implements IBudgetObserver {
  
  {
    ((JTextField) getEditor().getEditorComponent()).setHorizontalAlignment(
        JTextField.CENTER
    );
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    BudgetEvent event = (BudgetEvent) evt.getNewValue();
    if (event.getItemType() == BudgetEvent.ItemType.ExpenseCategories || 
        event.getItemType() == BudgetEvent.ItemType.AllItems) {
      buildItemList((BudgetModel) evt.getSource());
    }
  }

  private void buildItemList(BudgetModel budgetModel) {
    this.removeAllItems();

    for (String expenseCategory : budgetModel.getExpenseCategories()) {
      this.addItem(expenseCategory);
    }
  }
  
}
