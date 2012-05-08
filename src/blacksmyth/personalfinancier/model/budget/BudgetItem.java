package blacksmyth.personalfinancier.model.budget;

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;

public abstract class BudgetItem {

  protected String description;
  protected Money budgettedAmount;
  protected CashFlowFrequency frequency;
  protected Account budgetAccount;

  public BudgetItem() {
    super();
  }

  public String getDescription() {
    return description;
  }

  protected void setDescription(String description) {
    this.description = description;
  }

  public Money getBudgettedAmount() {
    return budgettedAmount;
  }

  protected void setBudgettedAmount(Money amount) {
    this.budgettedAmount = amount;
  }

  public CashFlowFrequency getFrequency() {
    return frequency;
  }

  protected void setFrequency(CashFlowFrequency frequency) {
    this.frequency = frequency;
  }

  public Account getBudgetAccount() {
    return budgetAccount;
  }

  protected void setBudgetAccount(Account account) {
    this.budgetAccount = account;
  }

}