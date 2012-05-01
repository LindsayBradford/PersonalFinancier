package blacksmyth.personalfinancier.gui;

import java.awt.Component;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.SwingUtilities;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.PreferencesModel;

enum BUDGET_DETAIL_COLUMNS {
  Category, Description, Amount, Frequency, 
  Daily, Weekly,Fortnightly, Monthly,
  Quarterly, Yearly, Account
}

public class BudgetDetailTable extends JTable {
  private static final long serialVersionUID = 1L;
  
  private static final int CELL_BUFFER = 15;

  public BudgetDetailTable(BudgetDetailTableController model) {
    super(model);
    this.setRowSelectionAllowed(true);
    setupColumns();
    this.getBudgetController().fireTableDataChanged();
  }
  
  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    
    setupCategoryCol();
    setupAmountCol(BUDGET_DETAIL_COLUMNS.Amount);
    setupFrequencyCol();
    setupAmountCol(BUDGET_DETAIL_COLUMNS.Daily);
    setupAmountCol(BUDGET_DETAIL_COLUMNS.Weekly);
    setupAmountCol(BUDGET_DETAIL_COLUMNS.Fortnightly);
    setupAmountCol(BUDGET_DETAIL_COLUMNS.Monthly);
    setupAmountCol(BUDGET_DETAIL_COLUMNS.Quarterly);
    setupAmountCol(BUDGET_DETAIL_COLUMNS.Yearly);
    setupAccountCol();
  }
  
  private void setupAccountCol() {
    getColFromEnum(BUDGET_DETAIL_COLUMNS.Account).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );
    
    DefaultCellEditor editor = WidgetFactory.createBudgetAccountCellEditor();    

    getColFromEnum(BUDGET_DETAIL_COLUMNS.Account).setCellEditor(editor);
    
    this.getBudgetController().addModelObserver(
        (Observer) editor.getComponent()
    );
    
    SwingUtilities.lockColumnWidth(
        getColFromEnum(BUDGET_DETAIL_COLUMNS.Account),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  private void setupAmountCol(BUDGET_DETAIL_COLUMNS thisColumn) {
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
    getColFromEnum(BUDGET_DETAIL_COLUMNS.Category).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = WidgetFactory.createBudgetCategoryCellEditor();    
    getColFromEnum(BUDGET_DETAIL_COLUMNS.Category).setCellEditor(editor);

    SwingUtilities.lockColumnWidth(
        getColFromEnum(BUDGET_DETAIL_COLUMNS.Category),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  private void setupFrequencyCol() {
    getColFromEnum(BUDGET_DETAIL_COLUMNS.Frequency).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = WidgetFactory.createCashFlowFrequencyCellEditor();    
    getColFromEnum(BUDGET_DETAIL_COLUMNS.Frequency).setCellEditor(editor);

    SwingUtilities.lockColumnWidth(
        getColFromEnum(BUDGET_DETAIL_COLUMNS.Frequency),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }
  
  private TableColumn getColFromEnum(BUDGET_DETAIL_COLUMNS thisEnum) {
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
    return (CashFlowFrequency) this.getModel().getValueAt(row, BUDGET_DETAIL_COLUMNS.Frequency.ordinal());
  }

  public BudgetDetailTableController getBudgetController() {
    return (BudgetDetailTableController) getModel();
  }

  public void addBudgetItem() {
    this.getBudgetController().addBudgetItem();
  }
  
  public void removeBudgetItem() {
    int row = this.getSelectedRow();
    if (row >= 0) {
      this.getBudgetController().removeItem(row);
    }
  }
}


