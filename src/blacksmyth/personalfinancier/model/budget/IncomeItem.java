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

public class IncomeItem extends BudgetItem {
  private IncomeCategory category;
  public IncomeItem(IncomeCategory category, String description, Money amount, CashFlowFrequency frequency, Account account) {
    this.category = category;
    this.description = description;
    this.budgettedAmount = amount;
    this.frequency = frequency;
    this.budgetAccount = account;
  }

  public IncomeCategory getCategory() {
    return this.category;
  }
  
  protected void setCategory(IncomeCategory category) {
    this.category = category;
  }

}