/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import blacksmyth.general.FontIconProvider;
import blacksmyth.general.SwingUtilities;
import blacksmyth.personalfinancier.control.BudgetUndoManager;
import blacksmyth.personalfinancier.control.command.ResetBudgetItemsCommand;
import blacksmyth.personalfinancier.control.gui.BudgetCashFlowSummaryTable;
import blacksmyth.personalfinancier.control.gui.BudgetCategorySummaryTable;
import blacksmyth.personalfinancier.control.gui.CashFlowPieChart;
import blacksmyth.personalfinancier.control.gui.CategoryPieChart;
import blacksmyth.personalfinancier.control.gui.ExpenseItemTable;
import blacksmyth.personalfinancier.control.gui.IncomeItemTable;
import blacksmyth.personalfinancier.control.gui.WidgetFactory;
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
    
    IncomeItemTable incomeItemTable = new IncomeItemTable(model);

    ExpenseItemTable expenseItemTable = new ExpenseItemTable(model);

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
    
    splitPane.setOneTouchExpandable(true);
    splitPane.setResizeWeight(0.5);
    
    return splitPane;
  }
  

  private static JPanel createExpenseItemsTablePanel(final ExpenseItemTable expenseItemTable) {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(
            WidgetFactory.createColoredTitledBorder(
                " Expense Items ",
                Color.GRAY.brighter()
            ),
            new EmptyBorder(0,3,5,4)
        )
    );
    
    panel.add(
        createExpenseItemToolbar(expenseItemTable),
        BorderLayout.PAGE_START
    );

    JScrollPane tableScrollPane =  new JScrollPane(expenseItemTable);
    
    
    panel.add(
        tableScrollPane,
        BorderLayout.CENTER
    );
  
    return panel;
  }

  private static JPanel createIncomeItemsTablePanel(final IncomeItemTable incomeItemTable) {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(
            WidgetFactory.createColoredTitledBorder(
                " Income Items ",
                Color.GRAY.brighter()
            ),
            new EmptyBorder(0,3,5,4)
        )
    );

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
    
    FontIconProvider.getInstance().setGlyphAsText(
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
    
    FontIconProvider.getInstance().setGlyphAsText(
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
    
    FontIconProvider.getInstance().setGlyphAsText(
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
    
    FontIconProvider.getInstance().setGlyphAsText(
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
    
    FontIconProvider.getInstance().setGlyphAsText(
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
    
    FontIconProvider.getInstance().setGlyphAsText(
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
    
    FontIconProvider.getInstance().setGlyphAsText(
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
    
    FontIconProvider.getInstance().setGlyphAsText(
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

    toolbar.add(
        createResetItemsButton(model)
    );
    
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

  private static JButton createResetItemsButton(final BudgetModel model) {
    AbstractAction resetItemsAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().addEdit(
            ResetBudgetItemsCommand.doCmd(model)
        );
      }
    };

    JButton resetItemsButton = new JButton(resetItemsAction);

    resetItemsButton.setForeground(
        Color.GRAY.brighter()
   );
    
    FontIconProvider.getInstance().setGlyphAsText(
        resetItemsButton, 
        FontIconProvider.icon_trash
    );

    resetItemsButton.setToolTipText(
        " Clear all income and expense items"
    );
    
    SwingUtilities.bindKeyStrokeToAction(
        resetItemsButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DELETE, 
            Event.CTRL_MASK
        ), 
        resetItemsAction
    );
    return resetItemsButton;
  }
  
  private static JButton createUndoButton() {
    AbstractAction undoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (BudgetUndoManager.getInstance().canUndo()) {
           BudgetUndoManager.getInstance().undo();
        }
      }
    };
    
    JButton button = new JButton(undoAction);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_step_backward
    );
    
    SwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Z, 
            Event.CTRL_MASK
        ), 
        undoAction
    );

    button.setToolTipText(" Undo ");

    return button;
  }

  private static JButton createRedoButton() {
    AbstractAction redoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (BudgetUndoManager.getInstance().canRedo()) {
           BudgetUndoManager.getInstance().redo();
        }
      }
    };

    JButton button = new JButton(redoAction);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_step_forward
    );

    SwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Y, 
            Event.CTRL_MASK
        ), 
        redoAction
    );

    button.setToolTipText(" Redo ");

    return button;
  }
  
  private static JComponent createBudgetSummaryPanel(BudgetModel model) {
    JPanel panel  = new JPanel(new GridLayout(1,2));

    panel.setBorder(
        new CompoundBorder(
            WidgetFactory.createColoredTitledBorder(
                " " + CashFlowFrequency.Fortnightly.toString() + " Summary ",
                Color.GRAY.brighter()
            ),
            new EmptyBorder(0,3,5,4)
        )
    );

    panel.add(
        WidgetFactory.createGraphTablePane(
          new CashFlowPieChart(model),
          createAccountSummaryTable(model) 
        )
    );

    panel.add(
        WidgetFactory.createGraphTablePane(
          createCategoryPieChart(model),
          createCategorySummaryTable(model) 
        )
    );

    return panel;    
  }

  private static JComponent createCategorySummaryTable(BudgetModel model) {
    return new JScrollPane(
        new BudgetCategorySummaryTable(model)
    );
  }
  
  private static JComponent createAccountSummaryTable(BudgetModel model) {
    return new JScrollPane(
        new BudgetCashFlowSummaryTable(model)
    );
  }
  
  private static JComponent createCategoryPieChart(BudgetModel model) {
    return new CategoryPieChart(model);
  }

}