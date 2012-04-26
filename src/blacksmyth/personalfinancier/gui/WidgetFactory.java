/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import blacksmyth.personalfinancier.model.CashFlowFrequency;

public class WidgetFactory {
  
  public static final String DECIMAL_FORMAT_PATTERN = "###,###,##0.00";
  
  private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(DECIMAL_FORMAT_PATTERN);

  @SuppressWarnings("serial")
  public static DefaultTableCellRenderer createCashFlowFrequencyCellRenderer() {
    return new DefaultTableCellRenderer() {
      public void setValue(Object value) {
        this.setHorizontalAlignment(JTextField.CENTER);
        super.setValue(value);
      }
    };
  }
  
  public static DefaultCellEditor createCashFlowFrequencyCellEditor() {
    return new DefaultCellEditor(
        createCashFlowFrequencyComboBox()
    );
  }
  
  /**
   * Creates a <tt>JComboBox</tt> pre-loaded with items from the 
   * <tt>CashFlowFrequency</tt> enumeration.
   * @return JComboBox
   */
  public static JComboBox createCashFlowFrequencyComboBox() {
    JComboBox comboBox = new JComboBox();

    DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
    dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    comboBox.setRenderer(dlcr);
    
    for (CashFlowFrequency frequency : CashFlowFrequency.values()) {
      comboBox.addItem(frequency.toString());
    }
    
    return comboBox;
  }
  
  @SuppressWarnings("serial")
  public static DefaultTableCellRenderer createAmountCellRenderer() {
    return new DefaultTableCellRenderer() {
      
      public void setValue(Object value) {
        this.setHorizontalAlignment(JTextField.RIGHT);
        this.setText((value == null) ? "" : DECIMAL_FORMATTER.format(value));
      }
    };
  }
  
  public static DefaultCellEditor createAmountCellEditor() {
    return new DefaultCellEditor(
        createAmountTextField()
    );
  }
  
  public static JFormattedTextField createAmountTextField() {
    JFormattedTextField field = new JFormattedTextField(DECIMAL_FORMATTER);
    
    field.setHorizontalAlignment(JTextField.RIGHT);
    field.setInputVerifier(new FormatVerifier());
    
    return field;
  }

}

class FormatVerifier extends InputVerifier {
  public boolean verify(JComponent input) {
    if (input instanceof JFormattedTextField) {
        JFormattedTextField ftf = (JFormattedTextField) input;
        AbstractFormatter formatter = ftf.getFormatter();
        if (formatter != null) {
            String text = ftf.getText();
            try {
                 formatter.stringToValue(text);
                 return true;
             } catch (ParseException pe) {
                 return false;
             }
         }
     }
     return true;
  }
  
  public boolean shouldYieldFocus(JComponent input) {
    return verify(input);
  }
}

