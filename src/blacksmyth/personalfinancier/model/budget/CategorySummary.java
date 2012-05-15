package blacksmyth.personalfinancier.model.budget;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyAmountFactory;
import blacksmyth.personalfinancier.model.MoneyUtilties;

public class CategorySummary implements Comparable<CategorySummary> {
  
  private String budgetCategory;
  private Money   budgettedAmount;
  private CashFlowFrequency budgettedFrequency;
  
  public CategorySummary(String category) {
    this.budgetCategory = category;
    this.budgettedAmount = MoneyAmountFactory.createAmount(0);
    // TODO: drive frequency off preferences.
    this.budgettedFrequency = CashFlowFrequency.Fortnightly;
  }
  
  public String getBudgetCategory() {
    return this.budgetCategory;
  }
  
  protected void setBudgetCategory(String category) {
    this.budgetCategory = category;
  }
  
  public Money getBudgettedAmount() {
    return budgettedAmount;
  }
  
  protected void setBudgettedAmount(Money budgettedAmount) {
    this.budgettedAmount = budgettedAmount;
  }
  
  public CashFlowFrequency getBudgettedFrequency() {
    return budgettedFrequency;
  }

  public void setBudgettedFrequency(CashFlowFrequency budgettedFrequency) {
    this.budgettedFrequency = budgettedFrequency;
  }

  public Object getBudgettedAmountAtFrequency(CashFlowFrequency newFrequency) {
    return MoneyUtilties.convertFrequencyAmount(
        this.getBudgettedAmount().getTotal(), 
        getBudgettedFrequency(), 
        newFrequency
     );
  }


  @Override
  public int compareTo(CategorySummary otherCategory) {
    // TODO: This needs to be more intelligent.
    // Right now, comparison of CategorySummary instances
    // is on budgeted amount total, regardless of exchange rates.
    return this.getBudgettedAmount().getTotal().compareTo(
        otherCategory.getBudgettedAmount().getTotal()
    );
  }
}
