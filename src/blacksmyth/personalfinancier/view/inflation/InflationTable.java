/**
 * (c) 2012, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.view.inflation;

import java.awt.Component;
import java.util.LinkedList;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import blacksmyth.general.SwingUtilities;

import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class InflationTable extends JTable {
  
  private LinkedList<TableColumn> tableColumnList = new LinkedList<TableColumn>();

  private static final long serialVersionUID = 1L;
  
  private static final int CELL_BUFFER = 15;
  
  public InflationTable(InflationModel budgetModel) {
    super(
        new InflationTableModel(budgetModel)
    );
    this.setRowSelectionAllowed(true);
    setupColumns();
    
    for(COLUMNS column : COLUMNS.values()) {
      tableColumnList.add(
          getColFromEnum(column)
      );
    }

    this.getInflationTableModel().fireTableDataChanged();
  }
  
  private void setupColumns() {
    this.tableHeader.setReorderingAllowed(false);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    
    setupDateCol();
    setupCPICol();
  }
  
  private void setupDateCol() {
    getColFromEnum(COLUMNS.Date).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    SwingUtilities.lockColumnWidth(
        getColFromEnum(COLUMNS.Date),
        SwingUtilities.getTextWidth(
            WidgetFactory.DATE_FORMAT_PATTERN
        ) + CELL_BUFFER
    );

    getColFromEnum(COLUMNS.Date).setCellRenderer(
        WidgetFactory.createDateCellRenderer()    
    );
    getColFromEnum(COLUMNS.Date).setCellEditor(
        WidgetFactory.createDateCellEditor()    
    );
  }

  private void setupCPICol() {
    getColFromEnum(COLUMNS.CPI).setCellRenderer(
        WidgetFactory.createTableCellRenderer(JTextField.CENTER)    
    );

    SwingUtilities.lockColumnWidth(
        getColFromEnum(COLUMNS.CPI),
        SwingUtilities.getTextWidth(
            WidgetFactory.DECIMAL_FORMAT_PATTERN
        ) + CELL_BUFFER
    );

    getColFromEnum(COLUMNS.CPI).setCellRenderer(
        WidgetFactory.createAmountCellRenderer()    
    );
    getColFromEnum(COLUMNS.CPI).setCellEditor(
        WidgetFactory.createAmountCellEditor()    
    );
  }
  
  public void tableChanged(TableModelEvent e) {
    this.setVisible(false);
    super.tableChanged(e);
    this.setVisible(true);
  }

  private TableColumn getColFromEnum(COLUMNS thisEnum) {
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
   
    return cellRenderer;
  }

  public InflationTableModel getInflationTableModel() {
    return (InflationTableModel) getModel();
  }

  public void addInflationEntry() {
    this.getInflationTableModel().addEntry();

    SwingUtilities.scrollRowToVisible(
        this, 
        this.getRowCount() - 1
    );
    
    this.selectionModel.setSelectionInterval(
        this.getRowCount() - 1, 
        this.getRowCount() - 1
    );
  }
  
  public void removeInflationEntries() {
    this.getInflationTableModel().removeEntries(
	    this.getSelectedRows()
	  );
  }
}