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
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.Utilities;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

@SuppressWarnings("serial")
public class BudgetExpenseCategorySummaryTable extends JTable {

  private static final int CELL_BUFFER = 15;
  
  private static final int ROW_LIMIT = 5;
  
  public BudgetExpenseCategorySummaryTable(BudgetModel budgetModel) {
    super(
        new BudgetExpenseCategorySummaryTableModel(budgetModel)
    );
    
    setupColumns();

    this.setPreferredScrollableViewportSize(
        new Dimension(
            (int) this.getPreferredScrollableViewportSize().getWidth(),
            this.getRowHeight() * ROW_LIMIT
        )
    );
    this.setAutoCreateRowSorter(false);
  }

  public void tableChanged(TableModelEvent e) {
    this.setVisible(false);
    super.tableChanged(e);
    this.setVisible(true);
  }

  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setupBudgettedCol();
    
    getColFromEnum(EXPENSE_CATEGORY_SUMMARY_COLUMNS.ExpenseCategory).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );
  }
  
  private void setupBudgettedCol() {
    Utilities.lockColumnWidth(
        getColFromEnum(EXPENSE_CATEGORY_SUMMARY_COLUMNS.Amount),
        Utilities.getTextWidth(
            WidgetFactory.DECIMAL_FORMAT_PATTERN
        ) + CELL_BUFFER
    );

    getColFromEnum(EXPENSE_CATEGORY_SUMMARY_COLUMNS.Amount).setCellRenderer(
        WidgetFactory.createAmountCellRenderer()    
    );
  }

  private TableColumn getColFromEnum(EXPENSE_CATEGORY_SUMMARY_COLUMNS thisEnum) {
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
    
    if (this.getModel().getValueAt(row, column) == null) {
      return cellRenderer;
    }
    
    if (this.getColFromEnum(EXPENSE_CATEGORY_SUMMARY_COLUMNS.Amount).getModelIndex() == column) {
      Utilities.renderCellBasedOnValue(
          cellRenderer,             
          Utilities.getTableBigDecimalAt(this, row, column)
      );
    }
    
    if (Utilities.rowIsLastInTable(row, this)) {
      if (this.getColFromEnum(EXPENSE_CATEGORY_SUMMARY_COLUMNS.ExpenseCategory).getModelIndex() == column) {
        ((JLabel) cellRenderer).setHorizontalAlignment(DefaultListCellRenderer.RIGHT);
        
        Utilities.renderCellBasedOnValue(
            cellRenderer, 
            Utilities.getTableBigDecimalAt(this, row, column + 1)
        );
      }
      
      return WidgetFactory.wrapComponentInTopLineBorder(cellRenderer);
    }

    return cellRenderer;
  }
  
  public BudgetCashFlowSummaryTableModel getTableModel() {
    return (BudgetCashFlowSummaryTableModel) getModel();
  }
  
}