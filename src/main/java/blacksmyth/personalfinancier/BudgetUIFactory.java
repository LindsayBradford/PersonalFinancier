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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

import blacksmyth.general.file.IFileHandler;
import blacksmyth.general.swing.Utilities;
import blacksmyth.general.swing.FontIconProvider;
import blacksmyth.personalfinancier.control.FileHandlerBuilder;
import blacksmyth.personalfinancier.control.budget.command.AddAccountCommand;

import blacksmyth.personalfinancier.control.budget.command.ResetBudgetItemsCommand;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.budget.BudgetFileContent;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.IApplicationMessageView;
import blacksmyth.personalfinancier.view.IPersonalFinancierComponentView;
import blacksmyth.personalfinancier.view.IPersonalFinancierView;
import blacksmyth.personalfinancier.view.JUndoListeningButton;
import blacksmyth.personalfinancier.view.PersonalFinancierView;
import blacksmyth.personalfinancier.view.ViewPreferences;
import blacksmyth.personalfinancier.view.WidgetFactory;
import blacksmyth.personalfinancier.view.budget.BudgetCashFlowSummaryTable;
import blacksmyth.personalfinancier.view.budget.BudgetExpenseCategorySummaryTable;
import blacksmyth.personalfinancier.view.budget.BudgetIncomeCategorySummaryTable;
import blacksmyth.personalfinancier.view.budget.CashFlowDonutChart;
import blacksmyth.personalfinancier.view.budget.CategoryDonutCharts;
import blacksmyth.personalfinancier.view.budget.ExpenseItemTable;
import blacksmyth.personalfinancier.view.budget.IncomeItemTable;

class BudgetUIFactory {

  private static BudgetModel budgetModel;

  private static JPanel expenseItemPanel;
  private static JToolBar expenseItemToolbar;
  private static ExpenseItemTable expenseItemTable;

  private static JPanel incomeItemPanel;
  private static JToolBar incomeItemToolbar;
  private static IncomeItemTable incomeItemTable;

  private static IFileHandler<BudgetFileContent> budgetFileController;

  private static Action LoadBudgetAction;
  private static Action SaveBudgetAction;
  private static Action SaveAsBudgetAction;

  private static Action BudgetItemInsertAction;
  private static Action BudgetItemDeleteAction;
  private static Action BudgetItemUpAction;
  private static Action BudgetItemDownAction;
  
  private static Action AddAccountAction;

  public static IPersonalFinancierComponentView createComponent(PersonalFinancierView view) {

    budgetModel = new BudgetModel(new AccountModel());

    budgetFileController = FileHandlerBuilder.buildBudgetHandler(view, budgetModel);

    budgetFileController.addObserver(view.getMessageViewer());

    createSharedBudgetTableActions();

    BudgetComponent newComponent = new BudgetComponent(
        JSplitPane.VERTICAL_SPLIT, 
        createBudgetItemPanel(view),
        createBudgetSummaryPanel()
    );

    newComponent.putClientProperty("AppMessage", "Manage your personal budget in this tab.");

    newComponent.putClientProperty("TabName", "Budget");

    newComponent.setOneTouchExpandable(true);
    newComponent.setResizeWeight(0.5);

    return newComponent;
  }

