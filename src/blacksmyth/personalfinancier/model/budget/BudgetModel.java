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
import blacksmyth.general.SortedArrayList;
import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.MoneyUtilties;
import blacksmyth.personalfinancier.model.PreferencesModel;

public class BudgetModel extends Observable implements Observer, IBudgetController {
  
  private static final String CONTROLLER_ASSERT_MSG = "Caller does not implement IBudgetController.";
  private static final String VIEWER_ASSERT_MSG = "Caller does not implement IBudgetObserver.";

  private AccountModel accountModel;
  
  private ArrayList<ExpenseItem> expenseItems;
  private ArrayList<IncomeItem> incomeItems;
  
  private SortedArrayList<AccountSummary> accountSummaries;
  private SortedArrayList<CategorySummary> categorySummaries;
  
  public BudgetModel() {
    this.expenseItems = new ArrayList<ExpenseItem>();
    this.incomeItems = new ArrayList<IncomeItem>();

    this.accountSummaries = new SortedArrayList<AccountSummary>();
    this.categorySummaries = new SortedArrayList<CategorySummary>();

    this.accountModel = new AccountModel();

    this.changeAndNotifyObservers();
  }
  
  public BudgetModel(AccountModel accountModel) {
    this.expenseItems = new ArrayList<ExpenseItem>();
    this.accountSummaries = new SortedArrayList<AccountSummary>();
    this.categorySummaries = new SortedArrayList<CategorySummary>();
    
    this.accountModel = accountModel;
    
    this.changeAndNotifyObservers();
  }
  
  public BudgetModel(BudgetModel.SerializableState state) {
    this.expenseItems = state.expenseItems;
    this.incomeItems = state.incomeItems;

    this.accountModel = new AccountModel();

    this.changeAndNotifyObservers();
  }

