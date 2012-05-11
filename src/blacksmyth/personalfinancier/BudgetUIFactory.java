/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import blacksmyth.general.FontIconProvider;
import blacksmyth.personalfinancier.control.BudgetUndoManager;
import blacksmyth.personalfinancier.control.gui.BudgetAccountSummaryTable;
import blacksmyth.personalfinancier.control.gui.BudgetCategorySummaryTable;
import blacksmyth.personalfinancier.control.gui.ExpenseItemTable;
import blacksmyth.personalfinancier.control.gui.ExpenseItemTableController;
import blacksmyth.personalfinancier.control.gui.IncomeItemTable;
import blacksmyth.personalfinancier.control.gui.IncomeItemTableController;
import blacksmyth.personalfinancier.control.gui.ResetBudgetItemsCommand;
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
        createBudgetItemToolbar(model),
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
    
    toolbar.addSeparator();
    
    JButton moveItemDownButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        moveItemDownButton, 
        FontIconProvider.icon_arrow_down
    );

    moveItemDownButton.setToolTipText(
        " Move item down in list "
    );
    
    moveItemDownButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            expenseItemTable.moveSelectedItemDown();
          }
        }
    );

    toolbar.add(moveItemDownButton);

    JButton moveItemUpButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        moveItemUpButton, 
        FontIconProvider.icon_arrow_up
    );

    moveItemUpButton.setToolTipText(
        " Move item up in list "
    );
    
    moveItemUpButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            expenseItemTable.moveSelectedItemUp();
          }
        }
    );

    toolbar.add(moveItemUpButton);
    
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
    
    toolbar.addSeparator();
    
    JButton moveItemDownButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        moveItemDownButton, 
        FontIconProvider.icon_arrow_down
    );

    moveItemDownButton.setToolTipText(
        " Move item down in list "
    );
    
    moveItemDownButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            incomeItemTable.moveSelectedItemDown();
          }
        }
    );

    toolbar.add(moveItemDownButton);

    JButton moveItemUpButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        moveItemUpButton, 
        FontIconProvider.icon_arrow_up
    );

    moveItemUpButton.setToolTipText(
        " Move item up in list "
    );
    
    moveItemUpButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            incomeItemTable.moveSelectedItemUp();
          }
        }
    );

    toolbar.add(moveItemUpButton);

    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JToolBar createBudgetItemToolbar(final BudgetModel model) {
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
            BudgetUndoManager.getInstance().addEdit(
                ResetBudgetItemsCommand.doCmd(model)
            );
          }
        }
    );

    toolbar.add(resetItemsButton);
    
    toolbar.addSeparator();

    toolbar.add(
        createUndoButton()    
    );

    toolbar.add(
        createRedoButton()    
    );

    toolbar.addSeparator();
    
    return toolbar;
  }
  
  private static JButton createUndoButton() {
    JButton button = new JButton();

    FontIconProvider.getInstance().configureButton(
        button, 
        FontIconProvider.icon_step_backward
    );
    
    button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            if (BudgetUndoManager.getInstance().canUndo()) {
              BudgetUndoManager.getInstance().undo();
            }
          }
        }
    );
    
    button.setToolTipText(" Undo ");
    return button;
  }

  private static JButton createRedoButton() {
    JButton button = new JButton();

    FontIconProvider.getInstance().configureButton(
        button, 
        FontIconProvider.icon_step_forward
    );

    button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            if (BudgetUndoManager.getInstance().canRedo()) {
              BudgetUndoManager.getInstance().redo();
            }
          }
        }
    );

    button.setToolTipText(" Redo ");
    return button;
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