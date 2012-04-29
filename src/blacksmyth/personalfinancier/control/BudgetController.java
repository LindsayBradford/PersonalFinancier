package blacksmyth.personalfinancier.control;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.swing.JButton;

import com.google.gson.reflect.TypeToken;

import blacksmyth.personalfinancier.gui.BudgetAccountSummaryTable;
import blacksmyth.personalfinancier.gui.BudgetDetailTable;
import blacksmyth.personalfinancier.io.json.JSonAdapter;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class BudgetController {
  private static final String fileName = "test.json";
  
  private BudgetModel model;
  private BudgetDetailTable budgetDetailTable;
  private BudgetAccountSummaryTable accountSummaryTable;
  
  private JButton addItemButton;
  private JButton removeItemButton;
  
  public BudgetController (AccountModel accountModel) {
    setModel(
        new BudgetModel(accountModel)
    );
  }
  
  
  public BudgetModel getModel() {
    return this.model;
  }

  public void setModel(BudgetModel model) {
    this.model = model;
  }


  public BudgetDetailTable getDetailTable() {
    return this.budgetDetailTable;
  }
  
  public void setDetailTable(BudgetDetailTable budgetTable) {
    this.budgetDetailTable = budgetTable;
  }


  public void setAccountSummaryTable(BudgetAccountSummaryTable accountSummaryTable) {
    this.accountSummaryTable = accountSummaryTable;
  }


  public Component getAccountSummaryTable() {
    return this.accountSummaryTable;
  }

  public JButton getAddItemButton() {
    return addItemButton;
  }

  public void setAddItemButton(JButton addItemButton) {
    this.addItemButton = addItemButton;
    addItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            getModel().addBudgetItem();
          }
        }
    );
  }

  public JButton getRemoveItemButton() {
    return removeItemButton;
  }

  // TODO: enable remove button only whenever a row is selected.
  public void setRemoveItemButton(JButton removeItemButton) {
    this.removeItemButton = removeItemButton;
    removeItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            int selectedRow = budgetDetailTable.getSelectedRow();
            if (selectedRow > -1) {
              getModel().removeBudgetItem(selectedRow);
            }
            // TODO: make selection stay as near as deleted row as possible.
          }
        }
    );
  }

  public void save() {
    JSonAdapter.getInstance().save(
        this.getModel().getBudgetItems(),
        fileName
    );
  }

  @SuppressWarnings("unchecked")
  public void load() {
    Type budgetItemsType = new TypeToken<ArrayList<BudgetItem>>() {}.getType();
    this.getModel().setBudgetItems(
        (ArrayList<BudgetItem>) JSonAdapter.getInstance().load(fileName, budgetItemsType)
    );
  }
}
