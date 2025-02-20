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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blacksmyth.general.swing.Utilities;
import blacksmyth.general.swing.FontIconProvider;

public class PersonalFinancierView implements IPersonalFinancierView {
  
  private static final Logger LOG = LogManager.getLogger(PersonalFinancierView.class);

  private static Action ExitAction;

  private JFrame frame;
  private JTabbedPane componentPane;

  private IApplicationMessageView messageViewer;
  
  private PropertyChangeSupport support;

  public PersonalFinancierView() {
    support = new PropertyChangeSupport(this);

    setMessageViewer(createMessageBar());

    createFrame();
  }

  @Override
  public void raiseEvent(Events event) {
    support.firePropertyChange("ViewEvent", null, event);
  }
  
  @Override
  public void addObserver(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public Rectangle getBounds() {
    return frame.getBounds();
  }

  @Override
  public void setBounds(Rectangle windowBounds) {
    frame.setBounds(windowBounds);
  }

  @Override
  public void display() {
    LOG.info("Displaying View");
    frame.setVisible(true);
  }

  @Override
  public void setMessageViewer(IApplicationMessageView messageViewer) {
    this.messageViewer = messageViewer;
  }

  @Override
  public IApplicationMessageView getMessageViewer() {
    return this.messageViewer;
  }

  @Override
  public void addComponentView(IPersonalFinancierComponentView componentView) {

    JComponent swingComponent = (JComponent) componentView;

    componentPane.addTab((String) swingComponent.getClientProperty("TabName"), swingComponent);
  }

  public JFrame getWindowFrame() {
    return frame;
  }

  private void createFrame() {
    frame = new JFrame();

    frame.setTitle("Personal Financier");

    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent arg0) {
        raiseEvent(Events.ExitRequested);
      }
    });

    frame.getContentPane().add(((ApplicationMessageView) this.messageViewer).getPanel(), BorderLayout.PAGE_END);

    Toolkit.getDefaultToolkit().setDynamicLayout(false);

    frame.setJMenuBar(createMenu());

    frame.getContentPane().add(createMainToolbar(), BorderLayout.PAGE_START);

    frame.getContentPane().add(createContentPane(), BorderLayout.CENTER);

    frame.setBounds(ViewPreferences.getInstance().getWindowBounds());

    frame.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        raiseBoundsChangedEvent();
      }

      public void componentMoved(ComponentEvent e) {
        raiseBoundsChangedEvent();
      }

      private void raiseBoundsChangedEvent() {
        ViewPreferences.getInstance().setWindowBounds(frame.getBounds());
      }
    });
  }

  private Component createMainToolbar() {
    JToolBar toolbar = new JToolBar();

    toolbar.addSeparator();

    toolbar.add(createPreferencesButton());

    toolbar.addSeparator();

    toolbar.add(createAboutButton());

    return toolbar;
  }

  private JButton createAboutButton() {
    JButton button = new JButton();

    button.setForeground(Color.GRAY);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_info_circle);

    button.setToolTipText(" About ");

    button.putClientProperty("AppMessage", "About this application...");
    getMessageViewer().bindViewComponent(button);

    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        JOptionPane.showMessageDialog(frame,
            "<html><body>" + "This application depends on unmodified, embedded binary distributions of the following libraries:"
                + "<ul>" + "<li> JTattoo 1.6.13 for a radically different Swing Look & Feel"
                + "<li> FontAwesome 4.7.0 for icon-friendly font glyphs"
                + "<li> XChart 3.8.8 for charting"
                + "<li> Log4j 2.24.3 for logging"
                + "<li> Json-io 4.32.0 for JSon serialisation"  
                + "</ul>"
                + "This application also optionally depends on Bouncy Castle 1.80 for AES-256 password encrypted budget files.<br> "
                + "Drop the relevant provider jar into the same directory as this application's jar to enable encryption functionality."
                + "<p><hr><p><br>"
                + "This project wouldn't be possible without these excellent libraries." + "</body></html>");
        }
    });
    return button;
  }

  private JButton createPreferencesButton() {
    JButton button = new JButton();

    button.setForeground(Color.GRAY);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_cogs);

    button.setToolTipText(" Preferences ");

    button.putClientProperty("AppMessage", "Set your preferences (not yet implemented)...");
    getMessageViewer().bindViewComponent(button);

    return button;
  }

  private JComponent createContentPane() {
    componentPane = new JTabbedPane();

    componentPane.addMouseMotionListener(new MouseMotionAdapter() {

      @Override
      public void mouseMoved(MouseEvent e) {
        for (int i = 0; i < componentPane.getTabCount(); i++) {
          if (componentPane.getBoundsAt(i).contains(e.getPoint())) {
            JComponent component = (JComponent) componentPane.getComponentAt(i);

            getMessageViewer().showMessage((String) component.getClientProperty("AppMessage"), 2000 // 2 seconds.
            );

          } // if event point is within tab's bounds
        } // for all tab pane indices
      }
    });

    WidgetFactory.enableSelectionHilightedTabPane(componentPane);

    return componentPane;
  }

  private ApplicationMessageView createMessageBar() {
    return new ApplicationMessageView();
  }

  private JMenuBar createMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");

    fileMenu.add(new JMenuItem("Preferences"));

    fileMenu.addSeparator();

    fileMenu.add(createExitMenuItem());

    menuBar.add(fileMenu);

    return menuBar;
  }

  @SuppressWarnings("serial")
  private JMenuItem createExitMenuItem() {
    ExitAction = new AbstractAction("Exit") {
      public void actionPerformed(ActionEvent e) {
        raiseEvent(Events.ExitRequested);
      }
    };

    JMenuItem menuItem = new JMenuItem(ExitAction);

    Utilities.bindKeyStrokeToAction(menuItem, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK),
        ExitAction);

    return menuItem;
  }
}
