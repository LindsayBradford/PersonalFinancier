package blacksmyth.personalfinancier.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import blacksmyth.personalfinancier.model.AccountModel;

public class PersonalFinancierController {

  private JButton loadButton;
  private JButton saveButton;
  
  private BudgetController budgetController;
  
  public PersonalFinancierController() {
    this.budgetController = new BudgetController(new AccountModel());
  }

  public BudgetController getBudgetController() {
    return budgetController;
  }

  public void setBudgetController(BudgetController budgetController) {
    this.budgetController = budgetController;
  }
  
  
  public void save() {
    this.budgetController.save();
  }
  
  public void load() {
    this.budgetController.load();
  }

  public JButton getLoadButton() {
    return loadButton;
  }

  public void setLoadButton(JButton loadButton) {
    this.loadButton = loadButton;

    final PersonalFinancierController controller = this;
     
    loadButton.addActionListener(
        new ActionListener() {

          public void actionPerformed(ActionEvent event) {
            controller.load();
          }
        }
    );
  }

  public JButton getSaveButton() {
    return saveButton;
  }

  public void setSaveButton(JButton saveButton) {
    this.saveButton = saveButton;

    final PersonalFinancierController controller = this;

    saveButton.addActionListener(
        new ActionListener() {

          public void actionPerformed(ActionEvent event) {
            controller.save();
          }
        }
    );
  }

}
