/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier;

import javax.swing.UIManager;

import blacksmyth.personalfinancier.gui.PersonalFinancierUIFactory;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;

public class PersonalFinancier {

  private static final long serialVersionUID = 1L;

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(
          new HiFiLookAndFeel()
        );
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    PersonalFinancierUIFactory.createJFrame().setVisible(true);
  }
}

