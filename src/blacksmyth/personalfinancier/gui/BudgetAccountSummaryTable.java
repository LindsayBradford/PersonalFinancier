package blacksmyth.personalfinancier.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.SwingUtilities;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.BudgetSummary;

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
        new BudgetAccountSummaryModel(budgetModel)
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
  
  private BudgetAccountSummaryModel getBudgetAccountSummaryModel() {
    return (BudgetAccountSummaryModel) getModel();
  }
}

// TODO: Refactor commonalities between this and BudgetDetailtableModel

@SuppressWarnings("serial")
class BudgetAccountSummaryModel extends AbstractTableModel implements Observer {

  private BudgetModel baseModel;
  
  public BudgetAccountSummaryModel(BudgetModel budgetModel) {
    super();
    baseModel = budgetModel;

    addBaseModelObserver(this);
  }

  public int getColumnCount() {
    return BUDGET_SUMMARY_COLUMNS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return BUDGET_SUMMARY_COLUMNS.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (BUDGET_SUMMARY_COLUMNS.values()[colNum]) {
      case Account:
        return String.class;
      case Detail:
        return String.class;
      case Budgetted: 
        return Money.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return baseModel.getBudgetSummaries().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    return false;
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    BudgetSummary summary = (BudgetSummary) baseModel.getBudgetSummaries().get(rowNum);
    
    switch (BUDGET_SUMMARY_COLUMNS.values()[colNum]) {
    case Account:
      return summary.getAccountNickname();
    case Detail:
      return summary.getAccountDetail();
    case Budgetted: 
      return summary.getBudgettedAmount(CashFlowFrequency.Fortnightly);
    default:
         return null;
    }
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