/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view;

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
