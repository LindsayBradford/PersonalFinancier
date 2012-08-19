package blacksmyth.personalfinancier.control.gui;

import java.util.Observable;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetEvent;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

@SuppressWarnings("serial")
public class IncomeCategoryComboBox extends JComboBox implements IBudgetObserver {
  
  {
    ((JTextField) getEditor().getEditorComponent()).setHorizontalAlignment(
        JTextField.CENTER
    );
  }
  
  public void update(Observable budgetModel, Object modelArgs) {
    
    BudgetEvent event = (BudgetEvent) modelArgs;
    if (event.getItemType() == BudgetEvent.ItemType.incomeCategories || 
        event.getItemType() == BudgetEvent.ItemType.AllItems) {
      buildItemList((BudgetModel) budgetModel);
    }
  }
  
  private void buildItemList(BudgetModel model) {
    this.removeAllItems();

    for (String incomeCategory : model.getIncomeCategories()) {
      this.addItem(incomeCategory);
    }
  }
  
}
