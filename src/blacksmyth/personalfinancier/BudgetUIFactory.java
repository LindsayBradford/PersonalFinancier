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
import javax.swing.JToggleButton;
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
import blacksmyth.personalfinancier.control.gui.JUndoListeningButton;
import blacksmyth.personalfinancier.control.gui.WidgetFactory;
import blacksmyth.personalfinancier.model.CashFlowFrequency;

class BudgetUIFactory {
  
  public static JComponent createBudgetComponent() {
    JSplitPane splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,
        createBudgetItemPanel(), 
        createBudgetSummaryPanel()
    );
    
    splitPane.setOneTouchExpandable(true);
    splitPane.setResizeWeight(0.5);
    
    return splitPane;
  }
  
  private static JComponent createBudgetItemPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    
    UIComponents.incomeTable = new IncomeItemTable(UIComponents.budgetModel);

    UIComponents.expenseTable = new ExpenseItemTable(UIComponents.budgetModel);

    panel.add(
        createBudgetItemToolbar(),
        BorderLayout.PAGE_START
    );
    
    panel.add(
        createBudgetItemsTablePanel(),
        BorderLayout.CENTER
    );

    return panel;    
  }
  
  private static JSplitPane createBudgetItemsTablePanel() {
    JSplitPane splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,
        createIncomeItemsTablePanel(),
        createExpenseItemsTablePanel()
    );
    
    splitPane.setOneTouchExpandable(true);
    splitPane.setResizeWeight(0.5);
    
    return splitPane;
  }

  private static JPanel createExpenseItemsTablePanel() {
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
        createExpenseItemToolbar(),
        BorderLayout.PAGE_START
    );

    JScrollPane tableScrollPane =  new JScrollPane(UIComponents.expenseTable);
    
    panel.add(
        tableScrollPane,
        BorderLayout.CENTER
    );
  
    return panel;
  }

  private static JPanel createIncomeItemsTablePanel() {
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
        createIncomeItemToolbar(),
        BorderLayout.PAGE_START
    );

    panel.add(
        new JScrollPane(UIComponents.incomeTable),
        BorderLayout.CENTER
    );
    
    return panel;
  }
  
  @SuppressWarnings("serial")
  private static JToolBar createExpenseItemToolbar() {
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
            UIComponents.expenseTable.addBudgetItem();
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
            UIComponents.expenseTable.removeBudgetItem();
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
    
    moveItemDownButton.setForeground(Color.GREEN);

    moveItemDownButton.setToolTipText(
        " Move item down in list "
    );
    
    moveItemDownButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            UIComponents.expenseTable.moveSelectedItemDown();
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

    moveItemUpButton.setForeground(Color.GREEN);

    moveItemUpButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            UIComponents.expenseTable.moveSelectedItemUp();
          }
        }
    );

    toolbar.add(moveItemUpButton);
    
    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JToolBar createIncomeItemToolbar() {
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
            UIComponents.incomeTable.addBudgetItem();
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
            UIComponents.incomeTable.removeBudgetItem();
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
    
    moveItemDownButton.setForeground(Color.GREEN);
    
    moveItemDownButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            UIComponents.incomeTable.moveSelectedItemDown();
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

    moveItemUpButton.setForeground(Color.GREEN);

    moveItemUpButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            UIComponents.incomeTable.moveSelectedItemUp();
          }
        }
    );

    toolbar.add(moveItemUpButton);

    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JToolBar createBudgetItemToolbar() {

    JToolBar toolbar = new JToolBar();

    toolbar.add(
        createUndoButton()    
    );

    toolbar.add(
        createRedoButton()    
    );

    toolbar.addSeparator();
    
    toolbar.add(
        createDerivedColumnsVisibileButton()
    );

    toolbar.addSeparator();

    toolbar.add(
        createResetItemsButton()
    );
    
    toolbar.addSeparator();

    return toolbar;
  }
  
  private static JToggleButton createDerivedColumnsVisibileButton() {

    return new JToggleButton() {
      { // begin: instance initializer
        
        this.setSelected(false);
        this.setForeground(Color.GREEN);
        setIconGlyph(FontIconProvider.icon_filter);

        this.addActionListener(
            new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                UIComponents.incomeTable.toggleDerivedColumnView();
                UIComponents.expenseTable.toggleDerivedColumnView();
              }
            }
        );  
      } // end: instance initializer
      
      private void setIconGlyph(char glyph) {
        FontIconProvider.getInstance().setGlyphAsText(
            this, 
            glyph
        );
      }
    };
  }

  private static JButton createResetItemsButton() {
    AbstractAction resetItemsAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().addEdit(
            ResetBudgetItemsCommand.doCmd(
                UIComponents.budgetModel
            )
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
    
    resetItemsButton.setForeground(Color.RED);
    
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
    
    JUndoListeningButton button = new JUndoListeningButton(undoAction) {
      
      protected void handleCantUndoState() {
        this.setEnabled(false);
      }
      
      protected void handleCanUndoState() {
        this.setEnabled(true);
      }
    };
    
    BudgetUndoManager.getInstance().addObserver(button);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_undo
    );
    
    SwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Z, 
            Event.CTRL_MASK
        ), 
        undoAction
    );
    
    button.setForeground(Color.GREEN);

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

    JUndoListeningButton button = new JUndoListeningButton(redoAction) {
      
      protected void handleCantRedoState() {
        this.setEnabled(false);
      }
      
      protected void handleCanRedoState() {
        this.setEnabled(true);
      }
    };
    
    BudgetUndoManager.getInstance().addObserver(button);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_repeat
    );

    SwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Y, 
            Event.CTRL_MASK
        ), 
        redoAction
    );
    
    button.setForeground(Color.GREEN);

    button.setToolTipText(" Redo ");

    return button;
  }
  
  private static JComponent createBudgetSummaryPanel() {
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
          new CashFlowPieChart(UIComponents.budgetModel),
          createAccountSummaryTable() 
        )
    );

    panel.add(
        WidgetFactory.createGraphTablePane(
          createCategoryPieChart(),
          createCategorySummaryTable() 
        )
    );

    return panel;    
  }

  private static JComponent createCategorySummaryTable() {
    return new JScrollPane(
        new BudgetCategorySummaryTable(
            UIComponents.budgetModel
        )
    );
  }
  
  private static JComponent createAccountSummaryTable() {
    return new JScrollPane(
        new BudgetCashFlowSummaryTable(
            UIComponents.budgetModel
        )
    );
  }
  
  private static JComponent createCategoryPieChart() {
    return new CategoryPieChart(UIComponents.budgetModel);
  }

}