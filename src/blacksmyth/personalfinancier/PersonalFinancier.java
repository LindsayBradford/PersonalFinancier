/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class PersonalFinancier {

  private static final long serialVersionUID = 1L;

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(
          "com.jtattoo.plaf.hifi.HiFiLookAndFeel"
        );
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    PersonalFinancierFactory.createJFrame().setVisible(true);
  }
}

class PersonalFinancierFactory {
  
  public static JFrame createJFrame() {
    
    JFrame frame = new JFrame("Personal Financier");
    
    frame.getContentPane().add(
        createContentPane(), 
        BorderLayout.CENTER
    );
    
    frame.getContentPane().add(
      createMessageBar(),
      BorderLayout.PAGE_END
    );
   
    frame.setBounds(
        0, 0,    // x,y coords
        Toolkit.getDefaultToolkit().getScreenSize().width, 
        Toolkit.getDefaultToolkit().getScreenSize().height
    );

    
    return frame;
  }
  
  
  private static JComponent createContentPane() {
    JTabbedPane pane = new JTabbedPane();
    pane.setBorder(new EmptyBorder(5,5,5,5));
    
    pane.addTab("Budget", createBudgetComponent());
    pane.addTab("Ledgers", createLedgersComponent());
    
    return pane;
  }
  
  private static JComponent createBudgetComponent() {
    return new JLabel("Budget Component");
  }
  
  private static JComponent createLedgersComponent() {
    return new JLabel("Ledgers Component");
  }
  
  private static JComponent createMessageBar() {
    
    JPanel messagePanel = new JPanel(new BorderLayout());
    messagePanel.setBorder(new EmptyBorder(2,5,5,5));
    
    JLabel messageLabel = new JLabel("test");
   
    messageLabel.setBorder(
       new CompoundBorder(
           new BevelBorder(BevelBorder.LOWERED),
           new EmptyBorder(2,5,5,5)
       )
    );
   
    messagePanel.add(messageLabel, BorderLayout.CENTER);
  
    return messagePanel;
  }
}
