/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view.inflation;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observer;

import javax.swing.undo.CompoundEdit;

import blacksmyth.personalfinancier.control.UndoManagers;
import blacksmyth.personalfinancier.control.inflation.command.AddInflationEntryCommand;
import blacksmyth.personalfinancier.control.inflation.command.ChangeInflationDateCommand;
import blacksmyth.personalfinancier.control.inflation.command.ChangeInflationNotesCommand;
import blacksmyth.personalfinancier.control.inflation.command.ChangeInflationValueCommand;
import blacksmyth.personalfinancier.control.inflation.command.RemoveInflationEntryCommand;
import blacksmyth.personalfinancier.model.inflation.InflationEntry;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.AbstractFinancierTableModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

/**
 * An abstract TableModel using generics for collecting all common behaviours of the Budget TableModels. An enumeration representing
 * the columns of the table model is required as part of the class definition. 
 * @author linds
 *
 * @param <T>
 */

enum COLUMNS {
  Date, CPI, Notes
}

class InflationTableModel extends AbstractFinancierTableModel<COLUMNS> {

  private InflationModel inflationModel;

  
  public InflationTableModel(InflationModel inflationModel) {
    super();
    setInflationModel(inflationModel);
  }

  /**
   * Returns a reference to the budget model this TableModel observers.
   * @return
   */
  protected final InflationModel getInflationModel() {
    return this.inflationModel;
  }

  /**
   * Registers this TableModel as an observer of <tt>budgetModel</tt>
   * and stores a reference to it.
   * @param budgetModel
   */
  protected final void setInflationModel(InflationModel inflationModel) {
    inflationModel.addObserver(this);
    this.inflationModel = inflationModel;
  }
  
  public void addModelObserver(Observer observer) {
    this.getInflationModel().addObserver(observer);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (this.getColumnEnumValueAt(colNum)) {
      case Date:
        return Calendar.class;
      case CPI:
        return Double.class;
      case Notes: 
        return String.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return getInflationModel().getInflationList().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    return true;
  }

  public Object getValueAt(int rowNum, int colNum) {
    InflationEntry entry = getInflationModel().getInflationList().get(rowNum);
    
    switch (this.getColumnEnumValueAt(colNum)) {
      case Date:
        return entry.getDate();
      case CPI:
        return new Double(entry.getCPIValue());
      case Notes: 
        return entry.getNotes();
      default:
        return null;
    }
  }
  
  public void setValueAt(Object value, int rowNum, int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
    case Date:
      
      GregorianCalendar valueAsDate = new GregorianCalendar();
      try {
        valueAsDate.setTime(
            WidgetFactory.DATE_FORMAT.parse((String) value)
        );
      } catch (Exception e) {
        valueAsDate.setTime(new Date());
      }
       
      UndoManagers.INFLATION_UNDO_MANAGER.addEdit(
          ChangeInflationDateCommand.doCmd(
              getInflationModel(), 
              rowNum, 
              valueAsDate
          )
      );
      break;
    case CPI:
      UndoManagers.INFLATION_UNDO_MANAGER.addEdit(
          ChangeInflationValueCommand.doCmd(
              getInflationModel(), 
              rowNum, 
              Double.parseDouble((String) value)
          )
      );
      break;
    case Notes:
      UndoManagers.INFLATION_UNDO_MANAGER.addEdit(
          ChangeInflationNotesCommand.doCmd(
              getInflationModel(), 
              rowNum, 
              (String) value
          )
      );
      break;
    }
  }

  public void addEntry() {
    UndoManagers.INFLATION_UNDO_MANAGER.addEdit(
        AddInflationEntryCommand.doCmd(
            getInflationModel()
        )
    );
  }

  public void removeEntries(int[] rows) {
    CompoundEdit removeItemsEdit = new CompoundEdit();
    
    Arrays.sort(rows);  // guarantee we're iterating over the rows in correct order.
  
    for(int rowIdx = rows.length - 1; rowIdx > -1; rowIdx--) {
      removeItemsEdit.addEdit(
        RemoveInflationEntryCommand.doCmd(
          getInflationModel(),
          rows[rowIdx]
        )
      );
    }
    removeItemsEdit.end();
    
    UndoManagers.INFLATION_UNDO_MANAGER.addEdit(removeItemsEdit);
  }


}