  public ExpenseItem addExpenseItem() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    ExpenseItem newItem = BudgetItemFactory.createExpense();
    this.addExpenseItem(newItem);
    return newItem;
  }

  public IncomeItem addIncomeItem() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    
    IncomeItem newItem = BudgetItemFactory.createIncome();
    
    this.addIncomeItem(newItem);
    
    return newItem;
  }
  
  public void addExpenseItem(ExpenseItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems.add(item);
    this.changeAndNotifyObservers();
  }

  public void addIncomeItem(IncomeItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems.add(item);
    this.changeAndNotifyObservers();
  }

  public void addIncomeItem(int index, IncomeItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems.add(index, item);
    this.changeAndNotifyObservers();
  }

  public void addExpenseItem(int index, ExpenseItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems.add(index, item);
    this.changeAndNotifyObservers();
  }

  public ExpenseItem removeExpenseItem(int index) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    ExpenseItem removedItem = this.expenseItems.remove(index);
    this.changeAndNotifyObservers();
    return removedItem;
  }

  public IncomeItem removeIncomeItem(int index) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    IncomeItem removedItem = this.incomeItems.remove(index);
    this.changeAndNotifyObservers();
    return removedItem;
  }

  public void removeIncomeItem(IncomeItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems.remove(item);
    this.changeAndNotifyObservers();
  }
  
  public void removeExpenseItem(BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems.remove(item);
    this.changeAndNotifyObservers();
  }
  
  public ArrayList<ExpenseItem> getExpenseItems() {
    return this.expenseItems;
  }
  
  public void setExpenseItems(ArrayList<ExpenseItem> expenseItems) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems = expenseItems;
    this.changeAndNotifyObservers();
  }

  public ArrayList<IncomeItem> getIncomeItems() {
    return incomeItems;
  }

  public void setIncomeItems(ArrayList<IncomeItem> incomeItems) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems = incomeItems;
    this.changeAndNotifyObservers();
  }
  
  public void setExpenseItemDescription(int index, String newDescription) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setDescription(newDescription);
    this.changeAndNotifyObservers();
  }

  public void setIncomeItemDescription(int index, String newDescription) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setDescription(newDescription);
    this.changeAndNotifyObservers();
  }
  
  public void setExpenseItemTotal(int index, BigDecimal total) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).getBudgettedAmount().setTotal(total);
    this.changeAndNotifyObservers();
  }

  public void setIncomeItemTotal(int index, BigDecimal total) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).getBudgettedAmount().setTotal(total);
    this.changeAndNotifyObservers();
  }

  public void setExpenseItemFrequency(int index, CashFlowFrequency frequency) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setFrequency(frequency);
    this.changeAndNotifyObservers();
  }

  public void setIncomeItemFrequency(int index, CashFlowFrequency frequency) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setFrequency(frequency);
    this.changeAndNotifyObservers();
  }

  public void setExpenseItemAccount(int index, String accountName) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setBudgetAccount(
        accountModel.getAccount(accountName)
    );
    this.changeAndNotifyObservers();
  }

  public void setIncomeItemAccount(int index, String accountName) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setBudgetAccount(
        accountModel.getAccount(accountName)
    );
    this.changeAndNotifyObservers();
  }

  public void setExpenseItemCategory(int index, ExpenseCategory category) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setCategory(category);
    this.changeAndNotifyObservers();
  }

  public void setIncomeItemCategory(int index, IncomeCategory category) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setCategory(category);
    this.changeAndNotifyObservers();
  }

  public ArrayList<Account> getBudgetAccounts() {
    return accountModel.getBudgetAccounts();
  }
  
  public Account getBudgetAccount(String nickname) {
    return accountModel.getBudgetAccount(nickname);
  }
  
  public void removeAllExpenseItems() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems = new ArrayList<ExpenseItem>();
    this.changeAndNotifyObservers();
  }

  public void removeAllIncomeItems() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems = new ArrayList<IncomeItem>();
    this.changeAndNotifyObservers();
  }
  
  public void removeAllBudgetItmes() {
    this.removeAllIncomeItems();
    this.removeAllExpenseItems();
  }

  public ArrayList<AccountSummary> getAccountSummaries() {
    return this.accountSummaries;
  }
 
  public SortedArrayList<CategorySummary> getCategorySummaries() {
    return this.categorySummaries;
  }
  
  private void updateDerivedData() {
    updateAccountSummaries();
    updateCategorySummaries();
  }

  private void updateAccountSummaries() {
    Hashtable<String, AccountSummary> summaryTable = new Hashtable<String, AccountSummary>();
    this.accountSummaries = new SortedArrayList<AccountSummary>();

    for(IncomeItem item : this.incomeItems) {
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
    
    for(ExpenseItem item : this.expenseItems) {
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
      BigDecimal newTotal = originalTotal.subtract(
        convertedBudgetAmount,
        PreferencesModel.getInstance().getPreferredMathContext()
      );
      
      summary.getBudgettedAmount().setTotal(newTotal);
    }

    for(AccountSummary summary : summaryTable.values()) {
      this.accountSummaries.insertSorted(summary);
    }
  }

  // TOOO: Include IncomeItems
  private void updateCategorySummaries() {
    Hashtable<String, CategorySummary> summaryTable = new Hashtable<String, CategorySummary>();
    this.categorySummaries = new SortedArrayList<CategorySummary>();

    for(IncomeItem item : this.incomeItems) {
      if (!summaryTable.containsKey(item.getCategory().toString())) {
        CategorySummary newSummary = new CategorySummary(item.getCategory().toString());
        summaryTable.put(item.getCategory().toString(), newSummary);
      }
      
      CategorySummary summary = summaryTable.get(item.getCategory().toString());
      
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

    for(ExpenseItem item : this.expenseItems) {
      if (!summaryTable.containsKey(item.getCategory().toString())) {
        CategorySummary newSummary = new CategorySummary(item.getCategory().toString());
        summaryTable.put(item.getCategory().toString(), newSummary);
      }
      
      CategorySummary summary = summaryTable.get(item.getCategory().toString());
      
      BigDecimal convertedBudgetAmount = MoneyUtilties.convertFrequencyAmount(
              item.getBudgettedAmount().getTotal(), 
              item.getFrequency(), 
              summary.getBudgettedFrequency()
      );
      
      BigDecimal originalTotal = summary.getBudgettedAmount().getTotal();
      BigDecimal newTotal = originalTotal.subtract(
        convertedBudgetAmount,
        PreferencesModel.getInstance().getPreferredMathContext()
      );
      
      summary.getBudgettedAmount().setTotal(newTotal);
    }
    
    for (CategorySummary summary : summaryTable.values()) {
      this.categorySummaries.insertSorted(summary);
    }
  }

  public void changeAndNotifyObservers() {
    this.updateDerivedData();
    this.setChanged();
    this.notifyObservers();
  }
  
  public void addObserver(Observer observer) {
    assert (ReflectionUtilities.classImplements(observer.getClass(), IBudgetObserver.class)) : VIEWER_ASSERT_MSG;
    super.addObserver(observer);
    
    this.changeAndNotifyObservers();
  }

  public void update(Observable o, Object arg) {
    this.changeAndNotifyObservers();
  }
  
  
  public BudgetModel.SerializableState getState() {
    return new BudgetModel.SerializableState(
        this.incomeItems, 
        this.expenseItems
    );
  }

  public void setState(BudgetModel.SerializableState state) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems = state.incomeItems;
    this.expenseItems = state.expenseItems;
    
    this.changeAndNotifyObservers();
  }
  
  public class SerializableState {
    private ArrayList<IncomeItem> incomeItems;
    private ArrayList<ExpenseItem> expenseItems;
    
    public SerializableState(ArrayList<IncomeItem> incomeItems, ArrayList<ExpenseItem> expenseItems) {
      this.incomeItems = incomeItems;
      this.expenseItems = expenseItems;
    }
  }
  

  public void moveExpenseItemDown(int itemIndex) {
    ExpenseItem movingExpense = this.removeExpenseItem(itemIndex);
    this.addExpenseItem(itemIndex + 1, movingExpense);
  }
  
  public void moveExpenseItemUp(int itemIndex) {
    ExpenseItem movingExpense = this.removeExpenseItem(itemIndex);
    this.addExpenseItem(itemIndex - 1, movingExpense);
  }

  public void moveIncomeItemDown(int itemIndex) {
    IncomeItem movingIncome = this.removeIncomeItem(itemIndex);
    this.addIncomeItem(itemIndex + 1, movingIncome);
  }
  
  public void moveIncomeItemUp(int itemIndex) {
    IncomeItem movingIncome = this.removeIncomeItem(itemIndex);
    this.addIncomeItem(itemIndex - 1, movingIncome);
  }

}