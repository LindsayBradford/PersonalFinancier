package blacksmyth.personalfinancier.control.gui;

import java.awt.Component;
import java.util.LinkedList;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.SwingUtilities;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

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
    this.setRowSelectionAllowed(true);
    setupColumns();
    
    for(EXPENSE_ITEM_COLUMNS column : EXPENSE_ITEM_COLUMNS.values()) {
      tableColumnList.add(
          getColFromEnum(column)
      );
    }
   
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
    
    DefaultCellEditor editor = WidgetFactory.createBudgetAccountCellEditor();    

    getColFromEnum(EXPENSE_ITEM_COLUMNS.Account).setCellEditor(editor);
    
    this.getExpenseItemTableModel().addModelObserver(
        (Observer) editor.getComponent()
    );
    
    SwingUtilities.lockColumnWidth(
        getColFromEnum(EXPENSE_ITEM_COLUMNS.Account),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  private void setupAmountCol(EXPENSE_ITEM_COLUMNS thisColumn) {
    SwingUtilities.lockColumnWidth(
        getColFromEnum(thisColumn),
        SwingUtilities.getTextWidth(
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
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Category).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = WidgetFactory.createExpenseCategoryCellEditor();    
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Category).setCellEditor(editor);

    this.getExpenseItemTableModel().addModelObserver(
        (Observer) editor.getComponent()
    );

    
    SwingUtilities.lockColumnWidth(
        getColFromEnum(EXPENSE_ITEM_COLUMNS.Category),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  private void setupFrequencyCol() {
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Frequency).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = WidgetFactory.createCashFlowFrequencyCellEditor();    
    getColFromEnum(EXPENSE_ITEM_COLUMNS.Frequency).setCellEditor(editor);

    SwingUtilities.lockColumnWidth(
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
          PreferencesModel.getInstance().getPreferredBudgetFrequencyCellColor()
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
    this.getExpenseItemTableModel().addExpenseItem();

    SwingUtilities.scrollRowToVisible(
        this, 
        this.getRowCount() -1
    );

    this.selectionModel.setSelectionInterval(
        this.getRowCount() - 1, 
        this.getRowCount() - 1
    );
  }
  
  public void removeBudgetItem() {
    int row = this.getSelectedRow();
    if (row < 0) return; // nothing to do

    this.getExpenseItemTableModel().removeItem(row);
    this.selectionModel.setSelectionInterval(row, row);
  }

  public void moveSelectedItemDown() {
    int row = this.getSelectedRow();
    
    if (row < 0 || row == this.getRowCount() - 1) return; // nothing to do
    
    this.getExpenseItemTableModel().moveExpenseItemDown(row);
    this.selectionModel.setSelectionInterval(row + 1, row + 1);
    SwingUtilities.scrollRowToVisible(this, row + 1);
  }

  public void moveSelectedItemUp() {
    int row = this.getSelectedRow();
    
    if (row <= 0) return; // nothing to do

    this.getExpenseItemTableModel().moveExpenseItemUp(row);
    this.selectionModel.setSelectionInterval(row - 1, row - 1);
    SwingUtilities.scrollRowToVisible(this, row - 1);
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


