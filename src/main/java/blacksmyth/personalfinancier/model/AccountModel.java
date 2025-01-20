/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.personalfinancier.control.budget.IBudgetController;

public class AccountModel {
  private static final String CONTROLLER_ASSERT_MSG = "Caller does not implement IBudgetController.";
  // private static final String VIEWER_ASSERT_MSG = "Caller does not implement BudgetObserver.";

  private ArrayList<Account> accounts;

  private PropertyChangeSupport support;
  
  public AccountModel() {
    support = new PropertyChangeSupport(this);
    addDefaultAccount();
  }

  private void addDefaultAccount() {
    ArrayList<Account> tmpAccounts = new ArrayList<Account>();

    tmpAccounts.add(Account.DEFAULT);

    this.setAccounts(tmpAccounts);
  }

  public ArrayList<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(ArrayList<Account> accounts) {
    this.accounts = accounts;
    // this.changeAndNotifyObservers();
  }

  public Account getBudgetAccount(String nickname) {
    Account account = getAccount(nickname);
    if (account.isBudgetAccount()) {
      return account;
    }
    return Account.DEFAULT;
  }

  public Account getAccount(String nickname) {
    for (Account account : this.getAccounts()) {
      if (account.getNickname().equals(nickname)) {
        return account;
      }
    }
    return Account.DEFAULT;
  }

  public int getAccountIndex(String nickname) {
    for (Account account : this.getAccounts()) {
      if (account.getNickname().equals(nickname)) {
        return accounts.indexOf(account);
      }
    }
    return -1;
  }

  public Account getAccount(int index) {
    return this.accounts.get(index);
  }

  public void setAccountNickname(int index, String newNickname) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.accounts.size());
    this.accounts.get(index).setNickname(newNickname);
    this.changeAndNotifyObservers();
  }

  public void setAccountDetail(int index, String newDetail) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < this.accounts.size());
    this.accounts.get(index).setDetail(newDetail);
    this.changeAndNotifyObservers();
  }

  public ArrayList<Account> getBudgetAccounts() {
    ArrayList<Account> budgetAccounts = new ArrayList<Account>();
    for (Account account : this.accounts) {
      if (account.isBudgetAccount()) {
        budgetAccounts.add(account);
      }
    }
    return budgetAccounts;
  }

  public Account addAccount() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;

    Account newAccount = Account.Create();

    this.addAccount(newAccount);

    return newAccount;
  }

  public void addAccount(Account account) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.accounts.add(account);
    this.changeAndNotifyObservers();
  }

  public void removeAccount(Account account) {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    this.accounts.remove(account);
    this.changeAndNotifyObservers();
  }

  private void changeAndNotifyObservers() {
    support.firePropertyChange("Account Changed", null,null);
  }

  public void addObserver(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
    this.changeAndNotifyObservers();
  }
}
