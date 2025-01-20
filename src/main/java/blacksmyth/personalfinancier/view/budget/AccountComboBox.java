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

import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

@SuppressWarnings("serial")
public class AccountComboBox extends JComboBox<String> implements IBudgetObserver {

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    buildItemList((BudgetModel) evt.getSource());
  }

  private void buildItemList(BudgetModel model) {
    this.removeAllItems();

    for (Account account: model.getBudgetAccounts()) {
      this.addItem(account.getNickname());
    }
  }

}
