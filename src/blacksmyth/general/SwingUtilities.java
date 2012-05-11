/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.general;

import java.awt.FontMetrics;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumn;

/**
 * A library of generally reusabile utility functions for Swing.
 * Being a library, the class is not able to be instantiated.
 * @author linds
 *
 */
public final class SwingUtilities {

  /**
   * Locks the width of <tt>column</tt> to the <tt>width</tt> specified.
   * @param column
   * @param width
   */
  public static void lockColumnWidth(TableColumn column, int width) {
    column.setMinWidth(width);
    column.setMaxWidth(width);
  }
  
  /**
   * Returns the width in pixels that the supplied <tt>text</tt>
   * would consume if rendered with a <tt>JLabel</tt> using its default font.
   * @param text
   * @return width as an integer.
   */
  public static int getTextWidth(String text) {
    JLabel label = new JLabel(text);
    
    FontMetrics metrics = label.getFontMetrics(label.getFont());
    
    return (int) metrics.getStringBounds(
        text, 
        label.getGraphics()
    ).getWidth();

  }
  
  /**
   * Returns the longest string set as an item in the supplied <tt>comboBox</tt>
   * @param comboBox
   * @return
   */
  public static String getWidestTextInComboBox(JComboBox comboBox) {
    
    String longestFoundString = "";

    for(int i = 0; i < comboBox.getItemCount(); i++) {
      assert (comboBox.getItemAt(i).getClass().equals(String.class));
    
      String item = (String) comboBox.getItemAt(i);
      if (item.length() > longestFoundString.length()) {
        longestFoundString = item;
      }
    }
    return longestFoundString;
  }
  
  /**
   * Binds the supplied <code>actionToPerform</code> to the supplied <code>keyStroke</code> 
   * for the given <code>component</code>. <p>
   * This is a convenience method that auto-generates the necessary <code>keyStrokeLabel</code> for the 
   * caller when mapping the <code>keyStroke</code> to the <code>actionToPerform</code>.
   * @see #bindKeyStrokeToAction(JComponent, String, KeyStroke, AbstractAction)
   * @param component
   * @param keyStroke
   * @param actionToPerform
   */
  public static void bindKeyStrokeToAction(JComponent component, 
                                           KeyStroke keyStroke, 
                                           AbstractAction actionToPerform) {
    bindKeyStrokeToAction(
        component, 
        UUID.randomUUID().toString(),  // just use a random (semi-)unique label we don't need to track
        keyStroke, 
        actionToPerform
    );
  }

  /**
   * Binds the supplied <code>actionToPerform</code> to the supplied 
   * <code>keyStroke</code> for the given <code>component</code> 
   * via the <code>keyStrokeLabel</code>. <p>
   * Note that the <code>keyStroke</code> will trigger <code>actionToPerform</code>
   * whenever the component is in a focused window.
   * @see JComponent#WHEN_IN_FOCUSED_WINDOW
   * @param component
   * @param keyStrokeLabel
   * @param keyStroke
   * @param actionToPerform
   */
  
  public static void bindKeyStrokeToAction(JComponent component, 
                                           String keyStrokeLabel, 
                                           KeyStroke keyStroke, 
                                           AbstractAction actionToPerform) {
    
    InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    
    inputMap.put(
        keyStroke, 
        keyStrokeLabel
    );

    component.getActionMap().put(
        keyStrokeLabel, 
        actionToPerform
    );
  }
}
