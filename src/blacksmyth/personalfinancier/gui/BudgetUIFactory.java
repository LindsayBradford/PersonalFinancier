/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import blacksmyth.general.ResourceBridge;
import blacksmyth.personalfinancier.control.BudgetController;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

class BudgetUIFactory {
  
  
  public static JComponent createBudgetComponent() {

    BudgetController controller = new BudgetController(new AccountModel());
    
    JPanel budgetPanel = new JPanel(new BorderLayout());
    
    budgetPanel.add(
        createBudgetItemPanel(controller), 
        BorderLayout.CENTER
    );

    budgetPanel.add(
        createBudgetSummaryPanel(controller), 
        BorderLayout.PAGE_END
    );

    return budgetPanel;
  }
  
  private static JComponent createBudgetItemPanel(BudgetController controller) {
    JPanel panel = new JPanel(new BorderLayout());
    
    BudgetDetailTable budgetTable = new BudgetDetailTable(
        controller.getModel()
    );
    
    controller.setDetailTable(budgetTable);
    
    panel.setBorder(new TitledBorder("Budget Items"));
    
    panel.add(
        createBudgetItemToolbar(controller), 
        BorderLayout.PAGE_START
    );
    
    panel.add(
        new JScrollPane(budgetTable),
        BorderLayout.CENTER
    );
    
    return panel;    
  }
  
  // TODO: eye-bending JButtons.  Make it more accessible to mortals.
  // TODO: replace JButton text with an embeeded images from a resource archive.
  
  @SuppressWarnings("serial")
  private static JToolBar createBudgetItemToolbar(BudgetController controller) {
    JToolBar toolbar = new JToolBar();
    
    controller.setAddItemButton(
        new JButton(
            ResourceBridge.getMenuIcon("plus16.png")
        )
     );
    controller.getAddItemButton().setToolTipText(
        "Add a new budget item"
    );
    
    toolbar.add(
      controller.getAddItemButton()
    );

    controller.setRemoveItemButton(
        new JButton(
            ResourceBridge.getMenuIcon("minus16.png")
        )
     );
    controller.getRemoveItemButton().setToolTipText(
        "Remove selected budget item"
    );

    toolbar.add(
        controller.getRemoveItemButton()
    );
    
    return toolbar;
  }
  
  private static JComponent createBudgetSummaryPanel(BudgetController controller) {
    JPanel panel  = new JPanel(new BorderLayout());
    
    assert controller.getModel() != null;
    controller.setAccountSummaryTable(
        new BudgetAccountSummaryTable(
            controller.getModel()
        )
    );
    
    panel.setBorder(new TitledBorder("Budget Summary"));
    
    panel.add(
        new JScrollPane(
            controller.getAccountSummaryTable()
        ),
        BorderLayout.LINE_END
    );
    
    return panel;    
  }
}