package blacksmyth.personalfinancier.model.budget;

public class BudgetEvent {
  
  // TODO: Views are still doing too much work. Record indexes of changed items in the event too.
  
  public enum ItemType {IncomeItems, ExpenseItems, AllItems, incomeCategories, expenseCategories}
  
  private ItemType itemType;
  
  public BudgetEvent() {
    this.setItemType(ItemType.AllItems);
  }
  
  public BudgetEvent(ItemType itemType) {
    setItemType(itemType);
  }
  
  public ItemType getItemType() {
    return this.itemType;
  }
  
  public void setItemType(ItemType itemType) {
    this.itemType = itemType;
  }
}
