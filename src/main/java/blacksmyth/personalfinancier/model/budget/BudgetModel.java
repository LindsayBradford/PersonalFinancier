/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.general.SortedArrayList;
import blacksmyth.general.file.IFileHandlerModel;
import blacksmyth.personalfinancier.control.FinancierUndoManager;
import blacksmyth.personalfinancier.control.budget.IBudgetController;
import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.CashFlowFrequencyUtility;
import blacksmyth.personalfinancier.model.ModelPreferences;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;

public class BudgetModel extends Observable
    implements Observer, IBudgetController, IFileHandlerModel<BudgetFileContent> {

  private static final String CONTROLLER_ASSERT_MSG = "Caller does not implement IBudgetController.";
  private static final String VIEWER_ASSERT_MSG = "Caller does not implement IBudgetObserver.";

  private final FinancierUndoManager undoManager = new FinancierUndoManager();

  private AccountModel accountModel;

  private HashSet<String> expenseCategories;
  private HashSet<String> incomeCategories;

  private ArrayList<BudgetItem> expenseItems;
  private ArrayList<BudgetItem> incomeItems;

  private SortedArrayList<AccountSummary> cashFlowSummaries;
  private SortedArrayList<CategorySummary> categorySummaries;

  private Money netCashFlow;
  
  private enum BudgetItemType {
    INCOME, EXPENSE
  }

  public BudgetModel(AccountModel accountModel) {
    this.expenseCategories = new HashSet<String>();
    this.incomeCategories = new HashSet<String>();

    this.expenseItems = new ArrayList<BudgetItem>();
    this.incomeItems = new ArrayList<BudgetItem>();

    this.cashFlowSummaries = new SortedArrayList<AccountSummary>();
    this.categorySummaries = new SortedArrayList<CategorySummary>();

    this.accountModel = accountModel;
    this.accountModel.addObserver(this);

    this.changeAndNotifyObservers();
  }

  public AccountModel getAccountModel() {
    return this.accountModel;
  }

  public FinancierUndoManager getUndoManager() {
    return undoManager;
  }

  public BudgetModel(BudgetFileContent state) {
    this.expenseCategories = state.expenseCategories;
    this.incomeCategories = state.incomeCategories;

    this.expenseItems = state.expenseItems;
    this.incomeItems = state.incomeItems;

    this.accountModel = new AccountModel();

    this.changeAndNotifyObservers();
  }

  public BudgetItem addExpenseItem() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    BudgetItem newItem = BudgetItemFactory.createExpense();
    this.addExpenseItem(newItem);
    return newItem;
  }

  public BudgetItem addExpenseItem(int itemIndex) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;

    BudgetItem newItem = BudgetItemFactory.createExpense();

    if (itemIndex - 1 >= 0) {
      BudgetItem itemAtIndex = this.getExpenseItems().get(itemIndex - 1);

      newItem.setBudgetAccount(itemAtIndex.getBudgetAccount());
      newItem.setCategory(itemAtIndex.getCategory());

      newItem.setFrequency(itemAtIndex.getFrequency());
    }

    this.addExpenseItem(itemIndex, newItem);
    return newItem;
  }

  public BudgetItem addIncomeItem() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;

    BudgetItem newItem = BudgetItemFactory.createIncome();

    this.addIncomeItem(newItem);

    return newItem;
  }

  public BudgetItem addIncomeItem(int itemIndex) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;

    BudgetItem newItem = BudgetItemFactory.createIncome();

    if (itemIndex - 1 >= 0) {
      BudgetItem itemAtIndex = this.getIncomeItems().get(itemIndex - 1);

      newItem.setBudgetAccount(itemAtIndex.getBudgetAccount());
      newItem.setCategory(itemAtIndex.getCategory());

      newItem.setFrequency(itemAtIndex.getFrequency());
    }

    this.addIncomeItem(itemIndex, newItem);

    return newItem;
  }

  public void addExpenseItem(BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems.add(item);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public void addIncomeItem(BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems.add(item);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void addIncomeItem(int index, BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems.add(index, item);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void addExpenseItem(int index, BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems.add(index, item);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public BudgetItem removeExpenseItem(int index) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    BudgetItem removedItem = this.expenseItems.remove(index);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
    return removedItem;
  }

  public BudgetItem removeIncomeItem(int index) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    BudgetItem removedItem = this.incomeItems.remove(index);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
    return removedItem;
  }

  public void removeIncomeItem(BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems.remove(item);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void removeExpenseItem(BudgetItem item) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems.remove(item);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public ArrayList<BudgetItem> getExpenseItems() {
    return this.expenseItems;
  }

  public void setExpenseItems(ArrayList<BudgetItem> expenseItems) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems = expenseItems;
    this.changeAndNotifyObservers();
  }

  public ArrayList<BudgetItem> getIncomeItems() {
    return incomeItems;
  }

  public void setIncomeItems(ArrayList<BudgetItem> incomeItems) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems = incomeItems;
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void setExpenseItemDescription(int index, String newDescription) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setDescription(newDescription);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public void setIncomeItemDescription(int index, String newDescription) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setDescription(newDescription);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void setExpenseItemTotal(int index, BigDecimal total) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).getBudgettedAmount().setTotal(total);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public void setIncomeItemTotal(int index, BigDecimal total) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).getBudgettedAmount().setTotal(total);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void setExpenseItemFrequency(int index, CashFlowFrequency frequency) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setFrequency(frequency);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public void setIncomeItemFrequency(int index, CashFlowFrequency frequency) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setFrequency(frequency);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void setExpenseItemAccount(int index, String accountName) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setBudgetAccount(accountModel.getAccount(accountName));
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public void setIncomeItemAccount(int index, String accountName) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setBudgetAccount(accountModel.getAccount(accountName));
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void setExpenseItemCategory(int index, String category) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.expenseItems.size());
    this.expenseItems.get(index).setCategory(category);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public void setIncomeItemCategory(int index, String category) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.incomeItems.size());
    this.incomeItems.get(index).setCategory(category);
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public ArrayList<Account> getBudgetAccounts() {
    return accountModel.getBudgetAccounts();
  }

  public Account getBudgetAccount(String nickname) {
    return accountModel.getBudgetAccount(nickname);
  }

  public void removeAllExpenseItems() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.expenseItems = new ArrayList<BudgetItem>();
    this.changeAndNotifyObservers(BudgetEvent.ItemType.ExpenseItems);
  }

  public void removeAllIncomeItems() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.incomeItems = new ArrayList<BudgetItem>();
    this.changeAndNotifyObservers(BudgetEvent.ItemType.IncomeItems);
  }

  public void removeAllBudgetItmes() {
    this.removeAllIncomeItems();
    this.removeAllExpenseItems();
  }

  public HashSet<String> getExpenseCategories() {
    return expenseCategories;
  }

  public void setExpenseCategories(HashSet<String> expenseCategories) {
    this.expenseCategories = expenseCategories;
    this.changeAndNotifyObservers(BudgetEvent.ItemType.expenseCategories);
  }

  public void addExpenseCategory(String newCategory) {
    if (this.expenseCategories.add(newCategory)) {
      this.changeAndNotifyObservers(BudgetEvent.ItemType.expenseCategories);
    }
  }

  public HashSet<String> getIncomeCategories() {
    return incomeCategories;
  }

  public void setIncomeCategories(HashSet<String> incomeCategories) {
    this.incomeCategories = incomeCategories;
    this.changeAndNotifyObservers(BudgetEvent.ItemType.incomeCategories);
  }

  public void addIncomeCategory(String newCategory) {
    System.out.println("Adding Income Category: " + newCategory);
    if (this.incomeCategories.add(newCategory)) {
      this.changeAndNotifyObservers(BudgetEvent.ItemType.incomeCategories);
    }
  }

  public void moveExpenseItemDown(int itemIndex) {
    BudgetItem movingExpense = this.removeExpenseItem(itemIndex);
    this.addExpenseItem(itemIndex + 1, movingExpense);
  }

  public void moveExpenseItemUp(int itemIndex) {
    BudgetItem movingExpense = this.removeExpenseItem(itemIndex);
    this.addExpenseItem(itemIndex - 1, movingExpense);
  }

  public void moveIncomeItemDown(int itemIndex) {
    BudgetItem movingIncome = this.removeIncomeItem(itemIndex);
    this.addIncomeItem(itemIndex + 1, movingIncome);
  }

  public void moveIncomeItemUp(int itemIndex) {
    BudgetItem movingIncome = this.removeIncomeItem(itemIndex);
    this.addIncomeItem(itemIndex - 1, movingIncome);
  }

  public Money getNetCashFlow() {
    return this.netCashFlow;
  }

  public ArrayList<AccountSummary> getCashFlowSummaries() {
    return this.cashFlowSummaries;
  }

  public SortedArrayList<CategorySummary> getCategorySummaries() {
    return this.categorySummaries;
  }

  private void updateDerivedData() {
    updateCashFlowSummaries();
    updateCategorySummaries();
    updateNetCashFlow();
  }

  private void updateCashFlowSummaries() {
    Hashtable<String, AccountSummary> summaryTable = buildSummaryTable();

    incomeItems.stream().forEach(
        item -> addItemToSummaryTable(summaryTable, item, BudgetItemType.INCOME)
    );
    
    expenseItems.stream().forEach(
        item -> addItemToSummaryTable(summaryTable, item, BudgetItemType.EXPENSE)
    );
    
    cashFlowSummaries.clear();
    summaryTable.values().stream().forEach(cashFlowSummaries::insertSorted);
  }

  private void addItemToSummaryTable(Hashtable<String, AccountSummary> summaryTable, BudgetItem item, BudgetItemType budgetItemType) {
    AccountSummary summary = summaryTable.get(item.getBudgetAccount().getNickname());

    BigDecimal convertedBudgetAmount = CashFlowFrequencyUtility.convertFrequencyAmount(
        item.getBudgettedAmount().getTotal(), item.getFrequency(), summary.getBudgettedFrequency());

    BigDecimal originalTotal = summary.getBudgettedAmount().getTotal();
    BigDecimal newTotal = null;
    
    switch(budgetItemType) {
    case INCOME:
      newTotal = originalTotal.add(convertedBudgetAmount, ModelPreferences.getInstance().getPreferredMathContext());
      break;
    case EXPENSE:
      newTotal = originalTotal.subtract(convertedBudgetAmount, ModelPreferences.getInstance().getPreferredMathContext());
      break;
    }
    
    summary.getBudgettedAmount().setTotal(newTotal);
  }

  private Hashtable<String, AccountSummary> buildSummaryTable() {
    Hashtable<String, AccountSummary> summaryTable = new Hashtable<>();

    accountModel.getBudgetAccounts().stream()
      .map(AccountSummary::new)
      .forEach(accountSummary -> summaryTable.put(accountSummary.getAccountNickname(), accountSummary));
    
      return summaryTable;
  }

  private void updateCategorySummaries() {
    Hashtable<String, CategorySummary> summaryTable = new Hashtable<String, CategorySummary>();
    this.categorySummaries = new SortedArrayList<CategorySummary>();

    for (BudgetItem item : this.incomeItems) {
      if (!summaryTable.containsKey(item.getCategory().toString())) {
        CategorySummary newSummary = new CategorySummary(item.getCategory().toString());
        summaryTable.put(item.getCategory().toString(), newSummary);
      }

      CategorySummary summary = summaryTable.get(item.getCategory().toString());

      BigDecimal convertedBudgetAmount = CashFlowFrequencyUtility.convertFrequencyAmount(
          item.getBudgettedAmount().getTotal(), item.getFrequency(), summary.getBudgettedFrequency());

      BigDecimal originalTotal = summary.getBudgettedAmount().getTotal();
      BigDecimal newTotal = originalTotal.add(convertedBudgetAmount,
          ModelPreferences.getInstance().getPreferredMathContext());

      summary.getBudgettedAmount().setTotal(newTotal);
    }

    for (BudgetItem item : this.expenseItems) {
      if (!summaryTable.containsKey(item.getCategory().toString())) {
        CategorySummary newSummary = new CategorySummary(item.getCategory().toString());
        summaryTable.put(item.getCategory().toString(), newSummary);
      }

      CategorySummary summary = summaryTable.get(item.getCategory().toString());

      BigDecimal convertedBudgetAmount = CashFlowFrequencyUtility.convertFrequencyAmount(
          item.getBudgettedAmount().getTotal(), item.getFrequency(), summary.getBudgettedFrequency());

      BigDecimal originalTotal = summary.getBudgettedAmount().getTotal();
      BigDecimal newTotal = originalTotal.subtract(convertedBudgetAmount,
          ModelPreferences.getInstance().getPreferredMathContext());

      summary.getBudgettedAmount().setTotal(newTotal);
    }

    for (CategorySummary summary : summaryTable.values()) {
      this.categorySummaries.insertSorted(summary);
    }
  }
  
  private void updateNetCashFlow() {
    netCashFlow = MoneyFactory.createAmount(0);

    cashFlowSummaries.stream()
        .forEach(
            summary -> netCashFlow.setTotal(
                          netCashFlow.getTotal().add(
                              summary.getBudgettedAmount().getTotal()
                           )
                        )
        );
  }

  public void changeAndNotifyObservers() {
    changeAndNotifyObservers(BudgetEvent.ItemType.AllItems);
  }

  public void changeAndNotifyObservers(BudgetEvent.ItemType itemType) {
    this.setChanged();
    this.updateDerivedData();
    this.notifyObservers(new BudgetEvent(itemType));
  }

  public void addObserver(Observer observer) {
    assert (ReflectionUtilities.classImplements(observer.getClass(), IBudgetObserver.class)) : VIEWER_ASSERT_MSG;
    super.addObserver(observer);

    this.changeAndNotifyObservers();
  }

  public void update(Observable o, Object arg) {
    this.changeAndNotifyObservers();
  }

  public BudgetFileContent toSerializable() {
    BudgetFileContent state = new BudgetFileContent();

    state.incomeCategories = this.incomeCategories;
    state.expenseCategories = this.expenseCategories;

    state.accounts = this.accountModel.getAccounts();

    state.incomeItems = this.incomeItems;
    state.expenseItems = this.expenseItems;

    return state;
  }

  public void fromSerializable(BudgetFileContent state) {
    // assert ReflectionUtilities.callerImplements(IBudgetController.class) :
    // CONTROLLER_ASSERT_MSG;

    if (state == null) {
      return;
    }

    this.incomeCategories = state.incomeCategories;
    this.expenseCategories = state.expenseCategories;

    this.accountModel.setAccounts(state.accounts);

    this.incomeItems = state.incomeItems;
    this.expenseItems = state.expenseItems;

    this.changeAndNotifyObservers();
  }
}
