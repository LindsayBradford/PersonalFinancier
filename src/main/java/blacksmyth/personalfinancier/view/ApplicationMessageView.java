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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.personalfinancier.control.IApplicationMessagePresenter;

public class ApplicationMessageView implements IApplicationMessageView {
  
  private JPanel messagePanel;
  private JLabel messageLabel;
  
  private Timer messageTimer;
  
  public ApplicationMessageView() {
    buildMessagePanel();
  }
  
  private Timer buildMessageTimer(int timeInMilliseconds) {
    Timer timer = new Timer(
        timeInMilliseconds, 
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            clearMessage();
          }
        }
    );
    timer.setRepeats(false);
    
    return timer;
  }
  
  private void buildMessagePanel() {
    messagePanel = new JPanel(new BorderLayout());
    messagePanel.setBorder(new EmptyBorder(2,5,5,5));
    
    messageLabel = new JLabel(" ");

    messageLabel.setMinimumSize(
        new Dimension(
            0,messageLabel.getPreferredSize().height
        )
    );
    
    messageLabel.setBorder(
       new CompoundBorder(
           new BevelBorder(BevelBorder.LOWERED),
           new EmptyBorder(2,5,5,5)
       )
    );
   
    messagePanel.add(
        messageLabel, 
        BorderLayout.CENTER
    );
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    assert ReflectionUtilities.classImplements(
        evt.getSource().getClass(), 
        IApplicationMessagePresenter.class
    );
    showMessage((String) evt.getNewValue());
  }


  @Override
  public void showMessage(String message) {
    messageLabel.setText(message);
  }

  @Override
  public void showMessage(String message, int timeInMilliseconds) {
    
    showMessage(message);
    
    if (messageTimer != null && messageTimer.isRunning()) {
      messageTimer.stop();
    }
    messageTimer = this.buildMessageTimer(timeInMilliseconds);
    
    messageTimer.start();
  }

  @Override
  public void clearMessage() {
    messageLabel.setText(" ");
  }

  public JPanel getPanel() {
    return this.messagePanel;  
  }
  
  public void bindViewComponent(final JComponent component) {
    final ApplicationMessageView view = this;
    component.addMouseListener(
        new MouseAdapter() {
          public void mouseEntered(MouseEvent e) {
            view.showMessage(
                (String) component.getClientProperty("AppMessage"),
                2000 // 2 seconds.
            );
         }
    });
  }

}
