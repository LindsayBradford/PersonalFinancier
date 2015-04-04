/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
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
import blacksmyth.general.BlacksmythSwingUtilities;
import blacksmyth.personalfinancier.control.UndoManagers;
import blacksmyth.personalfinancier.control.budget.command.ResetBudgetItemsCommand;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.view.JUndoListeningButton;
import blacksmyth.personalfinancier.view.WidgetFactory;
import blacksmyth.personalfinancier.view.budget.BudgetCashFlowSummaryTable;
import blacksmyth.personalfinancier.view.budget.BudgetCategorySummaryTable;
import blacksmyth.personalfinancier.view.budget.CashFlowPieChart;
import blacksmyth.personalfinancier.view.budget.CategoryPieChart;
import blacksmyth.personalfinancier.view.budget.ExpenseItemTable;
import blacksmyth.personalfinancier.view.budget.IncomeItemTable;

class BudgetUIFactory {
  
  private static Action BudgetItemInsertAction;
  private static Action BudgetItemDeleteAction;
  private static Action BudgetItemUpAction;
  private static Action BudgetItemDownAction;
  
  public static JComponent createBudgetComponent() {
    
    createSharedBudgetTableActions();
    
    JSplitPane splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,
        createBudgetItemPanel(), 
        createBudgetSummaryPanel()
    );
    
    splitPane.setOneTouchExpandable(true);
    splitPane.setResizeWeight(0.5);
    
