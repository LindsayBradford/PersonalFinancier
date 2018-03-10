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

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import blacksmyth.general.BlacksmythSwingUtilities;

@SuppressWarnings("serial")
public class PasswordPromptView extends JDialog implements IPasswordPromptView, ActionListener {
  private JFrame controllingFrame;

  private JPasswordField passwordField = new JPasswordField(10);
  private JButton okButton, cancelButton;

  private static String OK = "Ok";
  private static String CANCEL = "Cancel";

  private boolean passwordSpecified = false;

  public PasswordPromptView(PersonalFinancierView view) {
    super(view.getWindowFrame(), true);
    controllingFrame = view.getWindowFrame();
    buildView();

    this.setSize(this.getPreferredSize());
  }

  private void buildView() {

    this.setTitle("Specify File Access Password");

    Container contentPane = this.getContentPane();

    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    contentPane.setLayout(gbl);

    gbc.insets = new Insets(11, 11, 0, 11);

    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridheight = 1;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    contentPane.add(createPromptPanel(), gbc);

    gbc.insets = new Insets(17, 12, 11, 11);

    gbc.gridy++;
    gbc.weightx = 1;
    gbc.anchor = GridBagConstraints.EAST;
    contentPane.add(createButtonPanel(), gbc);
  }

  private JPanel createPromptPanel() {

    JPanel panel = new JPanel();

    passwordField.setActionCommand(OK);
    passwordField.addActionListener(this);

    JLabel label = new JLabel("Enter file password: ");
    label.setDisplayedMnemonic('p');
    label.setLabelFor(passwordField);

    panel.setLayout(new FlowLayout(FlowLayout.TRAILING));

    panel.add(label);
    panel.add(passwordField);

    return panel;
  }

  private JPanel createButtonPanel() {

    JPanel buttonPanel = new JPanel();

    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    buttonPanel.setLayout(gbl);

    gbc.insets = new Insets(0, 5, 0, 0);

    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.ipadx = 6;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    buttonPanel.add(getOkButton(), gbc);

    gbc.gridx = 1;

    buttonPanel.add(getCancelButton(), gbc);

    getRootPane().setDefaultButton(okButton);

    Vector<JComponent> buttons = new Vector<JComponent>();

    buttons.add(okButton);
    buttons.add(cancelButton);

    BlacksmythSwingUtilities.equalizeComponentSizes(buttons);

    buttons = null;

    return buttonPanel;
  }

  private JButton getCancelButton() {
    cancelButton = new JButton("Cancel");
    cancelButton.setMnemonic(KeyEvent.VK_C);
    cancelButton.setActionCommand(CANCEL);
    cancelButton.addActionListener(this);

    return cancelButton;
  }

  private JButton getOkButton() {
    okButton = new JButton("Ok");

    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.setActionCommand(OK);
    okButton.addActionListener(this);

    return okButton;
  }

  @Override
  public void clearPassword() {
    // Zero out the possible password, for security.
    char[] password = passwordField.getPassword();
    Arrays.fill(password, '0');

    passwordField.setText(null);

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

  // Must be called from the event dispatch thread.
  protected void resetFocus() {
    passwordField.requestFocusInWindow();
  }

  public void setVisible(boolean visible) {
    if (visible) {
      this.setLocationRelativeTo(controllingFrame);
    }
    super.setVisible(visible);
  }

  private void display() {
    setVisible(true);
  }

  @Override
  public boolean passwordSpecified() {
    return passwordSpecified;
  }

  @Override
  public void displaySavePrompt() {
    okButton.setText("Save");
    okButton.setMnemonic('S');

    display();
  }

  @Override
  public void displayLoadPrompt() {
    okButton.setText("Load");
    okButton.setMnemonic('L');

    display();
  }

  @Override
  public char[] getPassword() {
    return passwordField.getPassword();
  }

  @Override
  public void displayError(String errorMessage) {
    JOptionPane.showMessageDialog(controllingFrame, errorMessage, "Password Error", JOptionPane.ERROR_MESSAGE);
  }
}
