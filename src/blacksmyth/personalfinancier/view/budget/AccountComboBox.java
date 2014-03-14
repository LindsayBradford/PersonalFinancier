package blacksmyth.personalfinancier.view.budget;

import java.util.Observable;

import javax.swing.JComboBox;

import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

@SuppressWarnings("serial")
public class AccountComboBox extends JComboBox<String> implements IBudgetObserver {

  public void update(Observable model, Object modelArgs) {
    buildItemList((BudgetModel) model);
  }
  
  private void buildItemList(BudgetModel model) {
    this.removeAllItems();

    for (Account account: model.getBudgetAccounts()) {
      this.addItem(account.getNickname());
    }
  }
}
