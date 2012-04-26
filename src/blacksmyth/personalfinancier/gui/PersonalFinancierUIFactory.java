package blacksmyth.personalfinancier.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class PersonalFinancierUIFactory {
  
  public static JFrame createJFrame() {
    
    JFrame frame = new JFrame("Personal Financier");
    
    frame.setDefaultCloseOperation(
        WindowConstants.EXIT_ON_CLOSE
    );
    
    frame.getContentPane().add(
        createContentPane(), 
        BorderLayout.CENTER
    );
    
    frame.getContentPane().add(
      createMessageBar(),
      BorderLayout.PAGE_END
    );
   
    frame.setBounds(
        Toolkit.getDefaultToolkit().getScreenSize().width/4, 
        Toolkit.getDefaultToolkit().getScreenSize().height/4, 
        Toolkit.getDefaultToolkit().getScreenSize().width/2, 
        Toolkit.getDefaultToolkit().getScreenSize().height/2
    );
    
    return frame;
  }
  
  
  private static JComponent createContentPane() {
    JTabbedPane pane = new JTabbedPane();
    pane.setBorder(new EmptyBorder(5,5,5,5));
    
    pane.addTab("Budget", BudgetUIFactory.createBudgetComponent());
    pane.addTab("Ledgers", LedgersUIFactory.createLedgersComponent());
    
    return pane;
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

class LedgersUIFactory {
  public static JComponent createLedgersComponent() {
    return new JLabel("Ledgers Component");
  }
}
