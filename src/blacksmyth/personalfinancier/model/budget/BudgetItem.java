/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model.budget;

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;

public class BudgetItem {
  private String description;
  private Money budgettedAmount;
  private CashFlowFrequency frequency;
  private Account budgetAccount;
  
  public BudgetItem(String description, Money amount, CashFlowFrequency frequency, Account account) {
    this.description = description;
    this.budgettedAmount = amount;
    this.frequency = frequency;
    this.budgetAccount = account;
  }
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Money getBudgettedAmount() {
    return budgettedAmount;
  }

  public void setBudgettedAmount(Money amount) {
    this.budgettedAmount = amount;
  }

  public CashFlowFrequency getFrequency() {
    return frequency;
  }

  public void setFrequency(CashFlowFrequency frequency) {
    this.frequency = frequency;
  }

  
  public Account getBudgetAccount() {
    return budgetAccount;
  }

  public void setBudgetAccount(Account account) {
    this.budgetAccount = account;
  }
}
