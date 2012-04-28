/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import javax.swing.text.DocumentFilter;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.PreferencesModel;

/**
 * A library of methods to construct low-level Swing JComponet widgets in a uniform
 * way throughout the application based on user preferences in {@link PreferencesModel}.
 * @author linds
 *
 */
public final class WidgetFactory {
  
  public static final String DECIMAL_FORMAT_PATTERN = "###,##0.00";
  
  protected static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(DECIMAL_FORMAT_PATTERN);

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
    dlcr.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
    );

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
        this.setText((value == null) ? "" : DECIMAL_FORMAT.format(value));
      }
    };
  }
  
  public static DefaultCellEditor createAmountCellEditor() {
    return new DefaultCellEditor(
        createAmountTextField()
    );
  }

  /**
   * prepares a <tt>JTable</tt> cell renderer for display based on 
   * various colour preferences in {@link PreferencesModel}.
   * @param cellRenderer
   * @param row
   * @param column 
   * @param isEditable Indicates wheether the cell is editable or not.
   */
  public static void prepareTableCellRenderer(Component cellRenderer, int row, int column, boolean isEditable) {
    Color rowColor = (row % 2 == 0) ? 
        PreferencesModel.getInstance().getPreferredEvenRowColor() : 
        PreferencesModel.getInstance().getPreferredOddRowColor();
    
    cellRenderer.setBackground(rowColor);
    
    if (isEditable) {
      cellRenderer.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
      );
    } else {
      cellRenderer.setForeground(
        PreferencesModel.getInstance().getPreferredUnEditableCellColor()
      );
    }
  }
  
  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an application-specific way.
   * @return
   */
  public static JFormattedTextField createAmountTextField() {
    JFormattedTextField field = new JFormattedTextField();
    
    field.setInputVerifier(new FormatVerifier());
    
    ((AbstractDocument) field.getDocument()).setDocumentFilter(
        new DecimalCharFilter()
    );
    
    field.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
    );
    field.setHorizontalAlignment(JTextField.RIGHT);
    
    return field;
  }

  @SuppressWarnings("serial")
  public static DefaultTableCellRenderer createBudgetAccountCellRenderer() {
    return new DefaultTableCellRenderer() {
      public void setValue(Object value) {
        this.setHorizontalAlignment(JTextField.CENTER);
        super.setValue(value);
      }
    };
  }
  
  public static DefaultCellEditor createBudgetAccountCellEditor() {
    return new DefaultCellEditor(
        createBudgetAccountComboBox()
    );
  }
  
  /**
   * Creates a <tt>JComboBox</tt> pre-loaded with Accounts used
   * in budgetting.
   * @return JComboBox
   */
  public static JComboBox createBudgetAccountComboBox() {
    BudgetAccountComboBox comboBox = new BudgetAccountComboBox();  
    
    DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
    dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    dlcr.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
    );

    comboBox.setRenderer(dlcr);

    return comboBox;
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

class DecimalCharFilter extends DocumentFilter {
  private static DecimalCharFilter instance;
  
  private static final String VALID_CHARS = "0123456789.,";
  
  protected DecimalCharFilter() {
    super();
  }
  
  public static DecimalCharFilter getInstance() {
    if (instance == null) {
      instance = new DecimalCharFilter();
    }
    return instance;
  }
  
  public void insertString(DocumentFilter.FilterBypass bypass, int offset, String string, AttributeSet attr) 
      throws BadLocationException {
    if (isValidText(string)) bypass.insertString(offset, string,attr);
  }
  
  public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, String text, AttributeSet attributes) 
      throws BadLocationException {
    if (isValidText(text)) bypass.replace(offset,length,text,attributes);
  }

  protected boolean isValidText(String text) {
    for (int i = 0; i < text.length(); i++) {
      if (VALID_CHARS.indexOf(text.charAt(i)) == -1) {
        return false;
      }
    }
    return true;
  }
}