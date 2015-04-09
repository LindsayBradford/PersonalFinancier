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
import blacksmyth.personalfinancier.control.FileHandlerBuilder;
import blacksmyth.personalfinancier.control.UndoManagers;
import blacksmyth.personalfinancier.control.budget.command.ResetBudgetItemsCommand;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.IApplicationMessageView;
import blacksmyth.personalfinancier.view.IPersonalFinancierComponentView;
import blacksmyth.personalfinancier.view.IPersonalFinancierView;
import blacksmyth.personalfinancier.view.JUndoListeningButton;
import blacksmyth.personalfinancier.view.PersonalFinancierView;
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
  
  public static IPersonalFinancierComponentView createBudgetComponent(PersonalFinancierView view) {
    
    UIComponents.budgetModel = 
        new BudgetModel(
            new AccountModel()
        );
    
    UIComponents.budgetFileController = 
        FileHandlerBuilder.buildBudgetHandler(
            view,
            UIComponents.budgetModel
        );
    
    UIComponents.budgetFileController.addObserver(
        view.getMessageViewer()
    );
    
    createSharedBudgetTableActions();
    
    BudgetComponent newComponent = new BudgetComponent(
        JSplitPane.VERTICAL_SPLIT,
        createBudgetItemPanel(view), 
        createBudgetSummaryPanel()
    );
    
    newComponent.putClientProperty(
        "AppMessage", 
        "Manage your personal budget in this tab."
    );
    
    newComponent.putClientProperty(
        "TabName", 
        "Budget"
    );
    
    newComponent.setOneTouchExpandable(true);
    newComponent.setResizeWeight(0.5);
    
    return newComponent;
  }
  
  private static void createSharedBudgetTableActions() {

    UIComponents.LoadBudgetAction = new AbstractAction("Open...") {
      
      public void actionPerformed(ActionEvent e) {
        UndoManagers.BUDGET_UNDO_MANAGER.discardAllEdits();
        UIComponents.budgetFileController.load();
      }
    };

    UIComponents.SaveBudgetAction = new AbstractAction("Save") {
      public void actionPerformed(ActionEvent e) {
        UIComponents.budgetFileController.save();
      }
    };
    
    UIComponents.SaveAsBudgetAction = new AbstractAction("Save As...") {
      public void actionPerformed(ActionEvent e) {
        UIComponents.budgetFileController.saveAs();
      }
    };
    
    BudgetItemInsertAction = new AbstractAction("Insert Budget Item") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseItemTable.addBudgetItem(); 
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeItemTable.addBudgetItem();
        }
      }
    };
    
    BudgetItemDeleteAction = new AbstractAction("Delete Budget Item") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseItemTable.removeBudgetItems();
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeItemTable.removeBudgetItems();
        }
      }
    };
    
    BudgetItemUpAction = new AbstractAction("Move Budget Item Up") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseItemTable.moveSelectedItemUp(); 
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeItemTable.moveSelectedItemUp();
        }
      }
    };

    BudgetItemDownAction = new AbstractAction("Move Budget Item Down") {
      public void actionPerformed(ActionEvent e) {
        
        JComponent expensePanel = (JComponent) UIComponents.expenseItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(expensePanel)) {
          UIComponents.expenseItemTable.moveSelectedItemDown(); 
          return;
        }

        JComponent incomePanel = (JComponent) UIComponents.incomeItemTable.getParent().getParent().getParent();
        if (BlacksmythSwingUtilities.mouseIsOverComponent(incomePanel)) {
          UIComponents.incomeItemTable.moveSelectedItemDown();
        }
      }
    };
  }
  
  private static JButton createSaveButton(IApplicationMessageView messageBar) {
    
    JButton button = new JButton(UIComponents.SaveBudgetAction);

    button.setForeground(Color.GREEN.darker());

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_S, 
            Event.CTRL_MASK
        ), 
        UIComponents.SaveBudgetAction
    );
    
    button.setMnemonic(KeyEvent.VK_S);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.fa_save
    );

    button.setToolTipText(" Save the current budget ");
    
    button.putClientProperty("AppMessage", "Save your personal budget...");
    messageBar.bindViewComponent(button);

    return button;
  }

  private static JButton createSaveAsButton(IApplicationMessageView messageBar) {
    
    JButton button = new JButton(
        UIComponents.SaveAsBudgetAction
    );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_A, 
            Event.CTRL_MASK
        ), 
        UIComponents.SaveAsBudgetAction
    );
    
    button.setMnemonic(KeyEvent.VK_A);
    
    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.fa_save
    );
    
    button.setForeground(Color.GREEN);
    
    button.setToolTipText(" Save the budget to another filename... ");

    button.putClientProperty("AppMessage", "Save your personal budget as...");
    messageBar.bindViewComponent(button);
    
    return button;
  }

  
  private static JButton createLoadButton(IApplicationMessageView messageBar) {
    
    JButton button = new JButton(
        UIComponents.LoadBudgetAction
    );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_L, 
            Event.CTRL_MASK
        ), 
        UIComponents.LoadBudgetAction
    );

    
    button.setMnemonic(KeyEvent.VK_L);
    
    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.fa_folder_open_o
    );
    
    button.setForeground(Color.GREEN.darker());
    
    button.setToolTipText(" Load a budget ");

    button.putClientProperty("AppMessage", "Load your personal budget...");
    messageBar.bindViewComponent(button);
    
    return button;
  }


  
  private static JComponent createBudgetItemPanel(IPersonalFinancierView view) {
    JPanel panel = new JPanel(new BorderLayout());

    UIComponents.incomeItemTable  = new IncomeItemTable(UIComponents.budgetModel);
    UIComponents.incomeItemToolbar = createIncomeItemToolbar();
    
    UIComponents.expenseItemTable = new ExpenseItemTable(UIComponents.budgetModel);
    UIComponents.expenseItemToolbar = createExpenseItemToolbar();
    
    panel.add(
        createBudgetItemToolbar(view),
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
        UIComponents.expenseItemToolbar,
        BorderLayout.PAGE_START
    );

    JScrollPane tableScrollPane =  new JScrollPane(UIComponents.expenseItemTable);
    
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
        UIComponents.incomeItemToolbar,
        BorderLayout.PAGE_START
    );

    panel.add(
        new JScrollPane(UIComponents.incomeItemTable),
        BorderLayout.CENTER
    );
    
    return panel;
  }
  
  @SuppressWarnings("serial")
  private static JToolBar createExpenseItemToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    
    toolbar.add(
        createAddExpenseItemButton()
    );
    
    toolbar.add(
        createDeleteExpenseItemsButton()
    );
    
    toolbar.addSeparator();
    
    toolbar.add(
        createMoveExpenseItemDownButton()
    );

    toolbar.add(
        createMoveExpenseItemUpButton()
    );
    
    return toolbar;
  }
  
  private static JButton createAddExpenseItemButton() {
    final JButton newButton  = new JButton();
    
    newButton.setAction(BudgetItemInsertAction);
     
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_INSERT, 0
        ), 
        BudgetItemInsertAction
    );
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_plus
    );
    
    newButton.setToolTipText(
        "Add a new expense item"
    );
    
    newButton.setForeground(
        Color.GREEN.brighter()
   );
    
    return newButton;
  }
  
  private static JButton createDeleteExpenseItemsButton() {
    JButton newButton = WidgetFactory.createMultiSelectedtRowEnabledButton(UIComponents.expenseItemTable);
    
    newButton.setAction(BudgetItemDeleteAction);
     
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DELETE, 0
        ), 
        BudgetItemDeleteAction
    );
    
    newButton.setEnabled(false);
    
    newButton.setForeground(
        Color.RED.brighter()
   );
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_minus
    );

    newButton.setToolTipText(
        "Delete selected expense item(s)"
    );
    
    return newButton;
  }
  
  private static JButton createMoveExpenseItemDownButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.expenseItemTable);

    newButton.setAction(BudgetItemDownAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DOWN, Event.ALT_MASK
        ), 
        BudgetItemDownAction
    );
    
    newButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_arrow_down
    );
    
    newButton.setForeground(Color.GREEN);

    newButton.setToolTipText(
        " Move item down in list "
    );
    
    return newButton;
  }
  
  private static JButton createMoveExpenseItemUpButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.expenseItemTable);

    newButton.setAction(BudgetItemUpAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_UP, Event.ALT_MASK
        ), 
        BudgetItemUpAction
    );
    
    newButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_arrow_up
    );

    newButton.setToolTipText(
        " Move item up in list "
    );

    newButton.setForeground(Color.GREEN);

    return newButton;
  }
  

  @SuppressWarnings("serial")
  private static JToolBar createIncomeItemToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    
    toolbar.add(
        createAddIncomeItemButton()
    );
    
    toolbar.add(
        createRemoveIncomeItemsButton()
    );
    
    toolbar.addSeparator();

    toolbar.add(
        createMoveIncomeItemDownButton()
    );

    toolbar.add(
        createMoveIncomeItemUpButton()
    );

    return toolbar;
  }
  
  private static JButton createAddIncomeItemButton() {
    final JButton newButton = new JButton();
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_INSERT, 0
        ), 
        BudgetItemInsertAction
    );

    newButton.setAction(BudgetItemInsertAction);
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_plus
    );
    
    newButton.setToolTipText(
        "Add a new income item"
    );
    
    newButton.setForeground(
        Color.GREEN.brighter()
   );
    
    return newButton;
  }
  
  private static JButton createRemoveIncomeItemsButton() {
    JButton newButton = WidgetFactory.createMultiSelectedtRowEnabledButton(UIComponents.incomeItemTable);

    newButton.setAction(BudgetItemDeleteAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DELETE, 0
        ), 
        BudgetItemDeleteAction
    );

    newButton.setEnabled(false);
    
    newButton.setForeground(
        Color.RED.brighter()
   );
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_minus
    );

    newButton.setToolTipText(
        "Delete selected income item(s)"
    );
    
    return newButton;
  }
  
  private static JButton createMoveIncomeItemDownButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.incomeItemTable);

    newButton.setAction(BudgetItemDownAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_DOWN, Event.ALT_MASK
        ), 
        BudgetItemDownAction
    );
    
    newButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_arrow_down
    );

    newButton.setToolTipText(
        " Move item down in list "
    );  
    
    newButton.setForeground(Color.GREEN);

    return newButton;
  }
  
  private static JButton createMoveIncomeItemUpButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(UIComponents.incomeItemTable);

    newButton.setAction(BudgetItemUpAction);
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        newButton, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_UP, Event.ALT_MASK
        ), 
        BudgetItemUpAction
    );
    
    newButton.setEnabled(false);
    
    FontIconProvider.getInstance().setGlyphAsText(
        newButton, 
        FontIconProvider.fa_arrow_up
    );

    newButton.setToolTipText(
        " Move item up in list "
    );

    newButton.setForeground(Color.GREEN);

    return newButton;
  }

  @SuppressWarnings("serial")
  private static JToolBar createBudgetItemToolbar(IPersonalFinancierView view) {

    JToolBar toolbar = new JToolBar();

    toolbar.add(
        createLoadButton(
            view.getMessageViewer()
        )   
    );

    toolbar.add(
        createSaveButton(
            view.getMessageViewer()
        )   
    );

    toolbar.add(
        createSaveAsButton(
            view.getMessageViewer()
        )   
    );

    toolbar.addSeparator();
    
    toolbar.add(
        createUndoButton()    
    );

    toolbar.add(
        createRedoButton()    
    );

    toolbar.addSeparator();

    toolbar.add(
        createBudgetItemButtonsVisibileButton()
    );
    
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
  
  private static JToggleButton createBudgetItemButtonsVisibileButton() {
    
    final JToggleButton button = new JToggleButton() {
      { // begin: instance initializer
        
        this.setSelected(
            PreferencesModel.getInstance().getBudgetItemButtonsVisibility()
        );
        this.setForeground(Color.GREEN);
        changeSelectionRendering();
        
        this.addActionListener(
            new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                PreferencesModel.getInstance().toggleBudgetItemButtonsVisibility();
                changeSelectionRendering();
              }
            }
        );
        
      } // end: instance initializer
      
      private void changeSelectionRendering() {
        if (this.isSelected()) {
          FontIconProvider.getInstance().setGlyphAsText(
              this, 
              FontIconProvider.fa_check_square_o
          );
          this.setToolTipText(
              " Hide budget item table buttons. "
          );
        } else {
          FontIconProvider.getInstance().setGlyphAsText(
              this, 
              FontIconProvider.fa_square_o
          );
          this.setToolTipText(
              " Show budget item table buttons. "
          );
        }
      }
    };
    
    final Observer budgetButtonsController = 
       new Observer() { 
         {
           this.updateViewers();
         } 
            
         private void updateViewers() {
           boolean visibleBudgetItemButtonsState = PreferencesModel.getInstance().getBudgetItemButtonsVisibility();
           button.setSelected(visibleBudgetItemButtonsState);

           if (UIComponents.incomeItemToolbar.isVisible() != visibleBudgetItemButtonsState) {
             boolean visibleFlag = !UIComponents.incomeItemToolbar.isVisible();
             UIComponents.incomeItemToolbar.setVisible(visibleFlag);
             UIComponents.expenseItemToolbar.setVisible(visibleFlag);
           }
         } 
            
         public void update(Observable arg0, Object arg1) {
           updateViewers();
         }
    };
     
    PreferencesModel.getInstance().addObserver(
        budgetButtonsController    
    );
    
    return button;
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

           if (UIComponents.incomeItemTable.isShowDerivedColumns() != visibleBudgetColumnsState) {
             UIComponents.incomeItemTable.toggleDerivedColumnView();
             UIComponents.expenseItemTable.toggleDerivedColumnView();
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
            UIComponents.budgetModel
        )
    );
  }
  
  private static JComponent createCategoryPieChart() {
    return new CategoryPieChart(UIComponents.budgetModel);
  }
}

final class BudgetComponent extends JSplitPane implements IPersonalFinancierComponentView {

  public BudgetComponent(int verticalSplit, JComponent budgetItemPanel, JComponent budgetSummaryPanel) {
    super(verticalSplit, budgetItemPanel, budgetSummaryPanel);
  }
}