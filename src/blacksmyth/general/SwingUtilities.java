/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.general;

import java.awt.FontMetrics;

import javax.swing.JComboBox;
import javax.swing.JLabel;
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

}