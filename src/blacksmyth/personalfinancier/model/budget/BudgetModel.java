/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BudgetModel extends Observable {
  private ArrayList<BudgetItem> budgetItems;
  
  public BudgetModel() {
    this.budgetItems = new ArrayList<BudgetItem>();
  }

  public void addBudgetItem(String description) {
    addBudgetItem(
        BudgetItemFactory.createBudgetItem(description)
    );
  }
  
  public void addBudgetItem(BudgetItem item) {
    this.budgetItems.add(item);
    this.notifyObservers();
  }
  
  public void removeBudgetItem(BudgetItem item) {
    this.budgetItems.remove(item);
    this.notifyObservers();
  }
  
  public List<BudgetItem> getBudgetItems() {
    return this.budgetItems;
  }
  
  public void setBudgetItems(ArrayList<BudgetItem> budgetItems) {
    this.budgetItems = budgetItems;
    this.notifyObservers();
  }
}