  @SuppressWarnings("serial")
  private static void createSharedBudgetTableActions() {

    LoadBudgetAction = new AbstractAction("Open...") {

      public void actionPerformed(ActionEvent e) {
        budgetModel.getUndoManager().discardAllEdits();
        budgetFileController.load();
      }
    };

    SaveBudgetAction = new AbstractAction("Save") {
      public void actionPerformed(ActionEvent e) {
        budgetFileController.save();
      }
    };

    SaveAsBudgetAction = new AbstractAction("Save As...") {
      public void actionPerformed(ActionEvent e) {
        budgetFileController.saveAs();
      }
    };

    BudgetItemInsertAction = new AbstractAction("Insert Budget Item") {
      public void actionPerformed(ActionEvent e) {

        if (Utilities.mouseIsOverComponent(expenseItemPanel)) {
          expenseItemTable.addBudgetItem();
          return;
        }

        if (Utilities.mouseIsOverComponent(incomeItemPanel)) {
          incomeItemTable.addBudgetItem();
        }
      }
    };

    BudgetItemDeleteAction = new AbstractAction("Delete Budget Item") {
      public void actionPerformed(ActionEvent e) {

        if (Utilities.mouseIsOverComponent(expenseItemPanel)) {
          expenseItemTable.removeBudgetItems();
          return;
        }

        if (Utilities.mouseIsOverComponent(incomeItemPanel)) {
          incomeItemTable.removeBudgetItems();
        }
      }
    };

    BudgetItemUpAction = new AbstractAction("Move Budget Item Up") {
      public void actionPerformed(ActionEvent e) {

        if (Utilities.mouseIsOverComponent(expenseItemPanel)) {
          expenseItemTable.moveSelectedItemUp();
          return;
        }

        if (Utilities.mouseIsOverComponent(incomeItemPanel)) {
          incomeItemTable.moveSelectedItemUp();
        }
      }
    };

    BudgetItemDownAction = new AbstractAction("Move Budget Item Down") {
      public void actionPerformed(ActionEvent e) {

        if (Utilities.mouseIsOverComponent(expenseItemPanel)) {
          expenseItemTable.moveSelectedItemDown();
          return;
        }

        if (Utilities.mouseIsOverComponent(incomeItemPanel)) {
          incomeItemTable.moveSelectedItemDown();
        }
      }
    };
  }

  private static JButton createSaveButton(IApplicationMessageView messageBar) {

    JButton button = new JButton(SaveBudgetAction);

    button.setForeground(Color.GREEN.darker());

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
        SaveBudgetAction);

