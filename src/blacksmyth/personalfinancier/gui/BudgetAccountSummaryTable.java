package blacksmyth.personalfinancier.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.SwingUtilities;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

enum BUDGET_SUMMARY_COLUMNS {
  Account, Detail, Budgetted
}

// TODO: Sorting from largest budgetted amount to smallest.

@SuppressWarnings("serial")
public class BudgetAccountSummaryTable extends JTable {
  private static final int CELL_BUFFER = 15;
  
  private static final int ROW_LIMIT = 5;
  
  public BudgetAccountSummaryTable(BudgetModel budgetModel) {
    super(
        new BudgetAccountSummaryController(budgetModel)
    );
    setupColumns();

    this.setPreferredScrollableViewportSize(
        new Dimension(
            (int) this.getPreferredScrollableViewportSize().getWidth(),
            this.getRowHeight() * ROW_LIMIT
        )
    );
    
  }
  
  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setupBudgettedCol();
    
    getColFromEnum(BUDGET_SUMMARY_COLUMNS.Account).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    getColFromEnum(BUDGET_SUMMARY_COLUMNS.Detail).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );
  }
  
  private void setupBudgettedCol() {
    SwingUtilities.lockColumnWidth(
        getColFromEnum(BUDGET_SUMMARY_COLUMNS.Budgetted),
        SwingUtilities.getTextWidth(
            WidgetFactory.DECIMAL_FORMAT_PATTERN
        ) + CELL_BUFFER
    );

    getColFromEnum(BUDGET_SUMMARY_COLUMNS.Budgetted).setCellRenderer(
        WidgetFactory.createAmountCellRenderer()    
    );
  }

  private TableColumn getColFromEnum(BUDGET_SUMMARY_COLUMNS thisEnum) {
    return this.getColumnModel().getColumn(thisEnum.ordinal());
  }
  
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

    Component cellRenderer = super.prepareRenderer(renderer, row, column);
    
    WidgetFactory.prepareTableCellRenderer(
      cellRenderer,
      row, 
      column, 
      this.getModel().isCellEditable(row, column)
    );
    
     return cellRenderer;
  }
  
}