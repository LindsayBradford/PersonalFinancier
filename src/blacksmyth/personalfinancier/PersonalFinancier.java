/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier;

import javax.swing.UIManager;


import com.jtattoo.plaf.hifi.HiFiLookAndFeel;

/**
 * The bootstrap "main" class for the PersonalFinancier.  It 
 * sets the application's look & feel, but delegates construction
 * of the application GUI dependency graph to the factory 
 * {@link PersonalFinancierUIFactory}.
 * @author linds
 */
public final class PersonalFinancier {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;

  /**
   * Bootstrap method for the PersonalFinancier.
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

