package blacksmyth.personalfinancier.view.budget;

import java.util.Observable;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetEvent;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

@SuppressWarnings("serial")
public class ExpenseCategoryComboBox extends JComboBox<String> implements IBudgetObserver {
  
  {
    ((JTextField) getEditor().getEditorComponent()).setHorizontalAlignment(
        JTextField.CENTER
    );
  }

  public void update(Observable budgetModel, Object modelArgs) {
    BudgetEvent event = (BudgetEvent) modelArgs;
    if (event.getItemType() == BudgetEvent.ItemType.expenseCategories || 
        event.getItemType() == BudgetEvent.ItemType.AllItems) {
      buildItemList((BudgetModel) budgetModel);
    }
  }
  
  private void buildItemList(BudgetModel budgetModel) {
    this.removeAllItems();

    for (String expenseCategory : budgetModel.getExpenseCategories()) {
      this.addItem(expenseCategory);
    }
  }
  
}
