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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;



public class PasswordPromptView extends JDialog implements IPasswordPromptView, ActionListener {
  private JFrame controllingFrame; 
  
  private JPasswordField passwordField = new JPasswordField(10);

  private static String OK = "Ok";
  private static String CANCEL = "Cancel";
  
  private boolean passwordSpecified = false;
  
  public PasswordPromptView(JFrame f) {
    super(f, true);
    controllingFrame = f;
    buildView();
  }
  
  //TODO: This is one f**king ugly dialog layout.  Make it pretty.
  private void buildView() {
    passwordField.setActionCommand(OK);
    passwordField.addActionListener(this);

    JLabel label = new JLabel("Enter file password: ");
    label.setLabelFor(passwordField);

    //Lay out everything.
    JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));

    textPane.add(label);
    textPane.add(passwordField);
    
    this.setLayout(new BorderLayout());

    this.add(textPane, BorderLayout.NORTH);
    add(createButtonPanel(), BorderLayout.SOUTH);
    
    this.setSize(this.getPreferredSize());
    
  }
  
  protected JComponent createButtonPanel() {
    JPanel p = new JPanel(new GridLayout(1,0));
    JButton okButton = new JButton("OK");
    JButton helpButton = new JButton("Cancel");

    okButton.setActionCommand(OK);
    helpButton.setActionCommand(CANCEL);
    okButton.addActionListener(this);
    helpButton.addActionListener(this);

    p.add(okButton);
    p.add(helpButton);

    return p;
  }

  @Override
  public void clearPassword() {
    //Zero out the possible password, for security.
    char[] password = passwordField.getPassword();
    Arrays.fill(password, '0');
    passwordSpecified = false;

    passwordField.selectAll();
    resetFocus();
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand() == OK) {
      passwordSpecified = true;
    }
    if (e.getActionCommand() == CANCEL) {
      clearPassword();
    }
    setVisible(false);
  }
  
  //Must be called from the event dispatch thread.
  protected void resetFocus() {
      passwordField.requestFocusInWindow();
  }
  
  public void setVisible(boolean visible) {
    if (visible) {
      this.setLocationRelativeTo(controllingFrame);
    }
    super.setVisible(visible);
  }
  
  public void display() {
    setVisible(true);
  }
 
  public char[] getPassword() {
    return passwordField.getPassword();
  }

  @Override
  public boolean passwordSpecified() {
    return passwordSpecified;
  }
}
