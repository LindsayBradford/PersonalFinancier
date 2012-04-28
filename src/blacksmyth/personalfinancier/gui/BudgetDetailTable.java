package blacksmyth.personalfinancier.gui;

import java.awt.Component;
import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.SwingUtilities;


import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyUtilties;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetCategory;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

enum BUDGET_DETAIL_COLUMNS {
  Category, Description, Amount, Frequency, 
  Daily, Weekly,Fortnightly, Monthly,
  Quarterly, Yearly, Account
}

public class BudgetDetailTable extends JTable {
  private static final long serialVersionUID = 1L;
  
  private static final int CELL_BUFFER = 15;

  // TODO: align with BudgetModel;
  
  public BudgetDetailTable(BudgetModel budgetModel) {
    super(
        new BudgetDetailTableModel(budgetModel)
    );
    setupColumns();
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
    
    this.getBudgetDetailTableModel().addBaseModelObserver(
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

  private BudgetDetailTableModel getBudgetDetailTableModel() {
    return (BudgetDetailTableModel) getModel();
  }
}


class BudgetDetailTableModel extends AbstractTableModel implements Observer {
  private static final long serialVersionUID = 1L;

  private BudgetModel baseModel;
 
  public BudgetDetailTableModel(BudgetModel budgetModel) {
    super();
    baseModel = budgetModel;

    addBaseModelObserver(this);
  }

  public int getColumnCount() {
    return BUDGET_DETAIL_COLUMNS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return BUDGET_DETAIL_COLUMNS.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (BUDGET_DETAIL_COLUMNS.values()[colNum]) {
      case Category:
        return BudgetCategory.class;
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
    switch (BUDGET_DETAIL_COLUMNS.values()[colNum]) {
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
    
    switch (BUDGET_DETAIL_COLUMNS.values()[colNum]) {
      case Category:
        return item.getCategory();
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
    switch (BUDGET_DETAIL_COLUMNS.values()[colNum]) {
    case Category:
      baseModel.setBudgetItemCategory(
          rowNum, 
          BudgetCategory.valueOf((String) value)
      );
      break;
    case Description:
      baseModel.setBudgetItemDescription(rowNum, (String) value);
      break;
    case Amount:
      baseModel.setBudgetItemTotal(
          rowNum,
          BigDecimalFactory.create((String) value)
      );
      break;
    case Frequency:
      baseModel.setBudgetItemFrequency(
          rowNum,
          CashFlowFrequency.valueOf((String) value)
      );
      break;
    case Account:
      baseModel.setBudgetItemAccount(
          rowNum,
          (String) value
      );
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