package blacksmyth.personalfinancier.control.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.SwingUtilities;
import blacksmyth.personalfinancier.model.budget.BudgetModel;


// TODO: Sorting from largest budgetted amount to smallest.

@SuppressWarnings("serial")
public class BudgetAccountSummaryTable extends JTable {

  enum COLUMN_HEADERS {
    Account, Detail, Budgetted
  }

  private static final int CELL_BUFFER = 15;
  
  private static final int ROW_LIMIT = 5;
  
  public BudgetAccountSummaryTable(BudgetModel budgetModel) {
    super(
        new BudgetAccountSummaryViewer(budgetModel)
    );
    setupColumns();

    this.setPreferredScrollableViewportSize(
        new Dimension(
            (int) this.getPreferredScrollableViewportSize().getWidth(),
            this.getRowHeight() * ROW_LIMIT
        )
    );
    this.setAutoCreateRowSorter(true);
  }
  
  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setupBudgettedCol();
    
    getColFromEnum(COLUMN_HEADERS.Account).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    getColFromEnum(COLUMN_HEADERS.Detail).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );
  }
  
  private void setupBudgettedCol() {
    SwingUtilities.lockColumnWidth(
        getColFromEnum(COLUMN_HEADERS.Budgetted),
        SwingUtilities.getTextWidth(
            WidgetFactory.DECIMAL_FORMAT_PATTERN
        ) + CELL_BUFFER
    );

    getColFromEnum(COLUMN_HEADERS.Budgetted).setCellRenderer(
        WidgetFactory.createAmountCellRenderer()    
    );
  }

  private TableColumn getColFromEnum(COLUMN_HEADERS thisEnum) {
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
    
    if (this.getColFromEnum(COLUMN_HEADERS.Budgetted).getModelIndex() == column) {
      BigDecimal value = (BigDecimal) this.getModel().getValueAt(row, column);
      if (value.compareTo(BigDecimal.ZERO) == -1) {
        cellRenderer.setForeground(Color.RED);
      }
      if (value.compareTo(BigDecimal.ZERO) == 1) {
        cellRenderer.setForeground(Color.GREEN);
      }
    }

    return cellRenderer;
  }
  
}