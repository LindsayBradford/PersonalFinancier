/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
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
    
    IncomeItemTable incomeItemTable = new IncomeItemTable(
        new IncomeItemTableController(
            model
        )
     );

    ExpenseItemTable expenseItemTable = new ExpenseItemTable(
        new ExpenseItemTableController(
            model
        )
     );

    panel.add(
        createBudgetItemToolbar(expenseItemTable, incomeItemTable),
        BorderLayout.PAGE_START
    );
    
    panel.add(
        createBudgetItemsTablePanel(expenseItemTable, incomeItemTable),
        BorderLayout.CENTER
    );

    return panel;    
  }
  
  private static JSplitPane createBudgetItemsTablePanel(final ExpenseItemTable expenseItemTable, final IncomeItemTable incomeItemTable) {
    JSplitPane splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,
        createIncomeItemsTablePanel(incomeItemTable),
        createExpenseItemsTablePanel(expenseItemTable)
    );
    
    return splitPane;
  }

  private static JPanel createExpenseItemsTablePanel(final ExpenseItemTable expenseItemTable) {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(new TitledBorder("Expense Items"));
    
    panel.add(
        createExpenseItemToolbar(expenseItemTable),
        BorderLayout.PAGE_START
    );

    panel.add(
        new JScrollPane(expenseItemTable),
        BorderLayout.CENTER
    );
  
    return panel;
  }

  private static JPanel createIncomeItemsTablePanel(final IncomeItemTable incomeItemTable) {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(new TitledBorder("Income Items"));
    
    panel.add(
        createIncomeItemToolbar(incomeItemTable),
        BorderLayout.PAGE_START
    );

    panel.add(
        new JScrollPane(incomeItemTable),
        BorderLayout.CENTER
    );
    
    return panel;
  }

  @SuppressWarnings("serial")
  private static JToolBar createExpenseItemToolbar(final ExpenseItemTable expenseItemTable) {
    JToolBar toolbar = new JToolBar();
    
    JButton addItemButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        addItemButton, 
        FontIconProvider.icon_plus
    );
    
    addItemButton.setToolTipText(
        "Add a new expense item"
    );
    
    addItemButton.setForeground(
        Color.GREEN.brighter()
   );
    
    addItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            expenseItemTable.addBudgetItem();
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
        "Remove selected expense item"
    );
    
    removeItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            expenseItemTable.removeBudgetItem();
          }
        }
    );

    toolbar.add(removeItemButton);
    
    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JToolBar createIncomeItemToolbar(final IncomeItemTable incomeItemTable) {
    JToolBar toolbar = new JToolBar();
    
    JButton addItemButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        addItemButton, 
        FontIconProvider.icon_plus
    );
    
    addItemButton.setToolTipText(
        "Add a new income item"
    );
    
    addItemButton.setForeground(
        Color.GREEN.brighter()
   );
    
    addItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            incomeItemTable.addBudgetItem();
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
        "Remove selected income item"
    );
    
    removeItemButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            incomeItemTable.removeBudgetItem();
          }
        }
    );

    toolbar.add(removeItemButton);
    
    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JToolBar createBudgetItemToolbar(final ExpenseItemTable expenseItemTable, final IncomeItemTable incomeItemTable) {
    JToolBar toolbar = new JToolBar();

    JButton resetItemsButton = new JButton();

    resetItemsButton.setForeground(
        Color.GRAY.brighter()
   );
    
    FontIconProvider.getInstance().configureButton(
        resetItemsButton, 
        FontIconProvider.icon_trash
    );

    resetItemsButton.setToolTipText(
        " Clear all income and expense items"
    );
    
    resetItemsButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            expenseItemTable.resetBudgetItems();
            incomeItemTable.resetBudgetItems();
          }
        }
    );

    toolbar.add(resetItemsButton);
    
    return toolbar;
  }
  
  private static JComponent createBudgetSummaryPanel(BudgetModel model) {
    JPanel panel  = new JPanel(new BorderLayout());

    panel.setBorder(new TitledBorder("Budget " + CashFlowFrequency.Fortnightly.toString() + " Summary"));

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