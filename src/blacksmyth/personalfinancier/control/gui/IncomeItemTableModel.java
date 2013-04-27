package blacksmyth.personalfinancier.control.gui;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.undo.CompoundEdit;

import blacksmyth.personalfinancier.control.BudgetUndoManager;
import blacksmyth.personalfinancier.control.command.AddIncomeItemCommand;
import blacksmyth.personalfinancier.control.command.ChangeIncomeAccountCommand;
import blacksmyth.personalfinancier.control.command.ChangeIncomeAmountCommand;
import blacksmyth.personalfinancier.control.command.ChangeIncomeCategoryCommand;
import blacksmyth.personalfinancier.control.command.ChangeIncomeDescriptionCommand;
import blacksmyth.personalfinancier.control.command.ChangeIncomeFrequencyCommand;
import blacksmyth.personalfinancier.control.command.MoveIncomeItemDownCommand;
import blacksmyth.personalfinancier.control.command.MoveIncomeItemUpCommand;
import blacksmyth.personalfinancier.control.command.RemoveIncomeItemCommand;
import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyUtilties;
import blacksmyth.personalfinancier.model.budget.BudgetEvent;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

enum INCOME_ITEM_COLUMNS {
  Category, Description, Amount, Frequency, 
  Daily, Weekly,Fortnightly, Monthly,
  Quarterly, Yearly, Account
}

class IncomeItemTableModel extends AbstractBudgetTableModel<INCOME_ITEM_COLUMNS> {

  public IncomeItemTableModel(BudgetModel budgetModel) {
    super();
    setBudgetModel(budgetModel);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (this.getColumnEnumValueAt(colNum)) {
      case Category:
        return String.class;
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
    return getBudgetModel().getIncomeItems().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
      case Daily: case Weekly: case Fortnightly: 
      case Monthly: case Quarterly: case Yearly:
        return false;
      default:
        return true;
    }
  }

  public Object getValueAt(int rowNum, int colNum) {
    BudgetItem item = getBudgetModel().getIncomeItems().get(rowNum);
    
    switch (this.getColumnEnumValueAt(colNum)) {
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
    switch (this.getColumnEnumValueAt(colNum)) {
    case Category:
      BudgetUndoManager.getInstance().addEdit(
          ChangeIncomeCategoryCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              (String) value
          )
      );
      break;
    case Description:
      BudgetUndoManager.getInstance().addEdit(
          ChangeIncomeDescriptionCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              (String) value
          )
      );
      break;
    case Amount:
      BudgetUndoManager.getInstance().addEdit(
          ChangeIncomeAmountCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              BigDecimalFactory.create((String) value)
          )
      );
      break;
    case Frequency:
      BudgetUndoManager.getInstance().addEdit(
          ChangeIncomeFrequencyCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              CashFlowFrequency.valueOf((String) value)
          )
      );
      break;
    case Account:
      BudgetUndoManager.getInstance().addEdit(
          ChangeIncomeAccountCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              (String) value
          )
      );
      break;
    }
  }
  
  /**
   * Forces a table refresh whenever the BudgetModel this TableModel
   * observes sends an update.
   */
  @Override
  public void update(Observable budgeModelAsObject, Object budgetEventAsObject) {
    BudgetEvent event = (BudgetEvent) budgetEventAsObject;
    
    if (event.getItemType() == BudgetEvent.ItemType.IncomeItems ||
        event.getItemType() == BudgetEvent.ItemType.AllItems) {
      this.fireTableDataChanged();
    }
  }

  public void addModelObserver(Observer observer) {
    this.getBudgetModel().addObserver(observer);
  }

  public void addIncomeItem() {
    BudgetUndoManager.getInstance().addEdit(
        AddIncomeItemCommand.doCmd(
            getBudgetModel()
        )
    );
  }

  public void removeItem(int row) {
    BudgetUndoManager.getInstance().addEdit(
        RemoveIncomeItemCommand.doCmd(
            getBudgetModel(),
            row
        )
    );
  }
  
  public void removeItems(int[] rows) {
    CompoundEdit removeItemsEdit = new CompoundEdit();
	  
	Arrays.sort(rows);  // guarantee we're iterating over the rows in correct order.
	
	for(int rowIdx = rows.length - 1; rowIdx > -1; rowIdx--) {
	  removeItemsEdit.addEdit(
		  RemoveIncomeItemCommand.doCmd(
		    getBudgetModel(),
			rows[rowIdx]
		  )
	   );
	  }
	  removeItemsEdit.end();
	  
	  BudgetUndoManager.getInstance().addEdit(removeItemsEdit);
  }

  
  
  public void moveIncomeItemDown(int row) {
    BudgetUndoManager.getInstance().addEdit(
        MoveIncomeItemDownCommand.doCmd(
            getBudgetModel(),
            row
        )
    );
  }

  public void moveIncomeItemUp(int row) {
    BudgetUndoManager.getInstance().addEdit(
        MoveIncomeItemUpCommand.doCmd(
            getBudgetModel(),
            row
        )
    );
  }
}
