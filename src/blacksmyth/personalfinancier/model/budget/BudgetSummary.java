package blacksmyth.personalfinancier.model.budget;

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyAmountFactory;
import blacksmyth.personalfinancier.model.MoneyUtilties;

public class BudgetSummary {
  
  private Account budgetAccount;
  private Money   budgettedAmount;
  private CashFlowFrequency budgettedFrequency;
  
  public BudgetSummary(Account account) {
    assert account.isBudgetAccount();
    this.budgetAccount = account;
    this.budgettedAmount = MoneyAmountFactory.createAmount(0);
    // TODO: drive frequency off preferences.
    this.budgettedFrequency = CashFlowFrequency.Monthly;
  }
  
  public Account getBudgetAccount() {
    return budgetAccount;
  }
  
  protected void setBudgetAccount(Account budgetAccount) {
    this.budgetAccount = budgetAccount;
  }
  
  public Money getBudgettedAmount() {
    return budgettedAmount;
  }
  protected void setBudgettedAmount(Money budgettedAmount) {
    this.budgettedAmount = budgettedAmount;
  }
  
  public String getAccountNickname() {
    return budgetAccount.getNickname();
  }
  
  public String getAccountDetail() {
    // TODO: pad out account detail
    return budgetAccount.getNickname();
  }

  public CashFlowFrequency getBudgettedFrequency() {
    return budgettedFrequency;
  }

  public void setBudgettedFrequency(CashFlowFrequency budgettedFrequency) {
    this.budgettedFrequency = budgettedFrequency;
  }

  public Object getBudgettedAmount(CashFlowFrequency newFrequency) {
    return MoneyUtilties.convertFrequencyAmount(
        this.getBudgettedAmount().getTotal(), 
        getBudgettedFrequency(), 
        newFrequency
     );
  }
}
