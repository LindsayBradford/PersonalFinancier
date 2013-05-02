/**
 * (c) 2012, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */


package blacksmyth.personalfinancier.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import javax.swing.text.DocumentFilter;

import blacksmyth.general.FontIconProvider;
import blacksmyth.personalfinancier.model.PreferencesModel;

/**
 * A library of methods to construct low-level Swing JComponet widgets in a uniform
 * way throughout the application based on user preferences in {@link PreferencesModel}.
 * @author linds
 *
 */
public final class WidgetFactory {
  
  public static final String DECIMAL_FORMAT_PATTERN = "###,##0.00";
  public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(DECIMAL_FORMAT_PATTERN);

  public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

  @SuppressWarnings("serial")
  public static DefaultTableCellRenderer createTableCellRenderer(final int alignment) {
    return new DefaultTableCellRenderer() {
      public void setValue(Object value) {
        this.setHorizontalAlignment(alignment);
        super.setValue(value);
      }
    };
  }

  @SuppressWarnings("serial")
  public static DefaultTableCellRenderer createAmountCellRenderer() {
    return createDecimalCellRenderer(JTextField.RIGHT);
  }
  
  @SuppressWarnings("serial")
  public static DefaultTableCellRenderer createDecimalCellRenderer(final int alignment) {
    return new DefaultTableCellRenderer() {
      public void setValue(Object value) {
        this.setHorizontalAlignment(alignment);
        this.setText((value == null) ? "" : DECIMAL_FORMAT.format(value));            
      }
    };
  }
  
  public static DefaultTableCellRenderer createDateCellRenderer() {
    return new DefaultTableCellRenderer() {
      public void setValue(Object value) {
        if (value.getClass().equals(GregorianCalendar.class)) {
          value = ((GregorianCalendar) value).getTime();
        }
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setText((value == null) ? "" : DATE_FORMAT.format(value));            
      }
    };
  }
  
  public static DefaultCellEditor createAmountCellEditor() {
    return new DefaultCellEditor(
        createAmountTextField()
    );
  }

