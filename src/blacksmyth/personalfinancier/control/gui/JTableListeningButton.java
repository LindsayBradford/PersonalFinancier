/**
 * (c) 2012, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.control.gui;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class JTableListeningButton extends JButton implements ListSelectionListener {
  
  private JTable table;
  
  public JTableListeningButton(JTable theTable) {
    super();
    this.table = theTable;
    this.table.getSelectionModel().addListSelectionListener(this);
    this.setEnabled(false);
  }

  public JTable getTable() {
    return this.table;
  }
  
  public int selectedTableRows() {
    return this.table.getSelectedRows().length;
  }
  
  @Override
  public abstract void valueChanged(ListSelectionEvent event);
}
