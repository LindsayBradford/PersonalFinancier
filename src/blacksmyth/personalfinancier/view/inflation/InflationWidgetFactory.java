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
