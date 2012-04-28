package blacksmyth.personalfinancier.gui;

import java.awt.Component;
import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.SwingUtilities;


import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyUtilties;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

enum COLS_ENUM {
  Description,
  Amount,
  Frequency,
  Daily,
  Weekly,
  Fortnightly,
  Monthly,
  Quarterly,
  Yearly,
  Account
}

public class BudgetDetailTable extends JTable {
  private static final long serialVersionUID = 1L;
  
  private static final int CELL_BUFFER = 15;

  // TODO: align with BudgetModel;
  
  public BudgetDetailTable() {
    super(
        new BudgetDetailTableModel()
    );
    setupColumns();
  }
  
  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setupAmountCol(COLS_ENUM.Amount);
    setupFrequencyCol();
    setupAmountCol(COLS_ENUM.Daily);
    setupAmountCol(COLS_ENUM.Weekly);
    setupAmountCol(COLS_ENUM.Fortnightly);
    setupAmountCol(COLS_ENUM.Monthly);
    setupAmountCol(COLS_ENUM.Quarterly);
    setupAmountCol(COLS_ENUM.Yearly);
    setupAccountCol();
  }
  
  private void setupAccountCol() {
    getColFromEnum(COLS_ENUM.Account).setCellRenderer(
        WidgetFactory.createBudgetAccountCellRenderer()    
    );
    
    DefaultCellEditor editor = WidgetFactory.createBudgetAccountCellEditor();    

    getColFromEnum(COLS_ENUM.Account).setCellEditor(editor);
    
    this.getBudgetDetailTableModel().addBaseModelObserver(
        (Observer) editor.getComponent()
    );
    
    SwingUtilities.lockColumnWidth(
        getColFromEnum(COLS_ENUM.Account),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }

  private void setupAmountCol(COLS_ENUM thisColumn) {
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
  
  private void setupFrequencyCol() {
    getColFromEnum(COLS_ENUM.Frequency).setCellRenderer(
        WidgetFactory.createCashFlowFrequencyCellRenderer()    
    );

    DefaultCellEditor editor = WidgetFactory.createCashFlowFrequencyCellEditor();    
    getColFromEnum(COLS_ENUM.Frequency).setCellEditor(editor);

    SwingUtilities.lockColumnWidth(
        getColFromEnum(COLS_ENUM.Frequency),
        (int) editor.getComponent().getPreferredSize().getWidth()
    );
  }
  
  private TableColumn getColFromEnum(COLS_ENUM thisEnum) {
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
    return (CashFlowFrequency) this.getModel().getValueAt(row, COLS_ENUM.Frequency.ordinal());
  }

  private BudgetDetailTableModel getBudgetDetailTableModel() {
    return (BudgetDetailTableModel) getModel();
  }
}


class BudgetDetailTableModel extends AbstractTableModel implements Observer {
  private static final long serialVersionUID = 1L;

  private BudgetModel baseModel;
 
  public BudgetDetailTableModel() {
    super();
    baseModel = new BudgetModel();

    // TODO: we need a BudgetModel controller.
    
    baseModel.addObserver(this);
    
    baseModel.addBudgetItem("test");
    baseModel.addBudgetItem("another test");
    baseModel.addBudgetItem("yet another test");
  }

  public int getColumnCount() {
    return COLS_ENUM.values().length;
  }
  
  public String getColumnName(int colNum) {
    return COLS_ENUM.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (COLS_ENUM.values()[colNum]) {
      case Description:
        return String.class;
      case Amount: 
        return Money.class;
      case Frequency:
        return CashFlowFrequency.class;
      case Daily: case Weekly: case Fortnightly: 
      case Monthly: case Quarterly: case Yearly:
        return BigDecimal.class;
      case Account:
        return String.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return baseModel.getBudgetItems().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    switch (COLS_ENUM.values()[colNum]) {
      case Daily: case Weekly: case Fortnightly: 
      case Monthly: case Quarterly: case Yearly:
        return false;
      default:
        return true;
    }
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    BudgetItem item = (BudgetItem) baseModel.getBudgetItems().get(rowNum);
    
    switch (COLS_ENUM.values()[colNum]) {
      case Description:
        return item.getDescription();
      case Amount: 
        return item.getBudgettedAmount().getTotal();
      case Daily:
        return convertAmount(item, CashFlowFrequency.Daily);
      case Weekly:
        return convertAmount(item, CashFlowFrequency.Weekly);
      case Fortnightly:
        return convertAmount(item, CashFlowFrequency.Fortnightly);
      case Monthly:
        return convertAmount(item, CashFlowFrequency.Monthly);
      case Quarterly:
        return convertAmount(item, CashFlowFrequency.Quarterly);
      case Yearly:
        return convertAmount(item, CashFlowFrequency.Yearly);
      case Frequency:
        return item.getFrequency();
      case Account:
         return item.getBudgetAccount().getNickname();
       default:
         return null;
    }
  }
  
  private BigDecimal convertAmount(BudgetItem item, CashFlowFrequency newFrequency) {
    return MoneyUtilties.convertFrequencyAmount(
        item.getBudgettedAmount().getTotal(), 
        item.getFrequency(), 
        newFrequency
    );
  }
  
  public void setValueAt(Object value, int rowNum, int colNum) {
    @SuppressWarnings("cast")
    BudgetItem item = (BudgetItem) baseModel.getBudgetItems().get(rowNum);
    switch (COLS_ENUM.values()[colNum]) {
    case Description:
      item.setDescription((String) value);
      break;
    case Amount:
      item.getBudgettedAmount().setTotal(
          BigDecimalFactory.create((String) value)
      );
      break;
    case Frequency:
      item.setFrequency(CashFlowFrequency.valueOf((String) value));
      break;
    case Account:
      item.setBudgetAccount(baseModel.getBudgetAccount((String) value));
      break;
    }
    this.fireTableRowsUpdated(rowNum, rowNum);
  }

  public void update(Observable o, Object arg) {
    this.fireTableDataChanged();
  }
  
  public Observable getBaseModel() {
    return baseModel;
  }
  
  public void addBaseModelObserver(Observer observer) {
    this.getBaseModel().addObserver(observer);
  }
  
}