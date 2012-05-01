package blacksmyth.personalfinancier.gui;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComboBox;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

@SuppressWarnings("serial")
public class BudgetAccountComboBox extends JComboBox implements IBudgetObserver {

  public void update(Observable budgetModel, Object modelArgs) {
    buildItemList((BudgetModel) budgetModel);
  }
  
  @SuppressWarnings("unchecked")
  private void buildItemList(BudgetModel budgetModel) {
    this.removeAllItems();

    for (Account budgetAccount : (ArrayList<Account>) budgetModel.getBudgetAccounts()) {
      this.addItem(budgetAccount.getNickname());
    }
  }
}
