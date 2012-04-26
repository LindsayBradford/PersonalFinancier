/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model.budget;

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.MoneyAmount;

public class BudgetItem {
  private String description;
  private MoneyAmount amount;
  private CashFlowFrequency frequency;
  private Account account;
  
  public BudgetItem(String description, MoneyAmount amount, CashFlowFrequency frequency, Account account) {
    this.description = description;
    this.amount = amount;
    this.frequency = frequency;
    this.account = account;
  }
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MoneyAmount getAmount() {
    return amount;
  }

  public void setAmount(MoneyAmount amount) {
    this.amount = amount;
  }

  public CashFlowFrequency getFrequency() {
    return frequency;
  }

  public void setFrequency(CashFlowFrequency frequency) {
    this.frequency = frequency;
  }

  
  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }
}