    button.setMnemonic(KeyEvent.VK_S);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_save);

    button.setToolTipText(" Save the current budget ");

    button.putClientProperty("AppMessage", "Save your personal budget...");
    messageBar.bindViewComponent(button);

    return button;
  }

  private static JButton createSaveAsButton(IApplicationMessageView messageBar) {

    JButton button = new JButton(SaveAsBudgetAction);

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK),
        SaveAsBudgetAction);

    button.setMnemonic(KeyEvent.VK_A);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_save);

    button.setForeground(Color.GREEN);

    button.setToolTipText(" Save the budget to another filename... ");

    button.putClientProperty("AppMessage", "Save your personal budget as...");
    messageBar.bindViewComponent(button);

    return button;
  }

  private static JButton createLoadButton(IApplicationMessageView messageBar) {

    JButton button = new JButton(LoadBudgetAction);

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK),
        LoadBudgetAction);

    button.setMnemonic(KeyEvent.VK_L);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_folder_open_o);

    button.setForeground(Color.GREEN.darker());

    button.setToolTipText(" Load a budget ");

    button.putClientProperty("AppMessage", "Load your personal budget...");
    messageBar.bindViewComponent(button);

    return button;
  }

  private static JComponent createBudgetItemPanel(IPersonalFinancierView view) {
    JPanel panel = new JPanel(new BorderLayout());

    incomeItemTable = new IncomeItemTable(budgetModel);
    incomeItemToolbar = createIncomeItemToolbar();

    expenseItemTable = new ExpenseItemTable(budgetModel);
    expenseItemToolbar = createExpenseItemToolbar();

    panel.add(createBudgetItemToolbar(view), BorderLayout.PAGE_START);

    panel.add(createBudgetItemsTablePanel(), BorderLayout.CENTER);

    return panel;
  }

  private static JSplitPane createBudgetItemsTablePanel() {
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createIncomeItemsTablePanel(),
        createExpenseItemsTablePanel());

    splitPane.setOneTouchExpandable(true);
    splitPane.setResizeWeight(0.5);

    return splitPane;
  }

  private static JPanel createExpenseItemsTablePanel() {
    expenseItemPanel = new JPanel(new BorderLayout());

    expenseItemPanel
        .setBorder(new CompoundBorder(WidgetFactory.createColoredTitledBorder(" Expense Items ", Color.GRAY.brighter()),
            new EmptyBorder(0, 3, 5, 4)));

    expenseItemPanel.add(expenseItemToolbar, BorderLayout.NORTH);

    JScrollPane tableScrollPane = new JScrollPane(expenseItemTable);

    expenseItemPanel.add(tableScrollPane, BorderLayout.CENTER);

    Utilities.bindKeyStrokeToAction(expenseItemPanel, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0),
        BudgetItemInsertAction);

    Utilities.bindKeyStrokeToAction(expenseItemPanel, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
        BudgetItemDeleteAction);

    Utilities.bindKeyStrokeToAction(expenseItemPanel,
        KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK), BudgetItemDownAction);

    Utilities.bindKeyStrokeToAction(expenseItemPanel,
        KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK), BudgetItemUpAction);

    return expenseItemPanel;
  }

  private static JPanel createIncomeItemsTablePanel() {
    incomeItemPanel = new JPanel(new BorderLayout());

    incomeItemPanel.setBorder(new CompoundBorder(
        WidgetFactory.createColoredTitledBorder(" Income Items ", Color.GRAY.brighter()), new EmptyBorder(0, 3, 5, 4)));

    incomeItemPanel.add(incomeItemToolbar, BorderLayout.NORTH);

    incomeItemPanel.add(new JScrollPane(incomeItemTable), BorderLayout.CENTER);

    Utilities.bindKeyStrokeToAction(incomeItemPanel, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0),
        BudgetItemInsertAction);

    Utilities.bindKeyStrokeToAction(incomeItemPanel, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
        BudgetItemDeleteAction);

    Utilities.bindKeyStrokeToAction(incomeItemPanel,
        KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK), BudgetItemDownAction);

    Utilities.bindKeyStrokeToAction(incomeItemPanel,
        KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK), BudgetItemUpAction);

    return incomeItemPanel;
  }

  private static JToolBar createExpenseItemToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);

    toolbar.add(createAddExpenseItemButton());

    toolbar.add(createDeleteExpenseItemsButton());

    toolbar.addSeparator();

    toolbar.add(createMoveExpenseItemDownButton());

    toolbar.add(createMoveExpenseItemUpButton());

    return toolbar;
  }

  private static JButton createAddExpenseItemButton() {
    final JButton newButton = new JButton();

    newButton.setAction(BudgetItemInsertAction);

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_plus);

    newButton.setToolTipText("Add a new expense item");

    newButton.setForeground(Color.GREEN.brighter());

    return newButton;
  }

  private static JButton createDeleteExpenseItemsButton() {
    JButton newButton = WidgetFactory.createMultiSelectedtRowEnabledButton(expenseItemTable);

    newButton.setAction(BudgetItemDeleteAction);

    newButton.setEnabled(false);

    newButton.setForeground(Color.RED.brighter());

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_minus);

    newButton.setToolTipText("Delete selected expense item(s)");

    return newButton;
  }

  private static JButton createMoveExpenseItemDownButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(expenseItemTable);

    newButton.setAction(BudgetItemDownAction);

    newButton.setEnabled(false);

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_arrow_down);

    newButton.setForeground(Color.GREEN);

    newButton.setToolTipText(" Move item down in list ");

    return newButton;
  }

  private static JButton createMoveExpenseItemUpButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(expenseItemTable);

    newButton.setAction(BudgetItemUpAction);

    newButton.setEnabled(false);

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_arrow_up);

    newButton.setToolTipText(" Move item up in list ");

    newButton.setForeground(Color.GREEN);

    return newButton;
  }

  private static JToolBar createIncomeItemToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);

    toolbar.add(createAddIncomeItemButton());

    toolbar.add(createRemoveIncomeItemsButton());

    toolbar.addSeparator();

    toolbar.add(createMoveIncomeItemDownButton());

    toolbar.add(createMoveIncomeItemUpButton());

    return toolbar;
  }

  private static JButton createAddIncomeItemButton() {
    final JButton newButton = new JButton();

    newButton.setAction(BudgetItemInsertAction);

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_plus);

    newButton.setToolTipText("Add a new income item");

    newButton.setForeground(Color.GREEN.brighter());

    return newButton;
  }

  private static JButton createRemoveIncomeItemsButton() {
    JButton newButton = WidgetFactory.createMultiSelectedtRowEnabledButton(incomeItemTable);

    newButton.setAction(BudgetItemDeleteAction);

    newButton.setEnabled(false);

    newButton.setForeground(Color.RED.brighter());

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_minus);

    newButton.setToolTipText("Delete selected income item(s)");

    return newButton;
  }

  private static JButton createMoveIncomeItemDownButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(incomeItemTable);

    newButton.setAction(BudgetItemDownAction);

    newButton.setEnabled(false);

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_arrow_down);

    newButton.setToolTipText(" Move item down in list ");

    newButton.setForeground(Color.GREEN);

    return newButton;
  }

  private static JButton createMoveIncomeItemUpButton() {
    JButton newButton = WidgetFactory.createOneSelectedtRowEnabledButton(incomeItemTable);

    newButton.setAction(BudgetItemUpAction);

    newButton.setEnabled(false);

    Utilities.setGlyphAsText(newButton, FontIconProvider.FontIcon.fa_arrow_up);

    newButton.setToolTipText(" Move item up in list ");

    newButton.setForeground(Color.GREEN);

    return newButton;
  }

  private static JToolBar createBudgetItemToolbar(IPersonalFinancierView view) {

    JToolBar toolbar = new JToolBar();

    toolbar.add(createLoadButton(view.getMessageViewer()));

    toolbar.add(createSaveButton(view.getMessageViewer()));

    toolbar.add(createSaveAsButton(view.getMessageViewer()));

    toolbar.addSeparator();

    toolbar.add(createUndoButton());

    toolbar.add(createRedoButton());

    toolbar.addSeparator();

    toolbar.add(createBudgetItemButtonsVisibileButton());

    toolbar.add(createDerivedColumnsVisibileButton());

    toolbar.addSeparator();

    toolbar.add(createResetItemsButton());

    toolbar.addSeparator();

    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JToggleButton createBudgetItemButtonsVisibileButton() {

    final JToggleButton button = new JToggleButton() {
      { // begin: instance initializer

        this.setSelected(ViewPreferences.getInstance().getBudgetItemButtonsVisibility());
        this.setForeground(Color.GREEN);
        changeSelectionRendering();

        this.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            ViewPreferences.getInstance().toggleBudgetItemButtonsVisibility();
            changeSelectionRendering();
          }
        });

      } // end: instance initializer

      private void changeSelectionRendering() {
        if (this.isSelected()) {
          Utilities.setGlyphAsText(this, FontIconProvider.FontIcon.fa_check_square_o);
          this.setToolTipText(" Hide budget item table buttons. ");
        } else {
          Utilities.setGlyphAsText(this, FontIconProvider.FontIcon.fa_square_o);
          this.setToolTipText(" Show budget item table buttons. ");
        }
      }
    };

    final PropertyChangeListener budgetButtonsController = new PropertyChangeListener() {
      {
        this.updateViewers();
      }

      private void updateViewers() {
        boolean visibleBudgetItemButtonsState = ViewPreferences.getInstance().getBudgetItemButtonsVisibility();
        button.setSelected(visibleBudgetItemButtonsState);

        if (incomeItemToolbar.isVisible() != visibleBudgetItemButtonsState) {
          boolean visibleFlag = !incomeItemToolbar.isVisible();
          incomeItemToolbar.setVisible(visibleFlag);
          expenseItemToolbar.setVisible(visibleFlag);
        }
      }

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        updateViewers();
      }
    };

    ViewPreferences.getInstance().addObserver(budgetButtonsController);

    return button;
  }

  @SuppressWarnings("serial")
  private static JToggleButton createDerivedColumnsVisibileButton() {

    final JToggleButton button = new JToggleButton() {
      { // begin: instance initializer

        this.setSelected(ViewPreferences.getInstance().getDerivedBudgetColumsVisibility());
        this.setForeground(Color.GREEN);
        changeSelectionRendering();

        this.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            ViewPreferences.getInstance().toggleDerivedBudgetColumsVisibility();
            changeSelectionRendering();
          }
        });

      } // end: instance initializer

      private void changeSelectionRendering() {
        if (this.isSelected()) {
          Utilities.setGlyphAsText(this, FontIconProvider.FontIcon.fa_toggle_right);
          this.setToolTipText(" Hide derived amount columns. ");
        } else {
          Utilities.setGlyphAsText(this, FontIconProvider.FontIcon.fa_toggle_left);
          this.setToolTipText(" Show derived amount columns. ");
        }
      }
    };

    final PropertyChangeListener derivedColumnController = new PropertyChangeListener() {
      {
        this.updateViewers();
      }

      private void updateViewers() {
        boolean visibleBudgetColumnsState = ViewPreferences.getInstance().getDerivedBudgetColumsVisibility();
        button.setSelected(visibleBudgetColumnsState);

        if (incomeItemTable.isShowDerivedColumns() != visibleBudgetColumnsState) {
          incomeItemTable.toggleDerivedColumnView();
          expenseItemTable.toggleDerivedColumnView();
        }
      }

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        updateViewers();
      }
    };

    ViewPreferences.getInstance().addObserver(derivedColumnController);

    return button;
  }

  @SuppressWarnings("serial")
  private static JButton createResetItemsButton() {
    AbstractAction resetItemsAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        budgetModel.getUndoManager().addEdit(ResetBudgetItemsCommand.doCmd(budgetModel));
      }
    };

    JButton resetItemsButton = new JButton(resetItemsAction);

    resetItemsButton.setForeground(Color.GRAY.brighter());

    Utilities.setGlyphAsText(resetItemsButton, FontIconProvider.FontIcon.fa_trash_o);

    resetItemsButton.setToolTipText(" Clear all income and expense items");

    resetItemsButton.setForeground(Color.RED);

    Utilities.bindKeyStrokeToAction(resetItemsButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
        resetItemsAction);
    return resetItemsButton;
  }

  @SuppressWarnings("serial")
  private static JButton createUndoButton() {
    AbstractAction undoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (budgetModel.getUndoManager().canUndo()) {
          budgetModel.getUndoManager().undo();
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

    budgetModel.getUndoManager().addObserver(button);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_undo);

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK),
        undoAction);

    button.setForeground(Color.GREEN);

    button.setToolTipText(" Undo ");

    return button;
  }

  @SuppressWarnings("serial")
  private static JButton createRedoButton() {
    AbstractAction redoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (budgetModel.getUndoManager().canRedo()) {
          budgetModel.getUndoManager().redo();
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

    budgetModel.getUndoManager().addObserver(button);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_repeat);

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK),
        redoAction);

    button.setForeground(Color.GREEN);

    button.setToolTipText(" Redo ");

    return button;
  }

  private static JComponent createBudgetSummaryPanel() {
    JPanel panel = new JPanel(new GridLayout(1, 2));

    panel.setBorder(
        new CompoundBorder(
            WidgetFactory.createColoredTitledBorder(
                " " + CashFlowFrequency.Fortnightly.toString() + " Summary ", Color.GRAY.brighter()
            ),
            new EmptyBorder(0, 3, 5, 4)
        )
    );
    
    bindGraphTableComponents(
      panel, 
      new CashFlowDonutChart(budgetModel), 
      createAccountSummaryTable()
    );

    bindGraphTableComponents(
        panel, 
        createCategoryDonutChart(), 
        createCategorySummaryTables()
    );

    return panel;
  }
  
  private static void bindGraphTableComponents(JPanel panel, JComponent graphComponent, JComponent tableComponent) {
    panel.add(
        WidgetFactory.createGraphTablePane(
            graphComponent, 
            tableComponent
        )
    );
  }
  
  private static JComponent createCategorySummaryTables() {
    JPanel panel = new JPanel(new GridLayout(1, 2));
   
    panel.add(createIncomeCategorySummaryTable());
    panel.add(createExpenseCategorySummaryTable());
    
    return panel;
  }

  private static JComponent createIncomeCategorySummaryTable() {
    BudgetIncomeCategorySummaryTable table = new BudgetIncomeCategorySummaryTable(budgetModel);
    return new JScrollPane(table);
  }
  
  private static JComponent createExpenseCategorySummaryTable() {
    BudgetExpenseCategorySummaryTable table = new BudgetExpenseCategorySummaryTable(budgetModel);
    return new JScrollPane(table);
  }


  @SuppressWarnings("serial")
  private static JComponent createAccountSummaryTable() {
    BudgetCashFlowSummaryTable table = new BudgetCashFlowSummaryTable(budgetModel);
    
    AddAccountAction = new AbstractAction("Add Account") {
      public void actionPerformed(ActionEvent e) {
        
        budgetModel.getUndoManager().addEdit(
            AddAccountCommand.doCmd(
                budgetModel.getAccountModel()
            )
        );
      }
    };
    
    Utilities.bindKeyStrokeToAction(
        table, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0),
        AddAccountAction
    );
    
    return new JScrollPane(table);
  }

  private static JComponent createCategoryDonutChart() {
    return new CategoryDonutCharts(budgetModel);
  }
}

@SuppressWarnings("serial")
final class BudgetComponent extends JSplitPane implements IPersonalFinancierComponentView {

  public BudgetComponent(int verticalSplit, JComponent budgetItemPanel, JComponent budgetSummaryPanel) {
    super(verticalSplit, budgetItemPanel, budgetSummaryPanel);
    this.setOneTouchExpandable(true);
  }
}