package blacksmyth.personalfinancier.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class AccountModel extends Observable {
  private ArrayList<Account> accounts;
  
  public AccountModel() {
    super();
    mockAccounts();
  }

  private void mockAccounts() {
    ArrayList<Account> tmpAccounts = new ArrayList<Account>();
    tmpAccounts.add(Account.DEFAULT);

    tmpAccounts.add(
        new Account(
            "General", 
            "Here is a general account",
            true
        )
    );

    tmpAccounts.add(
        new Account(
            "Bills",
            "This is a bills account",
            true
        )
    );

    tmpAccounts.add(
        new Account(
            "Savings",
            "A dedicated savings account",
            true
        )
    );

    tmpAccounts.add(
        new Account(
            "Education",
            "An account to set aside education costs",
            true
        )
    );
    
    tmpAccounts.add(
        new Account(
            "Super Fund",
            "My Superanuation Fund",
            false
        )
    );
    
    this.setAccounts(tmpAccounts);
  }

  public ArrayList<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(ArrayList<Account> accounts) {
    this.accounts = accounts;
    this.changeAndNotifyObservers();
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

  public ArrayList<Account> getBudgetAccounts() {
    ArrayList<Account> budgetAccounts = new ArrayList<Account>();
    for (Account account : this.getAccounts()) {
      if (account.isBudgetAccount()) {
        budgetAccounts.add(account);
      }
    }
    return budgetAccounts;
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