    return splitPane;
  }
  
  private static void createSharedBudgetTableActions() {
    BudgetItemInsertAction = new AbstractAction("Insert Budget Item") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseTable.addBudgetItem(); 
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeTable.addBudgetItem();
        }
      }
    };
    
    BudgetItemDeleteAction = new AbstractAction("Delete Budget Item") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseTable.removeBudgetItems();
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeTable.removeBudgetItems();
        }
      }
    };
    
    BudgetItemUpAction = new AbstractAction("Move Budget Item Up") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseTable.moveSelectedItemUp(); 
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeTable.moveSelectedItemUp();
        }
      }
    };

    BudgetItemDownAction = new AbstractAction("Move Budget Item Down") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseTable.moveSelectedItemDown(); 
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeTable.moveSelectedItemDown();
        }
      }
    };
  }
  
  private static JComponent createBudgetItemPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    UIComponents.incomeTable  = new IncomeItemTable(UIComponents.budgetModel);
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
    toolbar.setFloatable(false);

    final JButton addItemButton = new JButton();
    
    addItemButton.setAction(BudgetItemInsertAction);
     
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        addItemButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_INSERT, 0
        ), 
        BudgetItemInsertAction
    );
    
    FontIconProvider.getInstance().setGlyphAsText(
        addItemButton, 
        FontIconProvider.fa_plus
    );
    
    addItemButton.setToolTipText(
        "Add a new expense item"
    );
    
    addItemButton.setForeground(
        Color.GREEN.brighter()
   );
    
    toolbar.add(addItemButton);

    JButton removeItemsButton = WidgetFactory.createMultiSelectedtRowEnabledButton(UIComponents.expenseTable);
    
    removeItemsButton.setAction(BudgetItemDeleteAction);
     
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        removeItemsButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DELETE, 0
        ), 
        BudgetItemDeleteAction
    );
    
    removeItemsButton.setEnabled(false);
    
    removeItemsButton.setForeground(
        Color.RED.brighter()
   );
    
    FontIconProvider.getInstance().setGlyphAsText(
        removeItemsButton, 
        FontIconProvider.fa_minus
    );

    removeItemsButton.setToolTipText(
        "Delete selected expense item(s)"
    );
    
    toolbar.add(removeItemsButton);
    
    toolbar.addSeparator();

    JButton moveItemDownButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.expenseTable);

    moveItemDownButton.setAction(BudgetItemDownAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        moveItemDownButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DOWN, Event.ALT_MASK
        ), 
        BudgetItemDownAction
    );
    
    moveItemDownButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        moveItemDownButton, 
        FontIconProvider.fa_arrow_down
    );
    
    moveItemDownButton.setForeground(Color.GREEN);

    moveItemDownButton.setToolTipText(
        " Move item down in list "
    );
    
    toolbar.add(moveItemDownButton);

    JButton moveItemUpButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.expenseTable);

    moveItemUpButton.setAction(BudgetItemUpAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        moveItemUpButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_UP, Event.ALT_MASK
        ), 
        BudgetItemUpAction
    );
    
    moveItemUpButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        moveItemUpButton, 
        FontIconProvider.fa_arrow_up
    );

    moveItemUpButton.setToolTipText(
        " Move item up in list "
    );

    moveItemUpButton.setForeground(Color.GREEN);

    toolbar.add(moveItemUpButton);
    
    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JToolBar createIncomeItemToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    
    final JButton addItemButton = new JButton();

     
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        addItemButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_INSERT, 0
        ), 
        BudgetItemInsertAction
    );

    addItemButton.setAction(BudgetItemInsertAction);
    
    FontIconProvider.getInstance().setGlyphAsText(
        addItemButton, 
        FontIconProvider.fa_plus
    );
    
    addItemButton.setToolTipText(
        "Add a new income item"
    );
    
    addItemButton.setForeground(
        Color.GREEN.brighter()
   );
    
    toolbar.add(addItemButton);

    JButton removeItemsButton = WidgetFactory.createMultiSelectedtRowEnabledButton(UIComponents.incomeTable);

    removeItemsButton.setAction(BudgetItemDeleteAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        removeItemsButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DELETE, 0
        ), 
        BudgetItemDeleteAction
    );

    removeItemsButton.setEnabled(false);
    
    removeItemsButton.setForeground(
        Color.RED.brighter()
   );
    
    FontIconProvider.getInstance().setGlyphAsText(
        removeItemsButton, 
        FontIconProvider.fa_minus
    );

    removeItemsButton.setToolTipText(
        "Delete selected income item(s)"
    );
    
    toolbar.add(removeItemsButton);
    
    toolbar.addSeparator();

    JButton moveItemDownButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.incomeTable);

    moveItemDownButton.setAction(BudgetItemDownAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        moveItemDownButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DOWN, Event.ALT_MASK
        ), 
        BudgetItemDownAction
    );
    
    moveItemDownButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        moveItemDownButton, 
        FontIconProvider.fa_arrow_down
    );

    moveItemDownButton.setToolTipText(
        " Move item down in list "
    );	
    
    moveItemDownButton.setForeground(Color.GREEN);

    toolbar.add(moveItemDownButton);

    JButton moveItemUpButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.incomeTable);

    moveItemUpButton.setAction(BudgetItemUpAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        moveItemUpButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_UP, Event.ALT_MASK
        ), 
        BudgetItemUpAction
    );
    
    moveItemUpButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        moveItemUpButton, 
        FontIconProvider.fa_arrow_up
    );

    moveItemUpButton.setToolTipText(
        " Move item up in list "
    );

    moveItemUpButton.setForeground(Color.GREEN);

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
	  
	  final JToggleButton button = new JToggleButton() {
      { // begin: instance initializer
        
        this.setSelected(
            PreferencesModel.getInstance().getDerivedBudgetColumsVisibility()
        );
        this.setForeground(Color.GREEN);
        changeSelectionRendering();
        
        this.addActionListener(
            new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                PreferencesModel.getInstance().toggleDerivedBudgetColumsVisibility();
                changeSelectionRendering();
              }
            }
        );
        
      } // end: instance initializer
      
      private void changeSelectionRendering() {
        if (this.isSelected()) {
          FontIconProvider.getInstance().setGlyphAsText(
              this, 
              FontIconProvider.fa_toggle_right
          );
          this.setToolTipText(
              " Hide derived amount columns. "
          );
        } else {
          FontIconProvider.getInstance().setGlyphAsText(
              this, 
              FontIconProvider.fa_toggle_left
          );
          this.setToolTipText(
              " Show derived amount columns. "
          );
        }
      }
    };
    
    final Observer derivedColumnController = 
       new Observer() { 
         {
           this.updateViewers();
         } 
            
         private void updateViewers() {
           boolean visibleBudgetColumnsState = PreferencesModel.getInstance().getDerivedBudgetColumsVisibility();
           button.setSelected(visibleBudgetColumnsState);

           if (UIComponents.incomeTable.isShowDerivedColumns() != visibleBudgetColumnsState) {
             UIComponents.incomeTable.toggleDerivedColumnView();
             UIComponents.expenseTable.toggleDerivedColumnView();
           }
         } 
          	
         public void update(Observable arg0, Object arg1) {
           updateViewers();
         }
    };
     
    PreferencesModel.getInstance().addObserver(
      derivedColumnController 	 
    );
    
    return button;
  }

  private static JButton createResetItemsButton() {
    AbstractAction resetItemsAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        UndoManagers.BUDGET_UNDO_MANAGER.addEdit(
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
        FontIconProvider.fa_trash_o
    );

    resetItemsButton.setToolTipText(
        " Clear all income and expense items"
    );
    
    resetItemsButton.setForeground(Color.RED);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        resetItemsButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DELETE, 
            0
        ), 
        resetItemsAction
    );
    return resetItemsButton;
  }
  
  private static JButton createUndoButton() {
    AbstractAction undoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (UndoManagers.BUDGET_UNDO_MANAGER.canUndo()) {
          UndoManagers.BUDGET_UNDO_MANAGER.undo();
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
    
    UndoManagers.BUDGET_UNDO_MANAGER.addObserver(button);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.fa_undo
    );
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
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
        if (UndoManagers.BUDGET_UNDO_MANAGER.canRedo()) {
          UndoManagers.BUDGET_UNDO_MANAGER.redo();
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
    
    UndoManagers.BUDGET_UNDO_MANAGER.addObserver(button);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.fa_repeat
    );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
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
            UIComponents.budgetModel,
            UIComponents.accountModel
        )
    );
  }
  
  private static JComponent createCategoryPieChart() {
    return new CategoryPieChart(UIComponents.budgetModel);
  }

}