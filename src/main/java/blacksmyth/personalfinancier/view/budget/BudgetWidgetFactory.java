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

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.ViewPreferences;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class BudgetWidgetFactory {
  
  public static DefaultCellEditor createIncomeCategoryCellEditor(BudgetModel model) {
    IncomeCategoryComboBox comboBox = new IncomeCategoryComboBox();

    model.addObserver(comboBox);

    return new DefaultCellEditor(comboBox);
  }

  public static DefaultCellEditor createExpenseCategoryCellEditor(BudgetModel model) {
    ExpenseCategoryComboBox comboBox = new ExpenseCategoryComboBox();
    
    model.addObserver(comboBox);

    return new DefaultCellEditor(comboBox);
  }

  public static DefaultCellEditor createBudgetAccountCellEditor() {
    return new DefaultCellEditor(
        createBudgetAccountComboBox()
    );
  }
  
  /**
   * Creates a <tt>JComboBox</tt> pre-loaded with Accounts used
   * in budgetting.
   * @return JComboBox
   */
  public static JComboBox<String> createBudgetAccountComboBox() {
    AccountComboBox comboBox = new AccountComboBox();  
    
    DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
    dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    dlcr.setForeground(
        ViewPreferences.getInstance().getPreferredEditableCellColor()
    );
    comboBox.setRenderer(dlcr);

    return comboBox;
  }

  public static DefaultCellEditor createCashFlowFrequencyCellEditor() {
    JComboBox<String> comboBox = WidgetFactory.createTableComboBox();
    
    for (CashFlowFrequency frequency : CashFlowFrequency.values()) {
      comboBox.addItem(frequency.toString());
    }
    
    return new DefaultCellEditor(comboBox);
  }

}
