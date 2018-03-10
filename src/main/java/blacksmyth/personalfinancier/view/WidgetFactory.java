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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;

import blacksmyth.general.BlacksmythSwingUtilities;
import blacksmyth.general.FontIconProvider;
import blacksmyth.personalfinancier.model.ModelPreferences;

/**
 * A library of methods to construct low-level Swing JComponet widgets in a
 * uniform way throughout the application based on user preferences in
 * {@link ModelPreferences}.
 * 
 * @author linds
 *
 */
public final class WidgetFactory {

  public static final String ACCOUNT_BUFFER = "                    ";

  public static final String PERCENT_FORMAT_PATTERN = "###,##0.00 %";
  public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat(PERCENT_FORMAT_PATTERN);

  public static final String DECIMAL_FORMAT_PATTERN = "#,###,##0.00";
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

  @SuppressWarnings("serial")
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
    return new DefaultCellEditor(createAmountTextField());
  }

  @SuppressWarnings("serial")
  public static DefaultCellEditor createDateCellEditor() {
    return new DefaultCellEditor(createDateTextField()) {
      @Override
      public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
          int column) {

        if (value.getClass() == GregorianCalendar.class) {
          GregorianCalendar valueAsCalendar = (GregorianCalendar) value;

          String formattedvalue = DATE_FORMAT.format(valueAsCalendar.getTime());
          delegate.setValue(formattedvalue);

          return editorComponent;
        }

        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
      }

      @Override
      public Object getCellEditorValue() {
        String value = (String) delegate.getCellEditorValue();
        GregorianCalendar valueAsCalendar;
        try {
          valueAsCalendar = new GregorianCalendar();
          valueAsCalendar.setTime(DATE_FORMAT.parse(value));
        } catch (ParseException e) {
          return super.getCellEditorValue();
        }
        return valueAsCalendar;
      }
    };
  }

  /**
   * prepares a <tt>JTable</tt> cell renderer for display based on various colour
   * preferences in {@link ModelPreferences}.
   * 
   * @param cellRenderer
   * @param row
   * @param column
   * @param isEditable
   *          Indicates wheether the cell is editable or not.
   */
  public static void prepareTableCellRenderer(JTable table, Component cellRenderer, int row, int column) {
    Color rowColor = (row % 2 == 0) ? ViewPreferences.getInstance().getPreferredEvenRowColor()
        : ViewPreferences.getInstance().getPreferredOddRowColor();

    cellRenderer.setBackground(rowColor);

    if (table.isCellEditable(row, column)) {
      cellRenderer.setForeground(ViewPreferences.getInstance().getPreferredEditableCellColor());
    } else {
      cellRenderer.setForeground(ViewPreferences.getInstance().getPreferredUnEditableCellColor());
    }

    if (table.isCellSelected(row, column)) {
      cellRenderer.setBackground(ViewPreferences.getInstance().getPreferredSelectedCellColor());
      cellRenderer.setForeground(cellRenderer.getForeground().brighter());
    }
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an
   * application-specific way.
   * 
   * @return
   */
  public static JFormattedTextField createCentredDecimalTextField() {
    return createDecimalTextField(JTextField.CENTER);
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an
   * application-specific way.
   * 
   * @return
   */
  public static JFormattedTextField createAmountTextField() {
    return createDecimalTextField(JTextField.RIGHT);
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an
   * application-specific way.
   * 
   * @return
   */
  public static JFormattedTextField createPercentTextField() {
    return createPercentTextField(JTextField.RIGHT);
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an
   * application-specific way.
   * 
   * @return
   */
  public static JFormattedTextField createDecimalTextField(int alignment) {
    JFormattedTextField field = new JFormattedTextField(DECIMAL_FORMAT);

    configureGenericTextFieldBehaviour(field, alignment,
        new Dimension(BlacksmythSwingUtilities.getTextWidth(DECIMAL_FORMAT_PATTERN),
            (int) field.getPreferredSize().getHeight()),
        "0123456789.,");

    return field;
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit decimal numbers in an
   * application-specific way.
   * 
   * @return
   */
  public static JFormattedTextField createPercentTextField(int alignment) {
    JFormattedTextField field = new JFormattedTextField(PERCENT_FORMAT);

    configureGenericTextFieldBehaviour(field, alignment,
        new Dimension(BlacksmythSwingUtilities.getTextWidth(PERCENT_FORMAT_PATTERN),
            (int) field.getPreferredSize().getHeight()),
        "0123456789.,%");

    return field;
  }

  /**
   * Create a {@link JFormattedTextField} configured to edit dates in an
   * application-specific way.
   * 
   * @return
   */
  public static JFormattedTextField createDateTextField() {

    final JFormattedTextField field = new JFormattedTextField(DATE_FORMAT);

    configureGenericTextFieldBehaviour(field, JTextField.CENTER,
        new Dimension(BlacksmythSwingUtilities.getTextWidth(DATE_FORMAT_PATTERN),
            (int) field.getPreferredSize().getHeight()),
        "0123456789/");

    return field;
  }

  private static void configureGenericTextFieldBehaviour(final JFormattedTextField field, final int alignment,
      final Dimension preferredSize, final String allowedCharacters) {

    field.setPreferredSize(preferredSize);

    field.setInputVerifier(new FormatVerifier());

    field.setForeground(ViewPreferences.getInstance().getPreferredEditableCellColor());

    field.setHorizontalAlignment(alignment);

    field.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (allowedCharacters.indexOf(c) == -1) {
          e.consume(); // ignore event
        }
      }
    });

    field.addFocusListener(new FocusAdapter() {

      @Override
      public void focusGained(FocusEvent arg0) {
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            field.selectAll();
          }
        });
      }

      @Override
      public void focusLost(FocusEvent arg0) {
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            try {
              field.commitEdit();
              field.postActionEvent();
            } catch (ParseException e) {
              field.requestFocus();
            }
          }
        });
      }
    });
  }

  /**
   * Creates a <tt>JComboBox</tt> suitable for application table cell editors with
   * a static enumeration.
   * 
   * @return JComboBox
   */
  public static JComboBox<String> createTableComboBox() {
    JComboBox<String> comboBox = new JComboBox<String>();

    DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
    dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    dlcr.setForeground(ViewPreferences.getInstance().getPreferredEditableCellColor());
    comboBox.setRenderer(dlcr);

    return comboBox;
  }

  /**
   * Creates a standard {@link TitledBorder} of the specified title and color.
   * 
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

    BlacksmythSwingUtilities.setGlyphAsTitle(pane, currTabIndex, FontIconProvider.FontIcon.fa_table);

    currTabIndex++;

    pane.addTab("", graphComponent);
    pane.setToolTipTextAt(currTabIndex, " View as graph ");

    BlacksmythSwingUtilities.setGlyphAsTitle(pane, currTabIndex, FontIconProvider.FontIcon.fa_pie_chart);

    enableSelectionHilightedTabPane(pane);

    return pane;
  }

  public static void enableSelectionHilightedTabPane(JTabbedPane pane) {

    final Color selectedColor = Color.green.darker().darker().darker();

    if (pane.getTabCount() > 0) {
      pane.setBackgroundAt(0, selectedColor);
    }

    pane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent changeEvent) {
        JTabbedPane sourcePane = (JTabbedPane) changeEvent.getSource();
        int index = sourcePane.getSelectedIndex();

        for (int tabIndex = 0; tabIndex < sourcePane.getTabCount(); tabIndex++) {
          sourcePane.setBackgroundAt(tabIndex, sourcePane.getBackground());
        }
        sourcePane.setBackgroundAt(index, selectedColor);
      }
    });
  }

  /**
   * Creates a JButton that is enabled only when a single row of the specified
   * JTable is selected.
   * 
   * @param table
   * @return
   */
  @SuppressWarnings("serial")
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
   * 
   * @param table
   * @return
   */
  @SuppressWarnings("serial")
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

  @Override
  public boolean verify(JComponent input) {
    try {
      JFormattedTextField ftf = (JFormattedTextField) input;
      String text = ftf.getText();
      ftf.getFormatter().stringToValue(text);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}