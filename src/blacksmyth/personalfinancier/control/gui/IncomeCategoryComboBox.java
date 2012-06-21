package blacksmyth.personalfinancier.control.gui;

import java.awt.event.ActionEvent;
import java.util.Observable;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetEvent;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

@SuppressWarnings("serial")
public class IncomeCategoryComboBox extends JComboBox implements IBudgetObserver {
  private BudgetModel model;

  public void update(Observable budgetModel, Object modelArgs) {
    if (this.model == null) {
      this.model = (BudgetModel) budgetModel;
      addActionListener(this);
      ((JTextField) getEditor().getEditorComponent()).setHorizontalAlignment(JTextField.CENTER);

    }
    
    BudgetEvent event = (BudgetEvent) modelArgs;
    if (event.getItemType() == BudgetEvent.ItemType.incomeCategories || 
        event.getItemType() == BudgetEvent.ItemType.AllItems) {
      buildItemList(model);
    }
  }
  
  private void buildItemList(BudgetModel budgetModel) {
    this.removeAllItems();

    for (String incomeCategory : budgetModel.getIncomeCategories()) {
      this.addItem(incomeCategory);
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("comboBoxEdited")) {
      String newSelection = (String) this.getSelectedItem();
      if (!model.getIncomeCategories().contains(newSelection)) {
        model.addIncomeCategory(newSelection);
      }
    }
  }
}
