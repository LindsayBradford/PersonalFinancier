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

  public void setBudgetItemDescription(String oldDescription, String newDescription) {
    this.getBudgetItem(oldDescription).setDescription(newDescription);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemTotal(String description, BigDecimal total) {
    this.getBudgetItem(description).getBudgettedAmount().setTotal(total);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemFrequency(String description, CashFlowFrequency frequency) {
    this.getBudgetItem(description).setFrequency(frequency);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemAccount(String description, String accountName) {
    this.getBudgetItem(description).setBudgetAccount(
        accountModel.getAccount(accountName)
    );
    this.changeAndNotifyObservers();
  }

  public BudgetItem getBudgetItem(String description) {
    for (BudgetItem item : this.getBudgetItems()) {
      if (item.getDescription().equals(description)) {
        return item;
      }
    }
    return null;
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
