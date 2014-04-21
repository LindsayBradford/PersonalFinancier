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

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import blacksmyth.personalfinancier.view.WidgetFactory;

public class InflationWidgetFactory {

  @SuppressWarnings("serial")
  public static DefaultTableCellRenderer createCPIValueCellRenderer() {
    return WidgetFactory.createDecimalCellRenderer(JTextField.CENTER);
  }
  
  public static DefaultCellEditor createCPIValueCellEditor() {
    return new DefaultCellEditor(
        WidgetFactory.createCentredDecimalTextField()
    );
  }
}