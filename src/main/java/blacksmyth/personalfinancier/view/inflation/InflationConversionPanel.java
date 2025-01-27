/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view.inflation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import blacksmyth.general.swing.Utilities;
import blacksmyth.personalfinancier.control.inflation.InflationConversionController;
import blacksmyth.personalfinancier.model.inflation.InflationConversionModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

@SuppressWarnings("serial")
public class InflationConversionPanel extends JPanel implements PropertyChangeListener {

  private LocalDate earliestDate;
  private LocalDate latestDate;

  private GridBagLayout gbl = new GridBagLayout();
  private GridBagConstraints gbc = new GridBagConstraints();

  private Vector<JComponent> labels = new Vector<JComponent>();
  private Vector<JComponent> fields = new Vector<JComponent>();

  private JFormattedTextField initialDateField = WidgetFactory.createDateTextField();
  private JFormattedTextField initialValueField = WidgetFactory.createAmountTextField();

  private JFormattedTextField conversionDateField = WidgetFactory.createDateTextField();
  private JFormattedTextField conversionValueField = WidgetFactory.createAmountTextField();

  private JFormattedTextField inflationOverPeriodField = WidgetFactory.createPercentTextField();
  private JFormattedTextField inflationPerAnnumField = WidgetFactory.createPercentTextField();

  private JLabel dateRangeLabel;

  private InflationConversionController controller;
  
  private PropertyChangeSupport support;

  public InflationConversionPanel(InflationConversionController controller) {
    super();
    this.controller = controller;
    this.support = new PropertyChangeSupport(this);
    this.controller.addPropertyListener(this);
    
    setLayout(gbl);
    createView();
}
  
  public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
    this.support.firePropertyChange("Inflation Conversion Event", null, null);
  }

  private void createView() {
    gbc.insets = new Insets(0, 5, 5, 5);

    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.25;
    gbc.weighty = 0;

    addFieldPair(new JLabel("Initial Date :"), initialDateField, 0, 0);

    final JFormattedTextField initialDateFieldFinal = this.initialDateField;

    this.initialDateField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.setInitialDate(initialDateFieldFinal.getText());
      }
    });

    addFieldPair(new JLabel("Initial Value :"), initialValueField, 0, 1);

    final JFormattedTextField initialValueFieldFinal = this.initialValueField;

    this.initialValueField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.setInitialValue(initialValueFieldFinal.getText());
      }
    });

    addFieldPair(new JLabel("Conversion Date :"), conversionDateField, 2, 0);

    final JFormattedTextField conversionDateFieldFinal = this.conversionDateField;

    this.conversionDateField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.setConversionDate(conversionDateFieldFinal.getText());
      }
    });

    addFieldPair(new JLabel("Conversion Value :"), conversionValueField, 2, 1);

    final JFormattedTextField conversionValueFieldFinal = this.conversionValueField;

    this.conversionValueField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.setConversionValue(conversionValueFieldFinal.getText());
      }
    });

    gbc.insets = new Insets(10, 5, 5, 5);

    addFieldPair(new JLabel("Inflation :"), inflationOverPeriodField, 0, 3);

    inflationOverPeriodField.setEnabled(false);
    fields.add(inflationOverPeriodField);

    addFieldPair(new JLabel("Inflation / Annum :"), inflationPerAnnumField, 2, 3);

    inflationPerAnnumField.setEnabled(false);
    fields.add(inflationPerAnnumField);

    gbc.insets = new Insets(10, 5, 5, 5);

    Utilities.equalizeComponentSizes(labels);
    Utilities.equalizeComponentSizes(fields);

    labels.clear();
    fields.clear();

    dateRangeLabel = new JLabel(getDateRangeMessage());
    dateRangeLabel.setHorizontalAlignment(SwingConstants.CENTER);

    gbc.insets = new Insets(12, 0, 0, 0);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.weightx = 1;

    add(dateRangeLabel, gbc);

  }

  private String getDateRangeMessage() {
    final String earliestDateString = dateToString(earliestDate);
    final String latestDateString = dateToString(latestDate);

    return "CPI calculations cover the date range " + earliestDateString + " - " + latestDateString + ".";
  }

  private String dateToString(LocalDate date) {
    if (date == null) {
      return "";

    }
    return date.format(WidgetFactory.DATE_FORMAT);
  }

  protected void addFieldPair(JLabel label, JFormattedTextField field, int gridX, int gridY) {

    label.setHorizontalAlignment(SwingConstants.RIGHT);

    gbc.anchor = GridBagConstraints.EAST;
    gbc.gridx = gridX;
    gbc.gridy = gridY;

    add(label, gbc);

    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = gridX + 1;

    add(field, gbc);

    labels.add(label);
    fields.add(field);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    assert evt.getSource().getClass().equals(InflationConversionModel.class) : "View/Model mismatch.";

    InflationConversionModel model = (InflationConversionModel) evt.getSource();

    this.initialDateField.setValue(model.getInitialDate().format(WidgetFactory.DATE_FORMAT));

    this.initialValueField.setValue(model.getInitialValue().getTotal().doubleValue());

    this.conversionDateField.setValue(model.getConversionDate().format(WidgetFactory.DATE_FORMAT));

    this.conversionValueField.setValue(model.getConversionValue().getTotal().doubleValue());

    this.inflationOverPeriodField.setValue(model.getInflationOverPeriod());

    this.inflationPerAnnumField.setValue(model.getInflationPerAnnum());

    this.earliestDate = model.getEarliestDate();
    this.latestDate = model.getLatestLatestDate();

    dateRangeLabel.setText(getDateRangeMessage());
  }
}
