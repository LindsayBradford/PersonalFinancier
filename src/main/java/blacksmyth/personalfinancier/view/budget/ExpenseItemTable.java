/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view.budget;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


import blacksmyth.general.swing.Utilities;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.ViewPreferences;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class ExpenseItemTable extends JTable {
  private static final long serialVersionUID = 1L;
 
  private static final int CELL_BUFFER = 15;
  
  private boolean showDerivedColumns = true;
  
  private LinkedList<TableColumn> tableColumnList = new LinkedList<TableColumn>();
  
  private static final EXPENSE_ITEM_COLUMNS[] DERIVED_COLUMNS = {
    EXPENSE_ITEM_COLUMNS.Daily,
    EXPENSE_ITEM_COLUMNS.Weekly,
    EXPENSE_ITEM_COLUMNS.Fortnightly,
    EXPENSE_ITEM_COLUMNS.Monthly,
    EXPENSE_ITEM_COLUMNS.Quarterly,
    EXPENSE_ITEM_COLUMNS.Yearly,
  };

  public ExpenseItemTable(BudgetModel budgetModel) {
    super(
        new ExpenseItemTableModel(budgetModel)
    );

    getSelectionModel().setSelectionMode(
        ListSelectionModel.SINGLE_INTERVAL_SELECTION
    );
    
    // Without disabling autoStartsEdit, the DELETE key binding for removing table rows fails.
    this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
    
    setupColumns();
    
    for(EXPENSE_ITEM_COLUMNS column : EXPENSE_ITEM_COLUMNS.values()) {
      tableColumnList.add(
          getColFromEnum(column)
      );
    }
    
    Utilities.enableEnterKeyEditsInJTable(this);
   
    this.getExpenseItemTableModel().fireTableDataChanged();
  }
  
  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    
    setupCategoryCol();
    setupAmountCol(EXPENSE_ITEM_COLUMNS.Amount);
    setupFrequencyCol();
    setupAmountCol(EXPENSE_ITEM_COLUMNS.Daily);
    setupAmountCol(EXPENSE_ITEM_COLUMNS.Weekly);
    setupAmountCol(EXPENSE_ITEM_COLUMNS.Fortnightly);
    setupAmountCol(EXPENSE_ITEM_COLUMNS.Monthly);
    setupAmountCol(EXPENSE_ITEM_COLUMNS.Quarterly);
    setupAmountCol(EXPENSE_ITEM_COLUMNS.Yearly);
    setupAccountCol();
  }
  
  private void setupAccountCol() {
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Account).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );
    
    DefaultCellEditor editor = BudgetWidgetFactory.createBudgetAccountCellEditor();    

    getColFromEnum(EXPENSE_ITEM_COLUMNS.Account).setCellEditor(editor);
    
    this.getExpenseItemTableModel().addModelObserver(
        (PropertyChangeListener) editor.getComponent()
    );
    
    Utilities.lockColumnWidth(
        getColFromEnum(EXPENSE_ITEM_COLUMNS.Account),
        Utilities.getTextWidth(
            WidgetFactory.ACCOUNT_BUFFER
        ) + CELL_BUFFER
    );
  }

  private void setupAmountCol(EXPENSE_ITEM_COLUMNS thisColumn) {
    Utilities.lockColumnWidth(
        getColFromEnum(thisColumn),
        Utilities.getTextWidth(
            WidgetFactory.DECIMAL_FORMAT_PATTERN
        ) + CELL_BUFFER
    );

    getColFromEnum(thisColumn).setCellRenderer(
        WidgetFactory.createAmountCellRenderer()    
    );
    getColFromEnum(thisColumn).setCellEditor(
        WidgetFactory.createAmountCellEditor()    
    );
  }

  private void setupCategoryCol() {
    DefaultTableCellRenderer renderer = WidgetFactory.createTableCellRenderer(JTextField.CENTER); 

    getColFromEnum(EXPENSE_ITEM_COLUMNS.Category).setCellRenderer(
           renderer
    );

    DefaultCellEditor editor = BudgetWidgetFactory.createExpenseCategoryCellEditor(
        getExpenseItemTableModel().getBudgetModel()
    );    
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Category).setCellEditor(editor);

    this.getExpenseItemTableModel().addModelObserver(
        (PropertyChangeListener) editor.getComponent()
    );
    
    Utilities.lockColumnWidth(
        getColFromEnum(EXPENSE_ITEM_COLUMNS.Category),
        Utilities.getTextWidth(" Variable Essential ")
    );
  }

  private void setupFrequencyCol() {
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Frequency).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = BudgetWidgetFactory.createCashFlowFrequencyCellEditor();    
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Frequency).setCellEditor(editor);

    Utilities.lockColumnWidth(
        getColFromEnum(EXPENSE_ITEM_COLUMNS.Frequency),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  public void tableChanged(TableModelEvent e) {
    this.setVisible(false);
    super.tableChanged(e);
    this.setVisible(true);
  }
    
  private TableColumn getColFromEnum(EXPENSE_ITEM_COLUMNS thisEnum) {
    return this.getColumnModel().getColumn(thisEnum.ordinal());
  }
  
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

    Component cellRenderer = super.prepareRenderer(renderer, row, column);
    
    WidgetFactory.prepareTableCellRenderer(
        this,
        cellRenderer,
        row, 
        column
    );
    
    // One final tweak specific to this table below.  
    // when the frequency selected matches the column name, render the 
    // foreground text of the cell slightly differently.

    if (this.getColumnName(column) == this.getFrequencyAt(row).toString()) {
      cellRenderer.setForeground(
          ViewPreferences.getInstance().getPreferredBudgetFrequencyCellColor()
      );
    }
    
    return cellRenderer;
  }
  
  private CashFlowFrequency getFrequencyAt(int row) {
    return (CashFlowFrequency) this.getModel().getValueAt(
        row, 
        EXPENSE_ITEM_COLUMNS.Frequency.ordinal()
    );
  }

  public ExpenseItemTableModel getExpenseItemTableModel() {
    return (ExpenseItemTableModel) getModel();
  }

  public void addBudgetItem() {
    int row = this.getSelectedRow();
    
    // if nothing selected, but there are entries, add entry at end.
    
    if (row == -1 && this.getRowCount() > 0) {
      row = this.getRowCount() - 1; 
    }

    this.getExpenseItemTableModel().addExpenseItem(row + 1);

    Utilities.scrollRowToVisible(
        this, 
        row + 1
    );
    
    this.selectionModel.setSelectionInterval(
        row + 1, 
        row + 1
    );
    this.requestFocus();
  }
  
  public void removeBudgetItems() {
    if (this.getSelectedRowCount() == 0) return;

    this.getExpenseItemTableModel().removeItems(
      this.getSelectedRows()
    );
    this.requestFocus();
  }

  public void moveSelectedItemDown() {

    int row = this.getSelectedRow();
    
    if (row < 0 || row == this.getRowCount() - 1) return; // nothing to do
    
    this.getExpenseItemTableModel().moveExpenseItemDown(row);
    this.selectionModel.setSelectionInterval(row + 1, row + 1);
    Utilities.scrollRowToVisible(this, row + 1);
    this.requestFocus();
  }

  public void moveSelectedItemUp() {
    int row = this.getSelectedRow();
    
    if (row <= 0) return; // nothing to do

    this.getExpenseItemTableModel().moveExpenseItemUp(row);
    this.selectionModel.setSelectionInterval(row - 1, row - 1);
    Utilities.scrollRowToVisible(this, row - 1);
    this.requestFocus();
  }

  public void toggleDerivedColumnView() {
    this.setShowDerivedColumns(
      !this.isShowDerivedColumns()    
    );
  }

  public boolean isShowDerivedColumns() {
    return showDerivedColumns;
  }

  public void setShowDerivedColumns(boolean showDerivedColumns) {
    this.showDerivedColumns = showDerivedColumns;
    this.updateDerivedColumns();
  }
  
  private void updateDerivedColumns() {
    final int totalColumns = this.getColumnCount();
    for(int i = 0; i < totalColumns; i++) {
      this.removeColumn(
        this.getColumnModel().getColumn(0)    
      );
    }
    
    for(int i = 0; i < this.getModel().getColumnCount(); i++) {
      EXPENSE_ITEM_COLUMNS modelColumn = EXPENSE_ITEM_COLUMNS.values()[i];

      boolean isDerivedColumn = false;

      for(EXPENSE_ITEM_COLUMNS derivedColumn : DERIVED_COLUMNS) {
        if (derivedColumn.equals(modelColumn)) isDerivedColumn = true;
      }
      if (!isDerivedColumn) {
        this.addColumn(
            this.tableColumnList.get(i)
        );
      }
      if (isDerivedColumn && this.isShowDerivedColumns()) {
        this.addColumn(
            this.tableColumnList.get(i)
        );
      }
    }
  }

}