  public static DefaultCellEditor createDateCellEditor() {
    return new DefaultCellEditor(
        createDateTextField()
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
  public static void prepareTableCellRenderer(JTable table, Component cellRenderer, int row, int column) {
    Color rowColor = (row % 2 == 0) ? 
        PreferencesModel.getInstance().getPreferredEvenRowColor() : 
        PreferencesModel.getInstance().getPreferredOddRowColor();
    
    cellRenderer.setBackground(rowColor);
    
    if (table.isCellEditable(row,column)) {
      cellRenderer.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
      );
    } else {
      cellRenderer.setForeground(
        PreferencesModel.getInstance().getPreferredUnEditableCellColor()
      );
    }
    
    if (table.isCellSelected(row, column)) {
      cellRenderer.setBackground(
          PreferencesModel.getInstance().getPreferredSelectedCellColor()
      );
      cellRenderer.setForeground(
          cellRenderer.getForeground().brighter()
      );
    }
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an application-specific way.
   * @return
   */
  public static JFormattedTextField createCentredDecimalTextField() {
    return createDecimalTextField(JTextField.CENTER);
  }
  
  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an application-specific way.
   * @return
   */
  public static JFormattedTextField createAmountTextField() {
    return createDecimalTextField(JTextField.RIGHT);
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an application-specific way.
   * @return
   */
  public static JFormattedTextField createDecimalTextField(int alignment) {
    JFormattedTextField field = new JFormattedTextField();
    
    field.setInputVerifier(new FormatVerifier());
    
    ensureTextFieldSelectsAllOnFocus(field);
    
    ((AbstractDocument) field.getDocument()).setDocumentFilter(
        new DecimalCharFilter()
    );
    
    field.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
    );
    field.setHorizontalAlignment(alignment);
    
    return field;
  }
  
  public static void ensureTextFieldSelectsAllOnFocus(final JTextField field) {
    field.addFocusListener(
        new FocusAdapter() {
          public void focusGained(java.awt.event.FocusEvent evt) {
            SwingUtilities.invokeLater( 
                new Runnable() {

                @Override
                public void run() {
                field.selectAll();    
              }
           });
        }
    });
  }

  
  /**
   * Create a {@link JFormattedTextField} configured to edit dates in an application-specific way.
   * @return
   */
  public static JFormattedTextField createDateTextField() {
    final JFormattedTextField field = new JFormattedTextField();
    
    field.setInputVerifier(new FormatVerifier());
    
    ensureTextFieldSelectsAllOnFocus(field);
    
    ((AbstractDocument) field.getDocument()).setDocumentFilter(
        new DateCharFilter()
    );
    
    field.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
    );
    field.setHorizontalAlignment(JTextField.CENTER);
    
    return field;
  }

  /**
   * Creates a <tt>JComboBox</tt> suitable for application table cell editors with
   * a static enumeration.
   * @return JComboBox
   */
  public static JComboBox<String> createTableComboBox() {
    JComboBox<String> comboBox = new JComboBox<String>();

    DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
    dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    dlcr.setForeground(
        PreferencesModel.getInstance().getPreferredEditableCellColor()
    );
    comboBox.setRenderer(dlcr);
    
    return comboBox;
  }
  /**
   * Creates a standard {@link TitledBorder} of the specified title and color.
   * @param title 
   * @param color
   * @return
   */
  public static TitledBorder createColoredTitledBorder(String title, Color color) {
    TitledBorder border = new TitledBorder(title);

    border.setBorder(new LineBorder(color));
    border.setTitleColor(color);
    
    return border;
  }
  
  public static JTabbedPane createGraphTablePane(JComponent graphComponent, JComponent tableComponent) {
    JTabbedPane pane = new JTabbedPane();
    
    pane.setTabPlacement(JTabbedPane.LEFT);

    int currTabIndex = 0;
    
    pane.addTab("", tableComponent);
    pane.setToolTipTextAt(currTabIndex, " View as table ");

    FontIconProvider.getInstance().setGlyphAsTitle(
        pane, currTabIndex, 
        FontIconProvider.icon_table
    );

    currTabIndex++;
    
    pane.addTab("",graphComponent);
    pane.setToolTipTextAt(currTabIndex, " View as graph ");
    
    FontIconProvider.getInstance().setGlyphAsTitle(
        pane, currTabIndex, 
        FontIconProvider.icon_bar_chart
    );
    
    enableSelectionHilightedTabPane(pane);
    
    return pane;
  }
  
  public static void enableSelectionHilightedTabPane(JTabbedPane pane) {

    final Color selectedColor = Color.green.darker().darker().darker();
    
    pane.setBackgroundAt(0, selectedColor);
    
    pane.addChangeListener(
        new ChangeListener() {
          public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane sourcePane = (JTabbedPane) changeEvent.getSource();
            int index = sourcePane.getSelectedIndex();
            
            for (int tabIndex = 0; tabIndex < sourcePane.getTabCount(); tabIndex++) {
              sourcePane.setBackgroundAt(tabIndex, sourcePane.getBackground());
            }
            sourcePane.setBackgroundAt(index, selectedColor);
          }
        }
    );
  }
  
  /**
   * Creates a JButton that is enabled only when a single row of the specified
   * JTable is selected.
   * @param table
   * @return
   */
  public static JButton createOneSelectedtRowEnabledButton(JTable table) {
    JTableListeningButton theButton = new JTableListeningButton(table) {
      public void valueChanged(ListSelectionEvent event) {
        if (this.selectedTableRows() != 1) {
          this.setEnabled(false);
        } else {
          this.setEnabled(true);
        }
      }
    };
    return theButton;
  }
  
  /**
   * Creates a JButton that is enabled when one or more row of the specified
   * JTable is selected.
   * @param table
   * @return
   */
  public static JButton createMultiSelectedtRowEnabledButton(JTable table) {
    JTableListeningButton theButton = new JTableListeningButton(table) {
      public void valueChanged(ListSelectionEvent event) {
        if (this.selectedTableRows() == 0) {
          this.setEnabled(false);
        } else {
          this.setEnabled(true);
        }
      }
    };
    return theButton;
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

class DateCharFilter extends DocumentFilter {
  private static DateCharFilter instance;
  
  private static final String VALID_CHARS = "0123456789/";
  
  protected DateCharFilter() {
    super();
  }
  
  public static DateCharFilter getInstance() {
    if (instance == null) {
      instance = new DateCharFilter();
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