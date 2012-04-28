/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import blacksmyth.personalfinancier.model.Account;

public class BudgetModel extends Observable {
  private ArrayList<BudgetItem> budgetItems;
  private ArrayList<Account> budgetAccounts;
  
  public BudgetModel() {
    this.budgetItems = new ArrayList<BudgetItem>();
    this.budgetAccounts = new ArrayList<Account>();
    
    // TODO: implement full budget account lifecycle.
    
    ArrayList<Account> tmpAccounts = new ArrayList<Account>();
    tmpAccounts.add(Account.DEFAULT);
    tmpAccounts.add(new Account("General"));
    tmpAccounts.add(new Account("Bills"));
    tmpAccounts.add(new Account("Savings"));
    tmpAccounts.add(new Account("Super Fund"));
    
    this.setBudgetAccounts(tmpAccounts);
  }

  public void addBudgetItem(String description) {
    addBudgetItem(
        BudgetItemFactory.createBudgetItem(description)
    );
    this.changeAndNotifyObservers();
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
  
  public ArrayList<Account> getBudgetAccounts() {
    return this.budgetAccounts;
  }
  
  public void setBudgetAccounts(ArrayList<Account> accounts) {
    this.budgetAccounts = accounts;
    this.changeAndNotifyObservers();
  }
  
  public void addBudgetAccount(Account account) {
    this.budgetAccounts.add(account);
    this.changeAndNotifyObservers();
  }
  
  public Account getBudgetAccount(String nickname) {
    for (Account account : this.getBudgetAccounts()) {
      if (account.getNickname().equals(nickname)) {
        return account;
      }
    }
    return Account.DEFAULT;
  }
  
  private void changeAndNotifyObservers() {
    this.setChanged();
    this.notifyObservers();
  }
  
  public void addObserver(Observer observer) {
    super.addObserver(observer);
    this.changeAndNotifyObservers();
  }
}
