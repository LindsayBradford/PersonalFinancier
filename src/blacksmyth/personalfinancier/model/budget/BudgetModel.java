/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.MoneyUtilties;
import blacksmyth.personalfinancier.model.PreferencesModel;

public class BudgetModel extends Observable implements Observer {
  private AccountModel accountModel; 
  private ArrayList<BudgetItem> budgetItems;
  private ArrayList<AccountSummary> accountSummaries;
  private ArrayList<CategorySummary> categorySummaries;
  
  public BudgetModel() {
    this.budgetItems = new ArrayList<BudgetItem>();
    this.accountSummaries = new ArrayList<AccountSummary>();
    this.categorySummaries = new ArrayList<CategorySummary>();

    this.accountModel = new AccountModel();

    this.changeAndNotifyObservers();
  }
  
  public BudgetModel(AccountModel accountModel) {
    this.budgetItems = new ArrayList<BudgetItem>();
    this.accountSummaries = new ArrayList<AccountSummary>();
    this.categorySummaries = new ArrayList<CategorySummary>();
    
    this.accountModel = accountModel;
    
    this.changeAndNotifyObservers();
  }
  

  public void addBudgetItem() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : 
      "caller to addBudgetItem() is not an IBudgetController";
    this.addBudgetItem(
        BudgetItemFactory.create()
    );
  }

  private void addBudgetItem(BudgetItem item) {
    this.budgetItems.add(item);
    this.changeAndNotifyObservers();
  }
  
  public void removeBudgetItem(int index) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    this.budgetItems.remove(index);
    this.changeAndNotifyObservers();
  }
  
  public void removeBudgetItem(BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    this.budgetItems.remove(item);
    this.changeAndNotifyObservers();
  }
  
  public ArrayList<BudgetItem> getBudgetItems() {
    return this.budgetItems;
  }
  
  public void setBudgetItems(ArrayList<BudgetItem> budgetItems) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    this.budgetItems = budgetItems;
    this.changeAndNotifyObservers();
  }

  public void setBudgetItemDescription(int index, String newDescription) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).setDescription(newDescription);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemTotal(int index, BigDecimal total) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).getBudgettedAmount().setTotal(total);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemFrequency(int index, CashFlowFrequency frequency) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).setFrequency(frequency);
    this.changeAndNotifyObservers();
  }
  
  public void setBudgetItemAccount(int index, String accountName) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
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
  
  public void setBudgetItemCategory(int index, BudgetCategory category) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    assert (index >= 0 && index < this.budgetItems.size());
    this.budgetItems.get(index).setCategory(category);
    this.changeAndNotifyObservers();
  }

  public void removeAllBudgetItems() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class);
    this.budgetItems = new ArrayList<BudgetItem>();
    this.changeAndNotifyObservers();
  }
  
  public ArrayList<AccountSummary> getAccountSummaries() {
    return this.accountSummaries;
  }
 
  public ArrayList<CategorySummary> getCategorySummaries() {
    return this.categorySummaries;
  }
  
  private void updateDerivedData() {
    updateAccountSummaries();
    updateCategorySummaries();
  }
  
  private void updateAccountSummaries() {
    Hashtable<String, AccountSummary> summaryTable = new Hashtable<String, AccountSummary>();
    this.accountSummaries = new ArrayList<AccountSummary>();
    for(BudgetItem item : this.budgetItems) {
      if (!summaryTable.containsKey(item.getBudgetAccount().getNickname())) {
        AccountSummary newSummary = new AccountSummary(item.getBudgetAccount());
        summaryTable.put(item.getBudgetAccount().getNickname(), newSummary);
      }
      
      AccountSummary summary = summaryTable.get(item.getBudgetAccount().getNickname());
      
      BigDecimal convertedBudgetAmount = MoneyUtilties.convertFrequencyAmount(
              item.getBudgettedAmount().getTotal(), 
              item.getFrequency(), 
              summary.getBudgettedFrequency()
      );
      
      BigDecimal originalTotal = summary.getBudgettedAmount().getTotal();
      BigDecimal newTotal = originalTotal.add(
        convertedBudgetAmount,
        PreferencesModel.getInstance().getPreferredMathContext()
      );
      
      summary.getBudgettedAmount().setTotal(newTotal);
    }
    this.accountSummaries = new ArrayList<AccountSummary>(summaryTable.values());
  }

  private void updateCategorySummaries() {
    Hashtable<BudgetCategory, CategorySummary> summaryTable = new Hashtable<BudgetCategory, CategorySummary>();
    this.categorySummaries = new ArrayList<CategorySummary>();
    for(BudgetItem item : this.budgetItems) {
      if (!summaryTable.containsKey(item.getCategory())) {
        CategorySummary newSummary = new CategorySummary(item.getCategory());
        summaryTable.put(item.getCategory(), newSummary);
      }
      
      CategorySummary summary = summaryTable.get(item.getCategory());
      
      BigDecimal convertedBudgetAmount = MoneyUtilties.convertFrequencyAmount(
              item.getBudgettedAmount().getTotal(), 
              item.getFrequency(), 
              summary.getBudgettedFrequency()
      );
      
      BigDecimal originalTotal = summary.getBudgettedAmount().getTotal();
      BigDecimal newTotal = originalTotal.add(
        convertedBudgetAmount,
        PreferencesModel.getInstance().getPreferredMathContext()
      );
      
      summary.getBudgettedAmount().setTotal(newTotal);
    }
    this.categorySummaries = new ArrayList<CategorySummary>(summaryTable.values());
  }

  public void changeAndNotifyObservers() {
    this.updateDerivedData();
    this.setChanged();
    this.notifyObservers();
  }
  
  public void addObserver(Observer observer) {
    assert (ReflectionUtilities.classImplements(observer.getClass(), IBudgetObserver.class)) : 
      "observer specified does not implement the interface IBudgetObserver";
    super.addObserver(observer);
    
    this.changeAndNotifyObservers();
  }

  public void update(Observable o, Object arg) {
    this.changeAndNotifyObservers();
  }
}


