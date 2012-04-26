package blacksmyth.personalfinancier.gui;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.SwingUtilities;
import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.MoneyAmount;
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
    SwingUtilities.lockColumnWidth(
        getColFromEnum(COLS_ENUM.Frequency),
        SwingUtilities.getTextWidth(
            COLS_ENUM.Frequency.toString()
        ) + CELL_BUFFER
    );

    getColFromEnum(COLS_ENUM.Frequency).setCellRenderer(
        WidgetFactory.createCashFlowFrequencyCellRenderer()    
    );
    getColFromEnum(COLS_ENUM.Frequency).setCellEditor(
        WidgetFactory.createCashFlowFrequencyCellEditor()    
    );
  }
  
  private TableColumn getColFromEnum(COLS_ENUM thisEnum) {
    return this.getColumnModel().getColumn(thisEnum.ordinal());
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
    switch (colNum) {
    case 0:
      return String.class;
    case 1: case 3: case 4: case 5: case 6: case 7: case 8:
      return MoneyAmount.class;
    case 2:
      return CashFlowFrequency.class;
    case 9:
      return Account.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return baseModel.getBudgetItems().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    switch (colNum) {
    case 3: case 4: case 5: case 6: case 7: case 8:
      return false;
    }
    return true;
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    BudgetItem item = (BudgetItem) baseModel.getBudgetItems().get(rowNum);
    switch (colNum) {
    case 0:
      return item.getDescription();
    // TODO: Add FrequencyConverter formulas.
    case 1: case 3: case 4: case 5: case 6: case 7: case 8:
      return item.getAmount().getAmount();
    case 2:
      return item.getFrequency();
    case 9:
       return item.getAccount();
    }
    return null;
  }
  
  public void setValueAt(Object value, int rowNum, int colNum) {
    @SuppressWarnings("cast")
    BudgetItem item = (BudgetItem) baseModel.getBudgetItems().get(rowNum);
    switch (colNum) {
    case 0 :
      item.setDescription((String) value);
      break;
    case 1:
      item.getAmount().setAmount(new BigDecimal((String) value));
      break;
    case 2 :
      item.setFrequency(CashFlowFrequency.valueOf((String) value));
      break;
    }
  }

  public void update(Observable o, Object arg) {
    this.fireTableDataChanged();
  }
}