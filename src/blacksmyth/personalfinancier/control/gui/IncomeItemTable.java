package blacksmyth.personalfinancier.control.gui;

import java.awt.Component;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.SwingUtilities;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class IncomeItemTable extends JTable {
  private static final long serialVersionUID = 1L;
  
  private static final int CELL_BUFFER = 15;
  
  public IncomeItemTable(BudgetModel budgetModel) {
    super(
        new IncomeItemTableModel(budgetModel)
    );
    this.setRowSelectionAllowed(true);
    setupColumns();

    this.getIncomeItemTableModel().fireTableDataChanged();
  }
  
  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    
    setupCategoryCol();
    setupAmountCol(INCOME_ITEM_COLUMNS.Amount);
    setupFrequencyCol();
    setupAmountCol(INCOME_ITEM_COLUMNS.Daily);
    setupAmountCol(INCOME_ITEM_COLUMNS.Weekly);
    setupAmountCol(INCOME_ITEM_COLUMNS.Fortnightly);
    setupAmountCol(INCOME_ITEM_COLUMNS.Monthly);
    setupAmountCol(INCOME_ITEM_COLUMNS.Quarterly);
    setupAmountCol(INCOME_ITEM_COLUMNS.Yearly);
    setupAccountCol();
  }
  
  private void setupAccountCol() {
    getColFromEnum(INCOME_ITEM_COLUMNS.Account).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );
    
    DefaultCellEditor editor = WidgetFactory.createBudgetAccountCellEditor();    

    getColFromEnum(INCOME_ITEM_COLUMNS.Account).setCellEditor(editor);
    
    this.getIncomeItemTableModel().addModelObserver(
        (Observer) editor.getComponent()
    );
    
    SwingUtilities.lockColumnWidth(
        getColFromEnum(INCOME_ITEM_COLUMNS.Account),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  private void setupAmountCol(INCOME_ITEM_COLUMNS thisColumn) {
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
    getColFromEnum(INCOME_ITEM_COLUMNS.Category).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = WidgetFactory.createIncomeCategoryCellEditor();    
    getColFromEnum(INCOME_ITEM_COLUMNS.Category).setCellEditor(editor);

    SwingUtilities.lockColumnWidth(
        getColFromEnum(INCOME_ITEM_COLUMNS.Category),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  private void setupFrequencyCol() {
    getColFromEnum(INCOME_ITEM_COLUMNS.Frequency).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = WidgetFactory.createCashFlowFrequencyCellEditor();    
    getColFromEnum(INCOME_ITEM_COLUMNS.Frequency).setCellEditor(editor);

    SwingUtilities.lockColumnWidth(
        getColFromEnum(INCOME_ITEM_COLUMNS.Frequency),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }
  
  private TableColumn getColFromEnum(INCOME_ITEM_COLUMNS thisEnum) {
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
        INCOME_ITEM_COLUMNS.Frequency.ordinal()
    );
  }

  public IncomeItemTableModel getIncomeItemTableModel() {
    return (IncomeItemTableModel) getModel();
  }

  public void addBudgetItem() {
    this.getIncomeItemTableModel().addIncomeItem();

    SwingUtilities.scrollRowToVisible(
        this, 
        this.getRowCount() - 1
    );
    
    this.selectionModel.setSelectionInterval(
        this.getRowCount() - 1, 
        this.getRowCount() - 1
    );
  }
  
  public void removeBudgetItem() {
    int row = this.getSelectedRow();
    
    if (row < 0) return; // nothing to do

    this.getIncomeItemTableModel().removeItem(row);
    this.selectionModel.setSelectionInterval(row, row);
  }

  public void moveSelectedItemDown() {
    int row = this.getSelectedRow();
    
    if (row < 0 || row == this.getRowCount() - 1) return; // nothing to do
    
    this.getIncomeItemTableModel().moveIncomeItemDown(row);
    this.selectionModel.setSelectionInterval(row + 1, row + 1);
    SwingUtilities.scrollRowToVisible(this, row + 1);
  }

  public void moveSelectedItemUp() {
    int row = this.getSelectedRow();
    
    if (row <= 0) return; // nothing to do

    this.getIncomeItemTableModel().moveIncomeItemUp(row);
    this.selectionModel.setSelectionInterval(row - 1, row - 1);
    SwingUtilities.scrollRowToVisible(this, row - 1);
  }
}


