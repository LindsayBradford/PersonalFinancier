/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import blacksmyth.general.swing.FontIconProvider.FontIcon;

/**
 * A library of generally reusabile utility functions for Swing. Being a
 * library, the class is not able to be instantiated.
 * 
 * @author linds
 *
 */
public final class Utilities {

  private static final Color POSITIVE_CASH_COLOUR = Color.GREEN;
  private static final Color NEGATIVE_CASH_COLOUR = Color.RED.brighter().brighter().brighter();

  /**
   * Locks the width of <tt>column</tt> to the <tt>width</tt> specified.
   * 
   * @param column
   * @param width
   */
  public static void lockColumnWidth(TableColumn column, int width) {
    column.setMinWidth(width);
    column.setMaxWidth(width);
  }

  /**
   * Returns the width in pixels that the supplied <tt>text</tt> would consume if
   * rendered with a <tt>JLabel</tt> using its default font.
   * 
   * @param text
   * @return width as an integer.
   */
  public static int getTextWidth(String text) {
    JLabel label = new JLabel(text);

    FontMetrics metrics = label.getFontMetrics(label.getFont());

    return (int) metrics.getStringBounds(text, label.getGraphics()).getWidth();

  }

  /**
   * Returns the longest string set as an item in the supplied <tt>comboBox</tt>
   * 
   * @param comboBox
   * @return
   */
  public static String getWidestTextInComboBox(JComboBox<String> comboBox) {

    String longestFoundString = "";

    for (int i = 0; i < comboBox.getItemCount(); i++) {
      assert (comboBox.getItemAt(i).getClass().equals(String.class));

      String item = comboBox.getItemAt(i);
      if (item.length() > longestFoundString.length()) {
        longestFoundString = item;
      }
    }
    return longestFoundString;
  }

  /**
   * Binds the supplied <code>actionToPerform</code> to the supplied
   * <code>keyStroke</code> for the given <code>component</code>.
   * <p>
   * This is a convenience method that auto-generates the necessary
   * <code>keyStrokeLabel</code> for the caller when mapping the
   * <code>keyStroke</code> to the <code>actionToPerform</code>.
   * 
   * @see #bindKeyStrokeToAction(JComponent, String, KeyStroke, AbstractAction)
   * @param component
   * @param keyStroke
   * @param actionToPerform
   */
  public static void bindKeyStrokeToAction(JComponent component, KeyStroke keyStroke, Action actionToPerform) {
    bindKeyStrokeToAction(component, UUID.randomUUID().toString(), // just use a random (semi-)unique label we don't
                                                                   // need to track
        keyStroke, actionToPerform);
  }

  /**
   * Binds the supplied <code>actionToPerform</code> to the supplied
   * <code>keyStroke</code> for the given <code>component</code> via the
   * <code>keyStrokeLabel</code>.
   * <p>
   * Note that the <code>keyStroke</code> will trigger
   * <code>actionToPerform</code> whenever the component is in a focused window.
   * 
   * @see JComponent#WHEN_IN_FOCUSED_WINDOW
   * @param component
   * @param keyStrokeLabel
   * @param keyStroke
   * @param actionToPerform
   */

  public static void bindKeyStrokeToAction(JComponent component, String keyStrokeLabel, KeyStroke keyStroke,
      Action actionToPerform) {

    InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(keyStroke, keyStrokeLabel);
    component.getActionMap().put(keyStrokeLabel, actionToPerform);
  }

  /**
   * Returns whether the mouse is currently located within the on-screen bounds of
   * component.
   * 
   * @param component
   * @return true if mouse is over component, false otherwise.
   */

  public static boolean mouseIsOverComponent(JComponent component) {
    Point mousePoint = MouseInfo.getPointerInfo().getLocation();
    Point componentPoint = component.getLocationOnScreen();

    if (mousePoint.x < componentPoint.x)
      return false;
    if (mousePoint.x > componentPoint.x + component.getWidth())
      return false;

    if (mousePoint.y < componentPoint.y)
      return false;
    if (mousePoint.y > componentPoint.y + component.getHeight())
      return false;

    return true;
  }

  public static void scrollRowToVisible(JTable table, int row) {
    table.scrollRectToVisible(table.getCellRect(row, 0, true));
  }

  public static void equalizeComponentSizes(List<JComponent> components) {
    Dimension maxComponentSize = new Dimension(0, 0);
    components.stream().forEach(c -> setMaximumSize(maxComponentSize, c));
    components.stream().forEach(c -> resize(c, maxComponentSize));
  }

  private static void setMaximumSize(Dimension maxComponentSize, JComponent component) {
    final Dimension componentSize = component.getPreferredSize();
    maxComponentSize.width = Math.max(maxComponentSize.width, (int) componentSize.getWidth());
    maxComponentSize.height = Math.max(maxComponentSize.height, (int) componentSize.getHeight());
  }

  private static void resize(JComponent component, Dimension size) {
    component.setPreferredSize(size);
    component.setMaximumSize(size);
    component.setMinimumSize(size);
  }

  @SuppressWarnings("serial")
  public static void enableEnterKeyEditsInJTable(final JTable theTable) {
    InputMap im = theTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    ActionMap am = theTable.getActionMap();

    KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

    im.put(enterKey, "Action.enter");
    am.put("Action.enter", new AbstractAction() {
      public void actionPerformed(ActionEvent evt) {
        int row = theTable.getSelectedRow();
        int column = theTable.getSelectedColumn();

        if (theTable.editCellAt(row, column)) {
          theTable.getEditorComponent().requestFocusInWindow();
        }
      }
    });
  }

  public static void setGlyphAsText(AbstractButton button, FontIcon icon) {
    button.setFont(FontIconProvider.getInstance().getFont());
    button.setText(icon.asString());
  }

  public static void setGlyphAsTitle(JTabbedPane pane, int tabIndex, FontIcon icon) {
    pane.setFont(FontIconProvider.getInstance().getFont());
    pane.setTitleAt(tabIndex, icon.asString());
  }
  
  public static void refreshLater(JComponent component) {
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            component.revalidate(); 
            component.repaint(); 
          }
        }
    );
  }
  
  public static boolean rowIsLastInTable(int row, JTable table) {
    return (row == table.getRowCount() - 1);
  }
  
  public static void renderCellBasedOnValue(Component cellRenderer, BigDecimal value) {
    if (value.compareTo(BigDecimal.ZERO) == -1) {
      cellRenderer.setForeground(NEGATIVE_CASH_COLOUR);
    }
    if (value.compareTo(BigDecimal.ZERO) == 1) {
      cellRenderer.setForeground(POSITIVE_CASH_COLOUR);
    }
  }
  
  public static BigDecimal getTableBigDecimalAt(JTable table, int row, int column) {
    return  (BigDecimal) table.getModel().getValueAt(row, column);
  }
}
