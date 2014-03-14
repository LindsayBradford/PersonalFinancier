/**
 * (c) 2012, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.view.budget;

import java.awt.Component;
import java.util.LinkedList;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.BlacksmythSwingUtilities;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class IncomeItemTable extends JTable {
  
  private boolean showDerivedColumns = true;
  
  private LinkedList<TableColumn> tableColumnList = new LinkedList<TableColumn>();

  private static final INCOME_ITEM_COLUMNS[] DERIVED_COLUMNS = {
    INCOME_ITEM_COLUMNS.Daily,
    INCOME_ITEM_COLUMNS.Weekly,
    INCOME_ITEM_COLUMNS.Fortnightly,
    INCOME_ITEM_COLUMNS.Monthly,
    INCOME_ITEM_COLUMNS.Quarterly,
    INCOME_ITEM_COLUMNS.Yearly,
  };
  
  private static final long serialVersionUID = 1L;
  
  private static final int CELL_BUFFER = 15;
  
  public IncomeItemTable(BudgetModel budgetModel) {
    super(
        new IncomeItemTableModel(budgetModel)
    );
    this.setRowSelectionAllowed(true);
    setupColumns();
    
    for(INCOME_ITEM_COLUMNS column : INCOME_ITEM_COLUMNS.values()) {
      tableColumnList.add(
          getColFromEnum(column)
      );
    }

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
    
    DefaultCellEditor editor = BudgetWidgetFactory.createBudgetAccountCellEditor();    

    getColFromEnum(INCOME_ITEM_COLUMNS.Account).setCellEditor(editor);
    
    this.getIncomeItemTableModel().addModelObserver(
        (Observer) editor.getComponent()
    );
    
    BlacksmythSwingUtilities.lockColumnWidth(
        getColFromEnum(INCOME_ITEM_COLUMNS.Account),
        BlacksmythSwingUtilities.getTextWidth(
            WidgetFactory.ACCOUNT_BUFFER
        ) + CELL_BUFFER
    );
  }

  private void setupAmountCol(INCOME_ITEM_COLUMNS thisColumn) {
    BlacksmythSwingUtilities.lockColumnWidth(
        getColFromEnum(thisColumn),
        BlacksmythSwingUtilities.getTextWidth(
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

    DefaultCellEditor editor = BudgetWidgetFactory.createIncomeCategoryCellEditor(
        getIncomeItemTableModel().getBudgetModel()
    );
    
    this.getIncomeItemTableModel().addModelObserver(
        (Observer) editor.getComponent()
    );

    getColFromEnum(INCOME_ITEM_COLUMNS.Category).setCellEditor(editor);

    BlacksmythSwingUtilities.lockColumnWidth(
        getColFromEnum(INCOME_ITEM_COLUMNS.Category),
        BlacksmythSwingUtilities.getTextWidth(" Variable Essential ")
    );
  }

  private void setupFrequencyCol() {
    getColFromEnum(INCOME_ITEM_COLUMNS.Frequency).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    DefaultCellEditor editor = BudgetWidgetFactory.createCashFlowFrequencyCellEditor();    
    getColFromEnum(INCOME_ITEM_COLUMNS.Frequency).setCellEditor(editor);

    BlacksmythSwingUtilities.lockColumnWidth(
        getColFromEnum(INCOME_ITEM_COLUMNS.Frequency),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }
  
  public void tableChanged(TableModelEvent e) {
    this.setVisible(false);
    super.tableChanged(e);
    this.setVisible(true);
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

    BlacksmythSwingUtilities.scrollRowToVisible(
        this, 
        this.getRowCount() - 1
    );
    
    this.selectionModel.setSelectionInterval(
        this.getRowCount() - 1, 
        this.getRowCount() - 1
    );
  }
  
  public void removeBudgetItems() {
    this.getIncomeItemTableModel().removeItems(
	  this.getSelectedRows()
	);
  }

  public void moveSelectedItemDown() {
    int row = this.getSelectedRow();
    
    if (row < 0 || row == this.getRowCount() - 1) return; // nothing to do
    
    this.getIncomeItemTableModel().moveIncomeItemDown(row);
    this.selectionModel.setSelectionInterval(row + 1, row + 1);
    BlacksmythSwingUtilities.scrollRowToVisible(this, row + 1);
  }

  public void moveSelectedItemUp() {
    int row = this.getSelectedRow();
    
    if (row <= 0) return; // nothing to do

    this.getIncomeItemTableModel().moveIncomeItemUp(row);
    this.selectionModel.setSelectionInterval(row - 1, row - 1);
    BlacksmythSwingUtilities.scrollRowToVisible(this, row - 1);
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
      INCOME_ITEM_COLUMNS modelColumn = INCOME_ITEM_COLUMNS.values()[i];

      boolean isDerivedColumn = false;

      for(INCOME_ITEM_COLUMNS derivedColumn : DERIVED_COLUMNS) {
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


