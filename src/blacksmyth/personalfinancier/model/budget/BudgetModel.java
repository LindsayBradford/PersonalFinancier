/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.CashFlowFrequency;

public class BudgetModel extends Observable implements Observer {
  private AccountModel accountModel = new AccountModel();
  private ArrayList<BudgetItem> budgetItems;
  
  public BudgetModel() {
    this.budgetItems = new ArrayList<BudgetItem>();
    
    // TODO: implement full budget account lifecycle.
    
    this.mockModelData();
    
  }
  
  // TODO: Move to budget controller.
  private void mockModelData() {

    ArrayList<BudgetItem> tmpItems = new ArrayList<BudgetItem>();

    tmpItems.add(BudgetItemFactory.create("test"));
    tmpItems.add(BudgetItemFactory.create("another test"));
    tmpItems.add(BudgetItemFactory.create("yet another test"));
    this.setBudgetItems(tmpItems);
    
  }
  
  public void addBudgetItem(BudgetItem item) {
    this.budgetItems.add(item);
    this.changeAndNotifyObservers();
  }
  
  public void removeBudgetItem(BudgetItem item) {
    this.budgetItems.remove(item);
    this.changeAndNotifyObservers();
  }
  
  public ArrayList<BudgetItem> getBudgetItems() {
    return this.budgetItems;
  }
  
  public void setBudgetItems(ArrayList<BudgetItem> budgetItems) {
    this.budgetItems = budgetItems;
    this.changeAndNotifyObservers();
  }

  public void setBudgetItemDescription(int index, String newDescription) {
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).setDescription(newDescription);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemTotal(int index, BigDecimal total) {
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).getBudgettedAmount().setTotal(total);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemFrequency(int index, CashFlowFrequency frequency) {
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).setFrequency(frequency);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemAccount(int index, String accountName) {
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).setBudgetAccount(
        accountModel.getAccount(accountName)
    );
    this.changeAndNotifyObservers();
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<Account> getBudgetAccounts() {
    return accountModel.getBudgetAccounts();
  }
  
  public Account getBudgetAccount(String nickname) {
    return accountModel.getBudgetAccount(nickname);
  }
  
  private void changeAndNotifyObservers() {
    this.setChanged();
    this.notifyObservers();
  }
  
  public void addObserver(Observer observer) {
    super.addObserver(observer);
    this.changeAndNotifyObservers();
  }

  public void update(Observable o, Object arg) {
    this.changeAndNotifyObservers();
  }
}
