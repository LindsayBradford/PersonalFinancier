/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import blacksmyth.general.ResourceBridge;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

class BudgetUIFactory {
  
  public static JComponent createBudgetComponent(BudgetModel model) {
    
    JPanel budgetPanel = new JPanel(new BorderLayout());
    
    budgetPanel.add(
        createBudgetItemPanel(model), 
        BorderLayout.CENTER
    );

    budgetPanel.add(
        createBudgetSummaryPanel(model), 
        BorderLayout.PAGE_END
    );

    return budgetPanel;
  }
  
  private static JComponent createBudgetItemPanel(BudgetModel model) {
    JPanel panel = new JPanel(new BorderLayout());
    
    BudgetDetailTable budgetTable = new BudgetDetailTable(
        new BudgetDetailTableController(
            model
        )
     );
    
    panel.setBorder(new TitledBorder("Budget Items"));
    
    panel.add(
        createBudgetItemToolbar(budgetTable), 
        BorderLayout.PAGE_START
    );
    
    panel.add(
        new JScrollPane(budgetTable),
        BorderLayout.CENTER
    );
    
    return panel;    
  }
  
  @SuppressWarnings("serial")
  private static JToolBar createBudgetItemToolbar(final BudgetDetailTable budgetTable) {
    JToolBar toolbar = new JToolBar();
    
    JButton addItemButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        addItemButton, 
        FontIconProvider.icon_plus
    );
    
    addItemButton.setToolTipText(
        "Add a new budget item"
    );
    
    addItemButton.setForeground(
        Color.GREEN.brighter()
   );
    
    addItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            budgetTable.addBudgetItem();
          }
        }
    );

    toolbar.add(addItemButton);

    JButton removeItemButton = new JButton();

    removeItemButton.setForeground(
        Color.RED.brighter()
   );
    
    FontIconProvider.getInstance().configureButton(
        removeItemButton, 
        FontIconProvider.icon_minus
    );

    removeItemButton.setToolTipText(
        "Remove selected budget item"
    );
    
    removeItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            budgetTable.removeBudgetItem();
          }
        }
    );

    toolbar.add(removeItemButton);
    
    toolbar.addSeparator();

    JButton resetItemsButton = new JButton();

    resetItemsButton.setForeground(
        Color.GRAY.brighter()
   );
    
    FontIconProvider.getInstance().configureButton(
        resetItemsButton, 
        FontIconProvider.icon_trash
    );

    resetItemsButton.setToolTipText(
        " Clear Budget Items"
    );
    
    resetItemsButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            budgetTable.resetBudgetItems();
          }
        }
    );

    toolbar.add(resetItemsButton);

    
    return toolbar;
  }
  
  private static JComponent createBudgetSummaryPanel(BudgetModel model) {
    JPanel panel  = new JPanel(new BorderLayout());

    panel.setBorder(new TitledBorder("Budget Summary"));

    BudgetCategorySummaryTable categorySummaryTable = new BudgetCategorySummaryTable(model);

    panel.add(
        new JScrollPane(
            categorySummaryTable
        ),
        BorderLayout.LINE_START
    );
    
    BudgetAccountSummaryTable accountSummaryTable = new BudgetAccountSummaryTable(model);

    panel.add(
        new JScrollPane(
            accountSummaryTable
        ),
        BorderLayout.LINE_END
    );
    
    return panel;    
  }
}